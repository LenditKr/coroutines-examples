package context

import run.runBlocking
import kotlin.coroutines.coroutineContext

suspend fun doSomething() {
    // 이 함수가 실행되는 current CoroutineContext중에 AuthUser Context에서 이름이 있는지 확인한다.
    // 어떤 suspending 함수에서나 현재 coroutine context 에 접근할 수 있다.
    val currentUser = coroutineContext[AuthUser]?.name ?: throw SecurityException("unauthorized")
    println("Current user is $currentUser")
}

fun main(args: Array<String>) = runBlocking(CommonPool) {
    launch {
        val user1 = AuthUser("admin1")
        val user2 = AuthUser("admin2")
        val user3 = AuthUser("admin3")
        // AuthUser라는 CoroutineContext내에서 Blocing하게 실행한다.
        launch(user1) {
            println(user1.key)
            doSomething()

            launch(user2) {
                log("User1 Context 상속")
                doSomething()
            }
        }

        runBlocking(user2) {
            println(user2.key)
            doSomething()
        }
    }
}
