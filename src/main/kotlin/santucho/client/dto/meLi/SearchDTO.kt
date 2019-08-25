package santucho.client.dto.meli

import arrow.core.Option
import arrow.core.toOption
import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigDecimal

data class SearchDTO(
    val siteId: String,
    val query: String,
    val results: List<ResultDTO>
)

data class ResultDTO(
    val id: String,
    val siteId: String,
    val title: String,
    val seller: SellerDTO,
    val price: BigDecimal,
    val currencyId: String,
    val availableQuantity: Int,
    val soldQuantity: Int,
    val buyingMode: String,
    val permalink: String,
    val installments: InstallmentsDTO,
    val originalPrice: Option<BigDecimal>
) {
    @JsonCreator
    constructor(
        id: String,
        siteId: String,
        title: String,
        seller: SellerDTO,
        price: BigDecimal,
        currencyId: String,
        availableQuantity: Int,
        soldQuantity: Int,
        buyingMode: String,
        permalink: String,
        installments: InstallmentsDTO,
        originalPrice: BigDecimal?
    ): this(id, siteId, title, seller, price, currencyId, availableQuantity, soldQuantity, buyingMode, permalink, installments, originalPrice.toOption())
}

data class SellerDTO(
    val id: Int
)

data class InstallmentsDTO(
    val quantity: Int,
    val amount: BigDecimal,
    val rate: BigDecimal,
    val currencyId: String
)
