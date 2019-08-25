package santucho.client

import com.fasterxml.jackson.core.type.TypeReference
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import org.slf4j.LoggerFactory
import santucho.client.dto.meli.CurrencyConversionDTO
import santucho.client.dto.meli.SearchDTO
import santucho.client.dto.meli.SiteDTO
import santucho.client.dto.meli.UserDTO
import santucho.web.client.restClient.ApiRestClient

@Singleton
class MeliClient @Inject constructor(
    @Named("meliRestClient") private val meliRestClient: ApiRestClient
) {

    fun getSite(site: String): SiteDTO {
        logger.info("Retrieving site information for $site")
        return meliRestClient
            .get(String.format(SITE_DETAIL_URL, site))
            .getResponseAs(SITE_TYPE_REF)
    }

    fun getSearch(site: String, query: String): SearchDTO {
        logger.info("Retrieving search results for $query in site $site")
        return meliRestClient
            .get(String.format(SEARCH_URL, site, query))
            .getResponseAs(SEARCH_TYPE_REF)
    }

    fun getCurrencyConversion(
        currencyFrom: String,
        currencyTo: String
    ): CurrencyConversionDTO {
        logger.info("Retrieving currency conversion from $currencyFrom to $currencyTo")
        return meliRestClient
            .get(String.format(CURRENCY_CONVERSIONS, currencyFrom, currencyTo))
            .getResponseAs(CURRENCY_CONVERSION_TYPE_REF)
    }

    fun getUser(userId: Int): UserDTO {
        logger.info("Retrieving user $userId")
        return meliRestClient
            .get(String.format(USERS_URL, userId))
            .getResponseAs(USER_TYPE_REF)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MeliClient::class.java)

        private const val SITES_URL = "/sites"
        private const val SITE_DETAIL_URL = "$SITES_URL/%s"
        private const val SEARCH_URL = "$SITES_URL/%s/search?q=%s"
        private const val CURRENCY_CONVERSIONS = "/currency_conversions/search?from=%s&to=%s"
        private const val USERS_URL = "/users/%s"

        private val SITE_TYPE_REF = object : TypeReference<SiteDTO>() {}
        private val SEARCH_TYPE_REF = object : TypeReference<SearchDTO>() {}
        private val CURRENCY_CONVERSION_TYPE_REF = object : TypeReference<CurrencyConversionDTO>() {}
        private val USER_TYPE_REF = object : TypeReference<UserDTO>() {}
    }
}
