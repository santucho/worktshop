package santucho.service

import com.google.inject.Inject
import com.google.inject.Singleton
import org.slf4j.LoggerFactory
import santucho.client.ExampleClient
import santucho.model.ExampleModel
import santucho.transformer.ExampleTransformer

@Singleton
class ExampleService @Inject constructor(
    private val exampleClient: ExampleClient,
    private val exampleTransformer: ExampleTransformer
) {

    fun getSome(): ExampleModel {
        val exampleDTO = exampleClient.getSome()
        return exampleTransformer.transform(exampleDTO)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ExampleService::class.java)
    }
}
