package com.ran.kolibri.extension

import org.apache.log4j.Logger

fun Logger.logInfo(fn: () -> String) {
    if (isInfoEnabled) {
        info(fn.invoke())
    }
}

fun Logger.logDebug(fn: () -> String) {
    if (isDebugEnabled) {
        debug(fn.invoke())
    }
}

fun Logger.logTrace(fn: () -> String) {
    if (isTraceEnabled) {
        trace(fn.invoke())
    }
}
