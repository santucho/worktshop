package santucho.client

import com.fasterxml.jackson.core.type.TypeReference
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import org.slf4j.LoggerFactory
import santucho.client.dto.example.ExampleDTO
import santucho.web.client.restClient.ApiRestClient

@Singleton
class ExampleClient @Inject constructor(
    @Named("exampleRestClient") private val exampleRestClient: ApiRestClient
) {

    fun getSome(): ExampleDTO {
        return exampleRestClient
            .get(EXAMPLE_URL)
            .getResponseAs(EXAMPLE_TYPE_REF)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ExampleClient::class.java)
        private const val EXAMPLE_URL = "/example"
        private val EXAMPLE_TYPE_REF = object : TypeReference<ExampleDTO>() {}
    }
}
