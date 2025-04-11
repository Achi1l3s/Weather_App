package com.faist.weatherapp

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class ChatScrollBenchmark {


    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollChat() = benchmarkRule.measureRepeated(
        packageName = "com.faist.weatherapp",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
    ) {
        startActivityAndWait()
    }
}