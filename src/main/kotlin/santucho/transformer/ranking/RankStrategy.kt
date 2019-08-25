package santucho.transformer.ranking

import santucho.model.Publication

interface RankStrategy {

    fun rank(
        sites: List<String>,
        publications: List<Publication>
    ): Pair<RankType, List<Publication>> = strategy() to sort(sites, publications)

    fun strategy(): RankType

    fun sort(
        sites: List<String>,
        publications: List<Publication>
    ): List<Publication>
}
