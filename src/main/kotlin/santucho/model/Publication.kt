package santucho.model

import java.math.BigDecimal

data class Publication(
    val id: String,
    val siteId: String,
    val title: String,
    val permalink: String,
    val price: Price,
    val seller: User
)

data class Price(
    val amount: BigDecimal,
    val currency: String,
    val discountPercentage: BigDecimal
)

interface User {
    val id: Int
}

data class SimpleUser(
    override val id: Int
) : User

data class FullUser(
    override val id: Int,
    val siteId: String,
    val nickname: String,
    val countryId: String,
    val permalink: String,
    val points: Int
) : User
