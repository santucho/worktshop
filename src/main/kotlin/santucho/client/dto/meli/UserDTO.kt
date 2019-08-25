package santucho.client.dto.meli

data class UserDTO(
    val id: Int,
    val nickname: String,
    val countryId: String,
    val points: Int,
    val siteId: String,
    val permalink: String
)
