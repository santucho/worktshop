package santucho.web.filter

import com.google.inject.Singleton
import org.slf4j.MDC
import spark.Request

@Singleton
class MDCHelper {

    fun loadMDC(request: Request) {
        MDC.put("req.method", request.requestMethod())
        MDC.put("req.requestURI", request.uri())
    }

    fun clearMDC() {
        MDC.clear()
    }
}
