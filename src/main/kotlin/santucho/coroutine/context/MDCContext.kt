package santucho.context

import kotlinx.coroutines.ThreadContextElement
import org.slf4j.MDC
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * @see <a href="https://github.com/Kotlin/kotlinx.coroutines/blob/master/integration/kotlinx-coroutines-slf4j/src/MDCContext.kt"></a>
 * */

/**
 * The value of [MDC] context map.
 * See [MDC.getCopyOfContextMap].
 */
typealias MDCContextMap = Map<String, String>?

class MDCContext(
    /**
     * The value of [MDC] context map.
     */
    private val contextMap: MDCContextMap = MDC.getCopyOfContextMap()
) : ThreadContextElement<MDCContextMap>, AbstractCoroutineContextElement(Key) {
    /**
     * Key of [MDCContext] in [CoroutineContext].
     */
    companion object Key : CoroutineContext.Key<MDCContext>

    /** @suppress */
    override fun updateThreadContext(context: CoroutineContext): MDCContextMap {
        val oldState = MDC.getCopyOfContextMap()
        setCurrent(contextMap)
        return oldState
    }

    /** @suppress */
    override fun restoreThreadContext(context: CoroutineContext, oldState: MDCContextMap) {
        setCurrent(oldState)
    }

    private fun setCurrent(contextMap: MDCContextMap) {
        if (contextMap == null) {
            MDC.clear()
        } else {
            MDC.setContextMap(contextMap)
        }
    }
}
