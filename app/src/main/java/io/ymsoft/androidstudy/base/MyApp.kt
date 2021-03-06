package io.ymsoft.androidstudy.base

import android.app.Application
import io.ymsoft.androidstudy.BuildConfig
import timber.log.Timber
import timber.log.Timber.Forest.plant

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(MyDebugTree())
        }
    }
}

class MyDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format(
            "[C:%s] [L:%s] [M:%s] ",
            super.createStackElementTag(element),
            element.lineNumber,
            element.methodName
        )
    }
}