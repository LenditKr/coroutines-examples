package context

import future.await
import future.future
import run.runBlocking
import util.log

fun main(args: Array<String>) = runBlocking(CommonPool) {
    // multithreaded pool
    val n = 16
    val compute = newFixedThreadPoolContext(n, "Compute")
    // start 4 coroutines to do some heavy computation
    val subs = Array(n) { i ->

        //compute라는 ThreadPool(CoroutineContext)에서 n개의 작업을 동시에 실행한다.
        future(compute) {
            log("Starting computation #$i")
            Thread.sleep(1000) // simulate long running operation
            log("Done computation #$i")
        }
    }
    // await all of them
    subs.forEach { it.await() }
    log("Done all")
}