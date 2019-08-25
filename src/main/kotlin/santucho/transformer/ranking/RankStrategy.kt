package santucho.transformer.ranking

import santucho.client.MeliClient
import santucho.model.Publication

interface RankStrategy {

    fun rank(
        meliClient: MeliClient,
        sites: List<String>,
        publications: List<Publication>
    ): Pair<RankType, List<Publication>> = strategy() to sort(meliClient, sites, publications)

    fun strategy(): RankType

    fun sort(
        meliClient: MeliClient,
        sites: List<String>,
        publications: List<Publication>
    ): List<Publication>

}