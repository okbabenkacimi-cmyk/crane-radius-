package com.craneradius.geometry

import kotlin.math.cos
import kotlin.math.sin

object CraneGeometryEngine {

    fun calculate(input: GeometryInput): Result<GeometryResult> {
        validate(input)?.let { return Result.failure(it) }

        val thetaRad = Math.toRadians(input.boomAngleDeg)

        val horizontalRadius = input.boomLengthM * cos(thetaRad)
        val verticalComponent = input.boomLengthM * sin(thetaRad)
        val tipHeight = input.pivotHeightM + verticalComponent
        val boundaryRadius = horizontalRadius + input.planningMarginM

        return Result.success(
            GeometryResult(
                horizontalRadiusM = horizontalRadius,
                verticalComponentM = verticalComponent,
                tipHeightM = tipHeight,
                boundaryRadiusM = boundaryRadius
            )
        )
    }

    private fun validate(input: GeometryInput): IllegalArgumentException? {
        if (!input.boomLengthM.isFinite() || input.boomLengthM < 0.0) {
            return IllegalArgumentException(
                "Boom length must be a finite, non-negative number."
            )
        }
        if (!input.boomAngleDeg.isFinite() ||
            input.boomAngleDeg < 0.0 ||
            input.boomAngleDeg > 90.0
        ) {
            return IllegalArgumentException(
                "Boom angle must be a finite number between 0 and 90 degrees."
            )
        }
        if (!input.pivotHeightM.isFinite() || input.pivotHeightM < 0.0) {
            return IllegalArgumentException(
                "Pivot height must be a finite, non-negative number."
            )
        }
        if (!input.planningMarginM.isFinite() || input.planningMarginM < 0.0) {
            return IllegalArgumentException(
                "Planning margin must be a finite, non-negative number."
            )
        }
        return null
    }
}
