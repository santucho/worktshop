package santucho.service

import com.google.inject.Inject
import com.google.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.slf4j.LoggerFactory
import santucho.client.MeliClient
import santucho.client.dto.meli.SearchDTO
import santucho.coroutine.runBlocking
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

    fun getMeliTop(info: MeliTopInfo): Map<RankType, List<Publication>> {
        val product = info.product
        val sites = info.sites
        val searches = sites
            .map { site -> meliClient.getSearch(site, product) }

        val publications = getPublications(searches)
        return buildRanksAsync(sites, publications)
    }

    private fun getPublications(searches: List<SearchDTO>): List<Publication> {
        return searches.flatMap { search ->
            search.results.map { resultTransformer.transform(it) }
        }
    }

    private fun buildRanksAsync(
        sites: List<String>,
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
                    strategy.rank(sites, publications)
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
