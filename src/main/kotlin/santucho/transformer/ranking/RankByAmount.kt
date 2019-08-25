package santucho.transformer.ranking

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
        }.fold(listOf<Pair<Publication, BigDecimal>>()) { acc, elem -> elem?.let { acc + elem } ?: acc }

        return publicationWithConversion
            .sortedBy { pwc -> pwc.second }
            .map { pwc -> pwc.first }
    }

    private fun getRatios(
        sites: List<String>
    ): Map<String, BigDecimal> {
        return sites.map { siteId ->
            try {
                val site = meliClient.getSite(siteId)
                val conversion = meliClient.getCurrencyConversion(site.defaultCurrencyId, USD_CURRENCY)
                site.defaultCurrencyId to conversion.ratio
            } catch (ex: Throwable) {
                logger.error("Error retrieving conversion")
                null
            }

            /**
             * We decided that isn't critical if we can't retrieve a currency ratio
             * */
        }.fold(mapOf()) { acc, elem -> elem?.let { acc + elem } ?: acc }
    }

    private fun getConvertedPrice(
        ratios: Map<String, BigDecimal>,
        publication: Publication
    ): Pair<Publication, BigDecimal>? {
        /**
         * We try to use the ratios that we already have, if we don't have it we try to retrieve it (again).
         * */
        val ratio = ratios[publication.price.currency]
            ?: try {
                meliClient.getCurrencyConversion(publication.price.currency, USD_CURRENCY).ratio
            } catch (ex: Throwable) {
                logger.error("Error retrieving conversion for ${publication.price.currency}")
                null
            }

        return ratio?.let { publication to it }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RankByAmount::class.java)
        private const val USD_CURRENCY = "USD"
    }
}
