package santucho.web.controller

import arrow.core.Either
import com.google.inject.Inject
import org.slf4j.LoggerFactory
import santucho.errors.ApiError
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
    private val responseTransformer: ResponseTransformer
) {

    init {
        get("/meli-top", this::getMeliTop, this.responseTransformer::serialize)
    }

    private fun getMeliTop(request: Request, response: Response): Either<ApiError, Map<RankType, List<Publication>>> {

        /**
         * Retrieves request parameters and validates them
         * */
        val meliTop = meliValidator.getMeliTopParameters(request)
            .fold(
                { it.handleValidationErrors() },
                {
                    logger.info("Trip policies request received for product ${it.product} and countries: ${it.sites.all.joinToString()}")
                    meliService.getMeliTop(it)
                }
            )

        /**
         * Fold function allow us to break the Either-effect and execute functions in both cases
         * It's similar to bimap but not implicates a transformation into another Either
         * */
        meliTop.fold(
            { logger.error("Meli top went wrong"); response.status(it.code) },
            { logger.info("Meli top finished 0K") }
        )

        return meliTop
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MeliController::class.java)
    }
}
