package santucho.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import spark.Request
import spark.Response
import spark.Spark.get

@Suppress("UNUSED_PARAMETER")
class ExampleController @Inject constructor(
    private val mapper: ObjectMapper
) {

    init {
        get("/some/url", this::getSome, this.mapper::writeValueAsString)
    }

    inner class ExampleModel(val method: String, val path: String)

    private fun getSome(request: Request, response: Response): ExampleModel {
        return ExampleModel("GET", "/some/url")
    }
}
