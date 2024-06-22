package com.uit.chiecnonkydieu

import android.app.Application

class ChiecNonKyDieuApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }

}