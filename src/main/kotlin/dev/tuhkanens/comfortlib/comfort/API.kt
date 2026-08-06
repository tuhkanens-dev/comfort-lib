package dev.tuhkanens.comfortlib.comfort

import java.util.concurrent.ConcurrentHashMap

abstract class API {

    @PublishedApi
    internal val registry: ConcurrentHashMap<Class<*>, Any> = ConcurrentHashMap()

    inline fun <reified T : Any> registerAPI(implementation: Any) {
        registry[T::class.java] = implementation
    }

    inline fun <reified T : Any> getAPI(): T {
        val implementation: Any = registry[T::class.java]
            ?: throw IllegalStateException("API '${T::class.java}' is not registered!")
        return implementation as T
    }

}