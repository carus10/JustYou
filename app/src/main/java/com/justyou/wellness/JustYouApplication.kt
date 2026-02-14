package com.justyou.wellness

import android.app.Application
import com.justyou.wellness.data.WellnessDatabase

class JustYouApplication : Application() {
    val database: WellnessDatabase by lazy {
        WellnessDatabase.getDatabase(this)
    }
}
