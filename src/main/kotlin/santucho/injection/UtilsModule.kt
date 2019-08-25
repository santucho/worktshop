package santucho.injection

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import santucho.utils.ObjectMapperProvider

class UtilsModule : AbstractModule() {

    override fun configure() {}

    @Provides
    @Singleton
    fun provideObjectMapper(): ObjectMapper {
        return ObjectMapperProvider.mapper
    }
}
