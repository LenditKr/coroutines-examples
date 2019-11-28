package context

import javax.swing.*
import kotlin.coroutines.*

/**
 * ContinuationInterceptor 구현체이며 context element
 * 이 코루틴을 실행하는 스레드의 이름 안에서 현재 동작하는 코루틴을 식별할 수 있는 디버깅 기능을 위함
 *
 */
object Swing : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        SwingContinuation(continuation)
}

/**
 * Swing UI 이벤트 핸들러 스레드에 coroutine continuation 에 대한 제어권을 전달하는 예제 코드
 * Resume 의 제어권을 Swing UI 스레드에 전달함
 */
private class SwingContinuation<T>(val cont: Continuation<T>) : Continuation<T> {
    override val context: CoroutineContext = cont.context

    override fun resumeWith(result: Result<T>) {
        SwingUtilities.invokeLater { cont.resumeWith(result) }
    }
}
