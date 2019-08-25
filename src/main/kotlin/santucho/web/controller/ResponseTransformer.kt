package santucho.web.controller

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Singleton

@Singleton
class ResponseTransformer @Inject constructor(
    private val mapper: ObjectMapper
) {

    /**
     * Fancy way to wrap Arrow-kt-Either serialization because jackson doesn't support it
     * */
    fun serialize(model: Any): String {
        return if (model is Either<*, *>) {
            this.mapper.eitherToJson(model)
        } else {
            this.mapper.writeValueAsString(model)
        }
    }

    private fun ObjectMapper.eitherToJson(either: Either<*, *>): String {
        return either.fold(
            { x -> this.writeValueAsString(x) },
            { x -> this.writeValueAsString(x) }
        )
    }
}
