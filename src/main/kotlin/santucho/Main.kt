package santucho

import com.google.inject.Guice
import org.eclipse.jetty.server.HttpConfiguration
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.gzip.GzipHandler
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.slf4j.LoggerFactory
import santucho.injection.ClientModule
import santucho.injection.ConfigModule
import santucho.injection.ControllerModule
import santucho.injection.MainGuiceModule
import santucho.injection.UtilsModule
import santucho.web.filter.ExampleFilter
import spark.servlet.SparkApplication
import spark.servlet.SparkFilter
import java.util.TimeZone
import kotlin.system.exitProcess

class Main {

    companion object {
        private val logger = LoggerFactory.getLogger(Main::class.java)

        private const val CONTEXT_PATH = "/"
        private const val PORT = 9290

        @JvmStatic
        fun main(args: Array<String>) {
            Main().run()
        }
    }

    fun run() {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

        val handlers = ContextHandlerCollection()

        val handler = ServletContextHandler()
        handler.contextPath = CONTEXT_PATH
        handler.gzipHandler = GzipHandler()

        handlers.addHandler(handler)

        listOf(
            /**
             * Insert your HttpRequest's filters here...
             * */
            FilterHolder(ExampleFilter()),
            object : FilterHolder(SparkFilter()) {
                init {
                    this.setInitParameter("applicationClass", SparkApp::class.java.name)
                }
            }
        ).forEach { h -> handler.addFilter(h, "*", null) }

        try {
            val server = Server()
            server.handler = handlers

            val httpConfig = HttpConfiguration()
            httpConfig.sendServerVersion = false
            httpConfig.sendXPoweredBy = false
            val httpFactory = HttpConnectionFactory(httpConfig)
            val httpConnector = ServerConnector(server, httpFactory)
            httpConnector.port = PORT
            server.connectors = arrayOf(httpConnector)
            server.start()
            httpConnector.start()
            server.join()
        } catch (e: Exception) {
            logger.error("Error starting the application", e)
            exitProcess(1)
        }
    }

    class SparkApp : SparkApplication {

        override fun init() {

            /**
             * Insert your Guice modules here... But only the important ones
             * */
            val injector = Guice.createInjector(
                ClientModule(),
                ConfigModule(),
                ControllerModule(),
                MainGuiceModule(), // e.g
                UtilsModule()
            )

            injector.allBindings.keys.forEach { injector.getInstance(it) }
        }
    }
}
