package com.example.reviewphim


import android.app.Application
import android.provider.Settings
import com.example.reviewphim.models.User
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class MyApplication:Application() {


    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var database: DatabaseReference

    override fun onCreate() {
        super.onCreate()
        firebaseAnalytics = Firebase.analytics

        val android_id = Settings.Secure.getString(
            this.getContentResolver(),
            Settings.Secure.ANDROID_ID
        )

        firebaseAnalytics.logEvent("open_app_review_phim") {
            param("user_$android_id",  Calendar.getInstance().getTime().toString())
        }

    }

    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        database.child("users").child(userId).setValue(user)
    }

//    @Synchronized
//    fun getDefaultTracker(): Tracker? {
//        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//        if (sTracker == null) {
//            sTracker = sAnalytics?.newTracker(R.xml.global_tracker)
//        }
//        return sTracker
//    }

}
