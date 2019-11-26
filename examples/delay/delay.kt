package delay

import java.util.concurrent.*
import kotlin.coroutines.*

/**
 * newSingleThreadScheduledExecutor 가 인자로 받는 ThreadFactory 는 하나의 함수를 정의한 interface 이므로
 * Lambda 와 호환됨
 */
private val executor = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "scheduler").apply { isDaemon = true }
}

suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): Unit = suspendCoroutine { cont ->
    // 주어진 시간 이후에 runnable or thread, lambda 를 실행한다.
    executor.schedule({ cont.resume(Unit) }, time, unit)
}
