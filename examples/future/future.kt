package future

import context.CommonPool
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.startCoroutine

// JAVA의 CompleteableFuture를 Coroutine으로 활용해보자
fun <T> future(context: CoroutineContext = CommonPool, block: suspend () -> T): CompletableFuture<T> =
        CompletableFutureCoroutine<T>(context).also { block.startCoroutine(completion = it) }

// CompletableFuture 을 상속해서 java 에서도 사용할 수 있다.
class CompletableFutureCoroutine<T>(override val context: CoroutineContext) : CompletableFuture<T>(), Continuation<T> {
    override fun resumeWith(result: Result<T>) {
        result
            .onSuccess { complete(it) }
            .onFailure { completeExceptionally(it) }
    }
}
