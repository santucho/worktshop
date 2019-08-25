package santucho.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import santucho.model.ExampleModel
import santucho.utils.ObjectMapperProvider
import spark.Request
import spark.Response
import spark.Spark.get

@Suppress("UNUSED_PARAMETER")
class ExampleController {
    private val mapper: ObjectMapper = ObjectMapperProvider.mapper

    /**
     * As a framework for creating web applications Spark provides methods like:
     * - get
     * - post
     * - put
     * - patch
     * - delete
     * - head
     * - trace
     * - connect
     * - options
     * */
    init {
        /**
         * get(path: String, route: Route, transformer: ResponseTransformer)
         * - Route: functional interface of Object handle(Request request, Response response) throws Exception;
         * - Response transformer: functional interface of String render(Object model) throws Exception;
         * */
        get("/some/ok", this::getSomeOk, this.mapper::writeValueAsString)
        get("/some/wrong", this::getSomeWrong, this.mapper::writeValueAsString)
    }

    private fun getSomeOk(request: Request, response: Response): ExampleModel {
        return ExampleModel("id", "value")
    }

    private fun getSomeWrong(request: Request, response: Response): ExampleModel {
        throw RuntimeException("error msg")
    }
}
