package santucho.injection

import com.google.inject.AbstractModule

class MainGuiceModule : AbstractModule() {

    override fun configure() {
        /**
         * Here you can do some magic stuff like...
         * - import other modules as submodules
         * - create singletons
         * - bind multiple implementations of an interface (strategy)
         * */
        install(SecondaryGuiceModule())
    }

    /**
     * Declare custom dependencies here using @Provides and @Singleton
     * If you want to provide multiple instances of the same class you can assign a name to each instance with @Named
     * */
}
