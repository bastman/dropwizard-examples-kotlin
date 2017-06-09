package com.example.demo.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

fun <R : Any> R.logger(): Lazy<Logger> {
    return lazy { getLogger(this.javaClass) }
}

private fun <T : Any> getLogger(forClass: Class<T>): Logger {
    return LoggerFactory.getLogger(unwrapCompanionClass(forClass).name)
}

private fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) {
        ofClass.enclosingClass
    } else {
        ofClass
    }
}

object AppLogger {
    fun get(clazz: Class<*>): Logger = LoggerFactory.getLogger(clazz)
}

fun AppLogger(clazz: Class<*>): Logger = AppLogger.get(clazz)
