package channel.example9

import channel.go
import channel.mainBlocking
import delay.delay
import mutex.Mutex

// https://tour.golang.org/concurrency/9

// Mutex를 활용해서 ThreadSafe하게 구현된 Counter
class SafeCounter {
    private val v = mutableMapOf<String, Int>()
    private val mux = Mutex()

    suspend fun inc(key: String) {
        mux.lock()
        try { v[key] = v.getOrDefault(key, 0) + 1 }
        finally { mux.unlock() }
    }

    suspend fun get(key: String): Int? {
        mux.lock()
        return try { v[key] }
        finally { mux.unlock() }
    }
}

fun main(args: Array<String>) = mainBlocking {
    val c = SafeCounter()
    for (i in 0..999) {
        go {
            printThreadName("go$i") // commonPool 최대 갯수만큼만 thread를 활용
            c.inc("somekey")
        } // 1000 concurrent coroutines
    }
    delay(1000)
    println("${c.get("somekey")}")
}

fun printThreadName(label: String) {
    // ForkJoinPool.commonPool-worker를 확인가능
    println("${label} thread Id: ${Thread.currentThread().getId()}, thread name: ${Thread.currentThread().name}")
}
