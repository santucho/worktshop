package santucho.client

import arrow.core.Either
import arrow.core.flatMap
import com.fasterxml.jackson.core.type.TypeReference
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import org.slf4j.LoggerFactory
import santucho.client.dto.meli.CurrencyConversionDTO
import santucho.client.dto.meli.SearchDTO
import santucho.client.dto.meli.SiteDTO
import santucho.client.dto.meli.UserDTO
import santucho.errors.ApiError
import santucho.web.client.restClient.ApiRestClient

@Singleton
class MeliClient @Inject constructor(
    @Named("meliRestClient") private val meliRestClient: ApiRestClient
) {

    fun getSite(site: String): Either<ApiError, SiteDTO> {
        logger.info("Retrieving site information for $site")
        val siteInfo = meliRestClient.get(String.format(SITE_DETAIL_URL, site)).flatMap {
            it.getResponseAs(SITE_TYPE_REF)
        }

        siteInfo.fold(
            { logger.error("Cannot retrieve site information") },
            { logger.info("Site information obtained") }
        )

        return siteInfo
    }

    fun getSearch(site: String, query: String): Either<ApiError, SearchDTO> {
        logger.info("Retrieving search results for $query in site $site")
        val search = meliRestClient.get(String.format(SEARCH_URL, site, query)).flatMap {
            it.getResponseAs(SEARCH_TYPE_REF)
        }

        search.fold(
            { logger.error("Cannot retrieve search results") },
            { logger.info("Search results obtained") }
        )

        return search
    }

    fun getCurrencyConversion(
        currencyFrom: String,
        currencyTo: String
    ): Either<ApiError, CurrencyConversionDTO> {
        logger.info("Retrieving currency conversion from $currencyFrom to $currencyTo")
        val currencyConversion = meliRestClient.get(String.format(CURRENCY_CONVERSIONS, currencyFrom, currencyTo)).flatMap {
            it.getResponseAs(CURRENCY_CONVERSION_TYPE_REF)
        }

        currencyConversion.fold(
            { logger.error("Cannot retrieve currency conversion") },
            { logger.info("Currency conversion obtained") }
        )

        return currencyConversion
    }

    fun getUser(userId: Int): Either<ApiError, UserDTO> {
        logger.info("Retrieving user $userId")
        val user = meliRestClient.get(String.format(USERS_URL, userId)).flatMap {
            it.getResponseAs(USER_TYPE_REF)
        }

        user.fold(
            { logger.error("Cannot retrieve user") },
            { logger.info("User obtained") }
        )

        return user
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
