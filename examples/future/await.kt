package future

import java.util.concurrent.*
import kotlin.coroutines.*

suspend fun <T> CompletableFuture<T>.await(): T =
    /**
     * Suspending functions may invoke any regular functions,
     * but to actually suspend execution they must invoke some other suspending function.
     * In particular, this await implementation invokes a suspending function suspendCoroutine that is defined in the standard library (in kotlin.coroutines package) as a top-level suspending function:
     *
     * continuation 객체의 실행 상태를 캡처하고 이 continuation 을 specified block 에 전달한다.
     *
     * 코루틴의 실행을 재개하기 위해서, 코드 블락은 현재 스레드에서 or 다른 스레드에서 continuation.resumeWith() 을 실행시켜야한다.
     *
     * suspend 함수가 중단되고 재개된다는 의미가 무엇이지?
     *
     * The actual suspension of a coroutine happens when the suspendCoroutine block returns without invoking resumeWith.
     * suspendCoroutine block 이 resumeWith 의 실행 없이 리턴될 때, coroutine 의 실제 중단이 일어난다.
     *
     * If continuation was resumed before returning from inside of the block, then the coroutine is not considered to have been suspended and continues to execute.
     */

    suspendCoroutine<T> { cont: Continuation<T> ->
        whenComplete { result, exception ->
            if (exception == null) // the future has been completed normally
                // 코루틴의 결과값을 마지막 중단 포인트로 넘기면서 해당 코루틴의 실행을 재개한다.
                cont.resume(result)
            else // the future has completed with an exception
                cont.resumeWithException(exception)
        }
    }
