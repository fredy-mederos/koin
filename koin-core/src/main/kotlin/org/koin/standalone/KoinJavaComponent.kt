package org.koin.standalone

import org.koin.KoinContext
import kotlin.jvm.internal.Reflection


/**
 * @author @fredy-mederos
 */
object KoinJavaComponent {

    /**
     * Retrieve given dependency lazily
     * @param clazz - dependency class
     * @param name - bean name / optional
     * @param parameters - dependency parameters / optional
     */
    @JvmOverloads
    @JvmStatic
    fun <T> inject(
        clazz: Class<T>,
        name: String = "",
        parameters: InjectParameters = emptyParameters
    ): Lazy<T> {
        return lazy { get(clazz, name, parameters) }
    }

    /**
     * Retrieve given dependency
     * @param clazz - dependency class
     * @param name - bean name / optional
     * @param parameters - dependency parameters / optional
     */
    @JvmOverloads
    @JvmStatic
    fun <T> get(
        clazz: Class<T>,
        name: String = "",
        parameters: InjectParameters = emptyParameters
    ): T {
        val kclazz = Reflection.getOrCreateKotlinClass(clazz)
        val koinContext = (StandAloneContext.koinContext as KoinContext)

        val beanDefinition = if (name.isBlank())
            koinContext.beanRegistry.searchAll(kclazz)
        else
            koinContext.beanRegistry.searchByName(name)

        return koinContext.resolveInstance(
            kclazz,
            { parameters.getParameters() },
            { beanDefinition }) as T
    }

    /**
     * inject lazily given property
     * @param key - key property
     */
    @JvmOverloads
    @JvmStatic
    fun <T> property(key: String, defaultValue: T? = null): Lazy<T?> {
        return lazy { getProperty(key, defaultValue) }
    }

    /**
     * Retrieve given property
     * @param key - key property
     */
    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    @JvmStatic
    fun <T> getProperty(key: String, defaultValue: T? = null): T? {
        val koinContext = (StandAloneContext.koinContext as KoinContext)
        return koinContext.propertyResolver.properties[key] as T? ?: defaultValue
    }
}

interface InjectParameters {
    fun getParameters(): Map<String, Any>
}

private val emptyParameters = object : InjectParameters {
    override fun getParameters(): Map<String, Any> = emptyMap()
}