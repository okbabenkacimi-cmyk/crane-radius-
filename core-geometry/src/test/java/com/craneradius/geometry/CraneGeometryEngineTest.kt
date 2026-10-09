package com.craneradius.geometry

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CraneGeometryEngineTest {

    private val tolerance = 0.0001

    @Test
    fun `zero degrees yields full boom length`() {
        val r = CraneGeometryEngine.calculate(
            GeometryInput(10.0, 0.0, 0.0, 0.0)
        ).getOrThrow()
        assertEquals(10.0, r.horizontalRadiusM, tolerance)
    }

    @Test
    fun `thirty degrees`() {
        val r = CraneGeometryEngine.calculate(
            GeometryInput(10.0, 30.0, 0.0, 0.0)
        ).getOrThrow()
        assertEquals(8.6603, r.horizontalRadiusM, tolerance)
    }

    @Test
    fun `forty five degrees`() {
        val r = CraneGeometryEngine.calculate(
            GeometryInput(10.0, 45.0, 0.0, 0.0)
        ).getOrThrow()
        assertEquals(7.0711, r.horizontalRadiusM, tolerance)
    }

    @Test
    fun `sixty degrees`() {
        val r = CraneGeometryEngine.calculate(
            GeometryInput(10.0, 60.0, 0.0, 0.0)
        ).getOrThrow()
        assertEquals(5.0, r.horizontalRadiusM, tolerance)
    }

    @Test
    fun `ninety degrees`() {
        val r = CraneGeometryEngine.calculate(
            GeometryInput(10.0, 90.0, 0.0, 0.0)
        ).getOrThrow()
        assertEquals(0.0, r.horizontalRadiusM, tolerance)
    }

    @Test
    fun `vertical component at thirty degrees`() {
        val r = CraneGeometryEngine.calculate(
            GeometryInput(10.0, 30.0, 0.0, 0.0)
        ).getOrThrow()
        assertEquals(5.0, r.verticalComponentM, tolerance)
    }

    @Test
    fun `tip height adds pivot height`() {
        val r = CraneGeometryEngine.calculate(
            GeometryInput(10.0, 30.0, 2.0, 0.0)
        ).getOrThrow()
        assertEquals(7.0, r.tipHeightM, tolerance)
    }

    @Test
    fun `boundary adds margin`() {
        val r = CraneGeometryEngine.calculate(
            GeometryInput(10.0, 0.0, 0.0, 2.5)
        ).getOrThrow()
        assertEquals(12.5, r.boundaryRadiusM, tolerance)
    }

    @Test
    fun `negative length rejected`() {
        assertTrue(
            CraneGeometryEngine.calculate(
                GeometryInput(-1.0, 30.0, 0.0, 0.0)
            ).isFailure
        )
    }

    @Test
    fun `negative angle rejected`() {
        assertTrue(
            CraneGeometryEngine.calculate(
                GeometryInput(10.0, -5.0, 0.0, 0.0)
            ).isFailure
        )
    }

    @Test
    fun `angle above ninety rejected`() {
        assertTrue(
            CraneGeometryEngine.calculate(
                GeometryInput(10.0, 95.0, 0.0, 0.0)
            ).isFailure
        )
    }

    @Test
    fun `NaN length rejected`() {
        assertTrue(
            CraneGeometryEngine.calculate(
                GeometryInput(Double.NaN, 30.0, 0.0, 0.0)
            ).isFailure
        )
    }

    @Test
    fun `infinite angle rejected`() {
        assertTrue(
            CraneGeometryEngine.calculate(
                GeometryInput(10.0, Double.POSITIVE_INFINITY, 0.0, 0.0)
            ).isFailure
        )
    }

    @Test
    fun `negative pivot height rejected`() {
        assertTrue(
            CraneGeometryEngine.calculate(
                GeometryInput(10.0, 30.0, -1.0, 0.0)
            ).isFailure
        )
    }

    @Test
    fun `negative margin rejected`() {
        assertTrue(
            CraneGeometryEngine.calculate(
                GeometryInput(10.0, 30.0, 0.0, -0.5)
            ).isFailure
        )
    }

    @Test
    fun `NaN margin rejected`() {
        assertTrue(
            CraneGeometryEngine.calculate(
                GeometryInput(10.0, 30.0, 0.0, Double.NaN)
            ).isFailure
        )
    }
}
