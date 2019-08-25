package santucho.web.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import org.slf4j.LoggerFactory
import santucho.errors.ApiError
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark.after
import spark.Spark.before
import spark.Spark.exception

@Suppress("UNUSED_PARAMETER")
class ControllerAdvisor @Inject constructor(
    private val mdcHelper: MDCHelper,
    private val mapper: ObjectMapper
) {

    private val beforeAnyRequest = Filter { request: Request, _: Response ->
        mdcHelper.clearMDC()
        mdcHelper.loadMDC(request)
    }

    private val afterAnyRequest = Filter { _: Request, response: Response ->
        response.type("application/json")
        response.header("Content-Type", "application/json; charset=utf-8")
    }

    private fun illegalArgumentHandler(iae: IllegalArgumentException, req: Request, res: Response) {
        noticeError(iae)
        val apiError = ApiError.IllegalArgument(iae.message ?: "")
        respondWithError(res, apiError)
    }

    private fun exceptionHandler(e: Exception, req: Request, res: Response) {
        noticeError(e)
        val apiError = ApiError.UnknownError(e.message ?: "")
        respondWithError(res, apiError)
    }

    private fun respondWithError(res: Response, apiError: ApiError) {
        res.body(mapper.writeValueAsString(apiError))
        res.type("application/json")
        res.status(apiError.code)
    }

    private fun noticeError(e: Exception) {
        logger.error(e.message)
    }

    init {
        before(beforeAnyRequest)
        after(afterAnyRequest)
        exception(IllegalArgumentException::class.java, this::illegalArgumentHandler)
        exception(Exception::class.java, this::exceptionHandler)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ControllerAdvisor::class.java)
    }
}
