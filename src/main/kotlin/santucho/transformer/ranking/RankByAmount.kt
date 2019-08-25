package santucho.transformer.ranking

import arrow.core.Option
import arrow.core.flatMap
import arrow.core.orElse
import arrow.core.toOption
import com.google.inject.Inject
import com.google.inject.Singleton
import java.math.BigDecimal
import org.slf4j.LoggerFactory
import santucho.client.MeliClient
import santucho.model.Publication

@Singleton
class RankByAmount @Inject constructor(
    private val meliClient: MeliClient
) : RankStrategy {
    override fun strategy() = RankType.BY_AMOUNT

    override fun sort(
        sites: List<String>,
        publications: List<Publication>
    ): List<Publication> {
        val ratios = getRatios(sites)

        /**
         * We try to get the converted price for each publication
         * Then we use fold function over the Option to see the real value and filter the publications that doesn't have converted price
         * */
        val publicationWithConversion = publications.map { p ->
            getConvertedPrice(ratios, p)
        }.fold(listOf<Pair<Publication, BigDecimal>>()) { acc, elem -> elem.fold({ acc }, { acc + it }) }

        return publicationWithConversion
            .sortedBy { pwc -> pwc.second }
            .map { pwc -> pwc.first }
    }

    private fun getRatios(
        sites: List<String>
    ): Map<String, BigDecimal> {
        return sites.map { siteId ->
            meliClient.getSite(siteId).flatMap { site ->
                meliClient.getCurrencyConversion(site.defaultCurrencyId,
                    USD_CURRENCY
                ).map {
                    site.defaultCurrencyId to it.ratio
                }
            }
            /**
             * We decided that isn't critical if we can't retrieve a currency ratio
             * */
        }.fold(mapOf()) { acc, elem -> elem.fold({ acc }, { acc + it }) }
    }

    private fun getConvertedPrice(
        ratios: Map<String, BigDecimal>,
        publication: Publication
    ): Option<Pair<Publication, BigDecimal>> {
        /**
         * We try to use the ratios that we already have, if we don't have it we try to retrieve it (again).
         * */
        val ratio = ratios[publication.price.currency].toOption()
            .orElse {
                meliClient.getCurrencyConversion(publication.price.currency, USD_CURRENCY)
                    .map { it.ratio }
                    .toOption()
            }
        return ratio.map { publication to it }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RankByAmount::class.java)
        private const val USD_CURRENCY = "USD"
    }
}
