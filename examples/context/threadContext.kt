package context

import java.util.concurrent.*
import java.util.concurrent.atomic.*
import kotlin.concurrent.*
import kotlin.coroutines.*

fun newFixedThreadPoolContext(nThreads: Int, name: String) = ThreadContext(nThreads, name)
fun newSingleThreadContext(name: String) = ThreadContext(1, name)

/**
 * https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/new-fixed-thread-pool-context.html
 *
 * Marks coroutine context element that intercepts coroutine continuations.
 * The coroutines framework uses ContinuationInterceptor.Key to retrieve the interceptor and intercepts all coroutine continuations with interceptContinuation invocations.
 * ContinuationInterceptor: 코루틴 컨텍스트를 coroutine 의
 * 코루틴 프레임워크는 ontinuationInterceptor.Key 를 사용해서 인터셉터를 검색하고 interceptContinuation 실행을 통해 모든 coroutine continuations 을 가로챈다.
 */
class ThreadContext(
    nThreads: Int,
    name: String
) : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    val threadNo = AtomicInteger()

    // 스레드 풀을 만들고 실제 요청시에 스레드를 만드나???
    val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(nThreads) { target ->
        // thread를 만들고 start = false 이므로 스케쥴에 등록한다.
        // 데몬스레드가 살아있다면 JVM이 유지된다.
        thread(start = false, isDaemon = true, name = name + "-" + threadNo.incrementAndGet()) {
            target.run()
        }
    }

    /**
     * Returns continuation that wraps the original [continuation], thus intercepting all resumptions.
     * This function is invoked by coroutines framework when needed and the resulting continuations are
     * cached internally per each instance of the original [continuation].
     *
     * This function may simply return original [continuation] if it does not want to intercept this particular continuation.
     *
     * When the original [continuation] completes, coroutine framework invokes [releaseInterceptedContinuation]
     * with the resulting continuation if it was intercepted, that is if `interceptContinuation` had previously
     * returned a different continuation instance.
     */
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        ThreadContinuation(continuation.context.fold(continuation) { cont, element ->
            if (element != this@ThreadContext && element is ContinuationInterceptor)
                element.interceptContinuation(cont) else cont
        })

    /**
     *
     */
    private inner class ThreadContinuation<T>(val cont: Continuation<T>) : Continuation<T>{
        override val context: CoroutineContext = cont.context

        override fun resumeWith(result: Result<T>) {
            executor.execute { cont.resumeWith(result) }
        }
    }
}

