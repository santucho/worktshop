package santucho.web.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory
import santucho.error.ApiError
import santucho.utils.ObjectMapperProvider
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark.after
import spark.Spark.exception
import spark.Spark.internalServerError
import spark.Spark.notFound

@Suppress("UNUSED_PARAMETER")
class ControllerAdvisor {

    private val mapper: ObjectMapper = ObjectMapperProvider.mapper

    private val afterAnyRequest = Filter { _: Request, response: Response ->
        response.type(APP_JSON)
        response.header("Content-Type", "application/json; charset=utf-8")
    }

    private fun exceptionHandler(e: Exception, req: Request, res: Response) {
        logger.error(e.message)
        res.body(mapper.writeValueAsString(e.message ?: ""))
        res.type(APP_JSON)
        res.status(500)
    }

    private val notFoundHandler = { _: Request, res: Response ->
        val notFoundError = ApiError.NotFound(HttpStatus.SC_NOT_FOUND, "Not found")
        res.type("application/json")
        res.header("Content-Type", "application/json; charset=utf-8")
        mapper.writeValueAsString(notFoundError)
    }

    private val internalServerErrorHandler = { _: Request, res: Response ->
        val internalServerError = ApiError.InternalServerError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error")
        res.type("application/json")
        res.header("Content-Type", "application/json; charset=utf-8")
        mapper.writeValueAsString(internalServerError)
    }

    init {
        after(afterAnyRequest)
        exception(Exception::class.java, this::exceptionHandler)
        notFound(notFoundHandler)
        internalServerError(internalServerErrorHandler)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ControllerAdvisor::class.java)
        private const val APP_JSON = "application/json"
    }
}
