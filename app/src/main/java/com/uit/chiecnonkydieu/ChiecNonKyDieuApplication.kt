package com.uit.chiecnonkydieu

import android.app.Application
import com.google.android.gms.ads.MobileAds

class ChiecNonKyDieuApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) { }
        container = AppContainer()
    }

}