package santucho.injection

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config
import santucho.utils.UrlUtils
import santucho.web.client.HttpClient
import santucho.web.client.HttpClientCreator
import santucho.web.client.restClient.ApiRestClient
import santucho.web.client.restClient.ApiRestClientBuilder

class ClientModule : AbstractModule() {

    override fun configure() {}

    @Provides
    @Singleton
    @Named("meliHttpClient")
    fun provideMeliHttpClient(
        httpClientCreator: HttpClientCreator
    ) = httpClientCreator.createClient(client = "meli")

    @Provides
    @Singleton
    @Named("meliRestClient")
    fun provideMeliRestClient(
        @Named("meliHttpClient") httpClient: HttpClient,
        urlUtils: UrlUtils,
        config: Config
    ): ApiRestClient {
        return ApiRestClientBuilder(httpClient, urlUtils = urlUtils, config = config).build()
    }
}
