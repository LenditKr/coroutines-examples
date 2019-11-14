package context

import run.runBlocking
import kotlin.coroutines.coroutineContext

suspend fun doSomething() {
    // 이 함수가 실행되는 current CoroutineContext중에 AuthUser Context에서 이름이 있는지 확인한다.
    val currentUser = coroutineContext[AuthUser]?.name ?: throw SecurityException("unauthorized")
    println("Current user is $currentUser")
}

fun main(args: Array<String>) {
    // AuthUser라는 CoroutineContext내에서 Blocing하게 실행한다.
    runBlocking(AuthUser("admin")) {
        doSomething()
    }

    runBlocking(AuthUser("check")) {
        doSomething()
    }
}
