package santucho.transformer.ranking

import com.google.inject.Singleton
import santucho.model.Publication

@Singleton
class RankByDiscount : RankStrategy {
    override fun strategy() = RankType.BY_DISCOUNT

    override fun sort(
        sites: List<String>,
        publications: List<Publication>
    ): List<Publication> {
        return publications.sortedByDescending { it.price.discountPercentage }
    }
}
