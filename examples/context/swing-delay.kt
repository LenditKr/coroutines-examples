package context

import javax.swing.*
import kotlin.coroutines.*

suspend fun Swing.delay(millis: Int): Unit = suspendCoroutine { cont ->
    // 주어진 시간 뒤에 continuation 을 재개한다.
    Timer(millis) { cont.resume(Unit) }.apply {
        isRepeats = false
        start()
    }
}
