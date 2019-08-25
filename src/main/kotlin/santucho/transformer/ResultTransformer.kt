package santucho.transformer

import arrow.core.Option
import arrow.core.getOrElse
import com.google.inject.Singleton
import java.math.BigDecimal
import santucho.client.dto.meli.ResultDTO
import santucho.model.Price
import santucho.model.Publication
import santucho.model.SimpleUser

@Singleton
class ResultTransformer {

    /**
     * We can pass unordered parameters to a constructor if we explicit the parameter name.
     * We can ignore parameters that have a default value
     * */
    fun transform(result: ResultDTO): Publication {
        return Publication(
            id = result.id,
            siteId = result.siteId,
            title = result.title,
            permalink = result.permalink,
            price = getPrice(result.price, result.currencyId, result.originalPrice),
            seller = SimpleUser(result.seller.id)
        )
    }

    private fun getPrice(
        amount: BigDecimal,
        currency: String,
        originalAmount: Option<BigDecimal>
    ): Price {
        val discountPercentage = originalAmount.map { oa ->
            ONE_HUNDRED.subtract(amount * ONE_HUNDRED / oa)
        }.getOrElse { BigDecimal.ZERO }
        return Price(
            amount = amount,
            currency = currency,
            discountPercentage = discountPercentage
        )
    }

    companion object {
        private val ONE_HUNDRED = BigDecimal.valueOf(100)
    }
}
