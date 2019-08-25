package santucho.service

import arrow.core.Either
import arrow.core.Nel
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.applicative.map
import arrow.core.extensions.list.traverse.sequence
import arrow.core.fix
import arrow.core.sequence
import com.google.inject.Inject
import com.google.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.slf4j.LoggerFactory
import santucho.client.MeliClient
import santucho.client.dto.meli.SearchDTO
import santucho.coroutine.runBlocking
import santucho.errors.ApiError
import santucho.model.MeliTopInfo
import santucho.model.Publication
import santucho.transformer.ResultTransformer
import santucho.transformer.ranking.RankStrategy
import santucho.transformer.ranking.RankType
import santucho.web.SharedData

@Singleton
class MeliService @Inject constructor(
    private val meliClient: MeliClient,
    private val resultTransformer: ResultTransformer,
    private val rankStrategies: Set<@JvmSuppressWildcards RankStrategy>
) {

    fun getMeliTop(info: MeliTopInfo): Either<ApiError, Map<RankType, List<Publication>>> {
        val product = info.product
        val sites = info.sites
        val publicationsEither = sites
            /**
             * Because we make a query for each country, any of them can fail but our client always returns a result
             * */
            .map { site -> meliClient.getSearch(site, product) }
            /**
             * Sequence function allow us to transform List<Either> into Either<List>
             * We decided not to continue if any of the searches failed...
             * */
            .sequence(Either.applicative())
            .map { getPublications(it.fix()) }

        return publicationsEither.map { publications ->
            buildRanksAsync(sites, publications)
        }
    }

    private fun getPublications(searches: Nel<SearchDTO>): List<Publication> {
        return searches.all.flatMap { search ->
            search.results.map { resultTransformer.transform(it) }
        }
    }

    private fun buildRanksAsync(
        sites: Nel<String>,
        publications: List<Publication>
    ): Map<RankType, List<Publication>> {
        SharedData.SOME_SHARE_STATE.set("SOME SHARE STATE VALUE")
        /**
         * SharedData is a wrapped ThreadLocal, so modifications will affect the entire context
         * */
        return runBlocking {
            logger.info("Initial SharedData: ${SharedData.SOME_SHARE_STATE.get<String>()}")
            val asyncRanks = rankStrategies.map { strategy ->
                /**
                 * All ranking strategies run in parallel
                 * */
                async {
                    logger.info("${strategy.strategy()} SharedData: ${SharedData.SOME_SHARE_STATE.get<String>()}")
                    logger.info("Ranking async ${strategy.strategy()}")
                    strategy.rank(meliClient, sites.all, publications)
                }
            }
            /**
             * The blocking execution awaits for all asynchronous executions to finish
             * */
            asyncRanks.awaitAll().toMap()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MeliService::class.java)
    }
}
