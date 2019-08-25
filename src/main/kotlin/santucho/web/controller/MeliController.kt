package santucho.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import org.slf4j.LoggerFactory
import santucho.model.Publication
import santucho.service.MeliService
import santucho.transformer.ranking.RankType
import santucho.web.controller.validator.MeliValidator
import spark.Request
import spark.Response
import spark.Spark.get

class MeliController @Inject constructor(
    private val meliValidator: MeliValidator,
    private val meliService: MeliService,
    private val mapper: ObjectMapper
) {

    init {
        get("/meli-top", this::getMeliTop, this.mapper::writeValueAsString)
    }

    private fun getMeliTop(request: Request, response: Response): Map<RankType, List<Publication>> {

        /**
         * Retrieves request parameters and validates them
         * */
        val info = meliValidator.getMeliTopParameters(request)
        logger.info("Trip policies request received for product ${info.product} and countries: ${info.sites.joinToString()}")
        return meliService.getMeliTop(info)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MeliController::class.java)
    }
}
