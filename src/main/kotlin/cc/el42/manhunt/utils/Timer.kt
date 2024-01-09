package cc.el42.manhunt.utils

import java.util.Timer
import java.util.TimerTask
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Timer {
    var duration: Duration = 0.seconds
    private var timer: Timer? = null

    fun start() {
        timer = Timer()
        timer?.schedule(
            object : TimerTask() {
                override fun run() {
                    duration += 1.seconds
                }
            },
            0,
            1000
        )
    }

    fun stop() {
        timer?.cancel()
    }
}