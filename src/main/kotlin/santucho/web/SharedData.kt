package santucho.web

import kotlinx.coroutines.asContextElement
import java.util.HashMap

enum class SharedData(val key: String) {

    SOME_SHARE_STATE("SOME_SHARE_STATE");

    companion object {

        fun clear() {
            state.remove()
        }

        fun asContextElement() = state.asContextElement()

        val state = object : ThreadLocal<MutableMap<String, Any>>() {
            override fun initialValue(): MutableMap<String, Any> {
                return HashMap()
            }
        }
    }

    fun set(value: Any?) {
        value?.let {
            state.get()[key] = value
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(): T? {
        return state.get()[this.key] as? T
    }
}
