package santucho.coroutine

import santucho.context.MDCContext
import santucho.web.SharedData

fun <T> runBlocking(
    block: suspend kotlinx.coroutines.CoroutineScope.() -> T
): T {
    return kotlinx.coroutines.runBlocking(
        context = SharedData.asContextElement() + MDCContext(),
        block = block
    )
}

fun <T> runBlocking(
    context: kotlin.coroutines.CoroutineContext,
    block: suspend kotlinx.coroutines.CoroutineScope.() -> T
): T {
    return kotlinx.coroutines.runBlocking(
        context = context + SharedData.asContextElement() + MDCContext(),
        block = block
    )
}
