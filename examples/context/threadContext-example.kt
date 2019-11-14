package context

import delay.delay
import future.await
import future.future
import util.log

fun main(args: Array<String>) {
    log("Starting MyEventThread")
    val context = newSingleThreadContext("MyEventThread")
    val f = future(context) {
        log("Hello, world!")
        val f1 = future(context) {
            log("f1 is sleeping")
            delay(1000) // sleep 1s
            log("f1 returns 1")
            1
        }
        val f2 = future(context) {
            log("f2 is sleeping")
            delay(1000) // sleep 1s
            log("f2 returns 2")
            2
        }
        log("I'll wait for both f1 and f2. It should take just a second!")
        val sum = f1.await() + f2.await()
        log("And the sum is $sum")
    }
    log("before get")
    f.get()
    log("after get")
    log("Terminated")

    /**
    Dispatchers가 import되어 있지 않음
    launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    */
}
