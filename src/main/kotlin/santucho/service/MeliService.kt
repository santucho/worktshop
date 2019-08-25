package santucho.service

import com.google.inject.Inject
import com.google.inject.Singleton
import org.slf4j.LoggerFactory
import santucho.client.MeliClient
import santucho.client.dto.meli.SearchDTO
import santucho.model.MeliTopInfo
import santucho.model.Publication
import santucho.transformer.ResultTransformer
import santucho.transformer.ranking.RankStrategy
import santucho.transformer.ranking.RankType

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
        /**
         * We'll parallelize this later
         * */
        return rankStrategies.map { strategy ->
            strategy.rank(sites, publications)
        }.toMap()
    }

    private fun getPublications(searches: List<SearchDTO>): List<Publication> {
        return searches.flatMap { search ->
            search.results.map { resultTransformer.transform(it) }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MeliService::class.java)
    }
}
