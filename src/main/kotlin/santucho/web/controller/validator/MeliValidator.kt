package santucho.web.controller.validator

import com.google.inject.Singleton
import santucho.errors.BadRequestException
import santucho.model.MeliTopInfo
import spark.Request

@Singleton
class MeliValidator {

    /**
     * Without using very complex logic we can only give information about the first error found
     * */
    fun getMeliTopParameters(request: Request): MeliTopInfo {
        val product = request.queryParams(PRODUCT) ?: throw BadRequestException(errorMessage = "No product")
        val sites = getSites(request)
        validateSites(sites)
        return MeliTopInfo(product, sites)
    }

    private fun getSites(request: Request) =
        request.queryParams(SITES)?.split(",") ?: listOf()

    private fun validateSites(
        rqSites: List<String>
    ) {
        if (rqSites.isEmpty()) throw BadRequestException(errorMessage = "Empty sites")
        if (rqSites.any { s -> !sites.contains(s) }) throw BadRequestException(errorMessage = "Invalid site")
    }

    companion object {
        private const val PRODUCT = "product"
        private const val SITES = "sites"
        private val sites = listOf(
            "MLV", "MGT", "MCR", "MHN", "MSV", "MLA", "MLM", "MLU", "MPY", "MPA",
            "MRD", "MLB", "MBO", "MPE", "MCU", "MLC", "MNI", "MCO", "MEC", "MPT"
        )
    }
}
