package santucho.client.dto.meli

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
    val originalPrice: BigDecimal?
)

data class SellerDTO(
    val id: Int
)

data class InstallmentsDTO(
    val quantity: Int,
    val amount: BigDecimal,
    val rate: BigDecimal,
    val currencyId: String
)
