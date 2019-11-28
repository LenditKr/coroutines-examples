package context

import run.launch
import util.log
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun makeRequest(): String {
    log("Making request...")

    // 참고1: https://www.baeldung.com/kotlin-inline-functions
    // 참고2: https://medium.com/@joongwon/kotlin-kotlin-%ED%82%A4%EC%9B%8C%EB%93%9C-%EB%B0%8F-%EC%97%B0%EC%82%B0%EC%9E%90-%ED%95%B4%EB%B6%80-part-3-59ff3ed736be
    // inline: That is, it substitutes the body directly into places where the function gets called.
    // noinline: exclude inlining
    // crossinline: 다른 실행 context를 통해서 호출될때 비-로컬 흐름을 제어하고 싶을 경우
    // suspendCoroutine: Obtains the current continuation instance inside suspend functions and suspends the currently running coroutine.
    return suspendCoroutine { c ->
        ForkJoinPool.commonPool().execute {
            c.resume("Result of the request")
        }
    }
}

fun display(result: String) {
    log("Displaying result '$result'")
}

fun main(args: Array<String>) {
    launch(Swing) {
        try {
            // suspend while asynchronously making request
            val result = makeRequest()
            // display result in UI, here Swing context ensures that we always stay in event dispatch thread
            display(result)
        } catch (exception: Throwable) {
            // process exception
        }
    }
}
