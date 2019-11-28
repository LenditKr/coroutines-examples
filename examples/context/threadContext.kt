package context

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

fun newFixedThreadPoolContext(nThreads: Int, name: String) = ThreadContext(nThreads, name)
fun newSingleThreadContext(name: String) = ThreadContext(1, name)

/**
 * ContinuationInterceptor, CoroutineDispatcher
 * https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-dispatcher/index.html
 * https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/common/src/CoroutineDispatcher.kt#L30
 *
 * An arbitrary Executor can be converted to dispatcher with asCoroutineDispatcher extension function.
 * https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/new-fixed-thread-pool-context.html
 *
 * Marks coroutine context element that intercepts coroutine continuations.
 * The coroutines framework uses ContinuationInterceptor.Key to retrieve the interceptor and intercepts all coroutine continuations with interceptContinuation invocations.
 *
 * ContinuationInterceptor: 코루틴 컨텍스트를 coroutine 의
 * 코루틴 프레임워크는 continuationInterceptor.Key 를 사용해서 인터셉터를 검색하고
 * interceptContinuation 실행을 통해 모든 coroutine continuations 을 가로챈다.
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
     * 원래의 [continuation] 을 wrapping 한 continuation 을 리턴하고 그래서 모든 재개를 인터셉트함.
     * 필요시 coroutine 프레임워크에 의해서 실행되고 원래 각각의 continuation 인스턴스 내부에 결과가 caching 됨
     */
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        ThreadContinuation(continuation.context.fold(continuation) { cont, element ->
            if (element != this@ThreadContext && element is ContinuationInterceptor)
                element.interceptContinuation(cont) else cont
        })

    private inner class ThreadContinuation<T>(val cont: Continuation<T>) : Continuation<T>{
        override val context: CoroutineContext = cont.context

        override fun resumeWith(result: Result<T>) {
            executor.execute { cont.resumeWith(result) }
        }
    }
}

