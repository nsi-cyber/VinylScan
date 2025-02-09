package com.nsicyber.vinylscan.utils

import android.app.Activity
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewManager @Inject constructor() {
    private var reviewManager: ReviewManager? = null

    fun initReviewFlow(activity: Activity) {
        if (reviewManager == null) {
            reviewManager = ReviewManagerFactory.create(activity)
        }
        val request = reviewManager?.requestReviewFlow()
        request?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reviewManager?.launchReviewFlow(activity, task.result)
            }
        }
    }


} 