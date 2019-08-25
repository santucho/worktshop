package santucho.utils

import com.google.inject.Singleton
import java.io.InputStreamReader
import java.util.*

@Singleton
class I18n {
    fun getMessage(key: String, locale: Locale): String =
        ResourceBundle.getBundle("i18n/messages", locale, UTF8Control).getString(key)

    fun getMessage(key: String, args: List<String>, locale: Locale): String {
        val message = ResourceBundle.getBundle("i18n/messages", locale, UTF8Control).getString(key)
        return args.foldIndexed(message) { index, acc, arg ->
            acc.replace("{$index}", arg)
        }
    }
}

object UTF8Control : ResourceBundle.Control() {

    override fun newBundle(
        baseName: String,
        locale: Locale,
        format: String,
        loader: ClassLoader,
        reload: Boolean
    ): ResourceBundle {
        val bundleName = toBundleName(baseName, locale)
        val resourceName: String = toResourceName(bundleName, "properties")
        if (reload) {
            loader.getResource(resourceName).openConnection()?.let { connection ->
                connection.useCaches = false
                connection.getInputStream().use {
                    return PropertyResourceBundle(InputStreamReader(it, "UTF-8"))
                }
            }
        }

        loader.getResourceAsStream(resourceName).use {
            return PropertyResourceBundle(InputStreamReader(it, "UTF-8"))
        }
    }
}
