package context

import future.*
import util.*

fun main(args: Array<String>) {
    val future = future(Swing) {
        log("Let's Swing.delay for 1 second")
        Swing.delay(10000)
        log("We're still in Swing EDT")
    }

    // future.await() 과 같은 기능이지만 suspendable한 함수가 아니라 안됨
    future.join()
}

