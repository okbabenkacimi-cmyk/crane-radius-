package com.craneradius

import android.app.Application
import com.craneradius.data.AssessmentRepository

class CraneRadiusApp : Application() {

    val assessmentRepository: AssessmentRepository by lazy {
        AssessmentRepository.create(this)
    }
}
