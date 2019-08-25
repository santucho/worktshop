package santucho.web.controller.validator

import arrow.core.*
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.validated.applicative.applicative
import com.google.inject.Singleton
import santucho.errors.RequestError
import santucho.model.MeliTopInfo
import santucho.web.controller.getCommaSeparatedQueryParam
import santucho.web.controller.getQueryParam
import spark.Request

@Singleton
class MeliValidator {

    /**
     * A functional way for checking all params/query-params is to implement Validated
     * It allow us to validate all parameters and give proper feedback about all errors, rather than just the first one
     * https://arrow-kt.io/docs/arrow/data/validated/#validated
     * */
    fun getMeliTopParameters(request: Request): ValidatedNel<RequestError, MeliTopInfo> {
        val product = request.getQueryParam(PRODUCT).toValidatedNel()
        val sites = getSites(request).toValidatedNel()
        return ValidatedNel.applicative(Nel.semigroup<RequestError>()).map(product, sites) {
            MeliTopInfo(it.a, it.b)
        }.fix()
    }

    private fun getSites(request: Request): Validated<RequestError, Nel<String>> =
        validatedNel(request, SITES, validateSites())

    private fun validatedNel(
        request: Request,
        param: String,
        validator: (String) -> Boolean
    ): Validated<RequestError, Nel<String>> =
        request.getCommaSeparatedQueryParam(param).withEither { validatedParam ->
            validatedParam.filterOrElse(
                { ps -> ps.all.all { p -> validator(p) } },
                { RequestError.WrongParamValue(param) }
            )
        }

    private fun validateSites() = { site: String ->
        sites.contains(site)
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