package santucho.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import santucho.model.ExampleModel
import santucho.service.ExampleService
import spark.Request
import spark.Response
import spark.Spark.get

@Suppress("UNUSED_PARAMETER")
class ExampleController @Inject constructor(
    private val exampleService: ExampleService,
    private val mapper: ObjectMapper
) {

    init {
        get("/some/url", this::getSome, this.mapper::writeValueAsString)
    }

    private fun getSome(request: Request, response: Response): ExampleModel {
        return exampleService.getSome()
    }
}
