package santucho.client.dto.meli

import java.math.BigDecimal

data class CurrencyDTO(
    val id: String,
    val symbol: String,
    val description: String,
    val decimalPlaces: Int
)

data class CurrencyConversionDTO(
    val ratio: BigDecimal
)
