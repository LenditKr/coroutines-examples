package context

import future.*
import run.runBlocking
import util.*

fun main(args: Array<String>) = runBlocking(CommonPool) {
    val future = future(Swing) {
        log("Let's Swing.delay for 1 second")
        Swing.delay(10000)
        log("We're still in Swing EDT")
    }

    // coroutine 안이기 때문에 await 이 가능하다.
    future.await()
}
