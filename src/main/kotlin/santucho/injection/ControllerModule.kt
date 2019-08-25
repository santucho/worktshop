package santucho.injection

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import santucho.web.controller.ExampleController
import santucho.web.controller.MeliController
import santucho.web.filter.ControllerAdvisor

class ControllerModule : AbstractModule() {

    override fun configure() {
        bind(ControllerAdvisor::class.java).`in`(Singleton::class.java)
        bind(ExampleController::class.java).`in`(Singleton::class.java)
        bind(MeliController::class.java).`in`(Singleton::class.java)
    }
}
