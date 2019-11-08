package channel.example9

import channel.go
import channel.mainBlocking
import delay.delay
import mutex.Mutex

// https://tour.golang.org/concurrency/9

/**
 * 스케일러블한 비동기 프로그래밍을 위한 법칙은 코드나 스레드를 절대 블락하지 말고 잠시 중단을 멈추라는 것이다.
 * coroutine 끼리 공유되는 리소스에 대해 리소스를 얻을 때 lock 을 걸어 함수가 기다리게 한다.
 * Mutex를 활용해서 ThreadSafe하게 구현된 Counter
 */
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
            // 1000 개의 코루틴은 어떤게 먼저 생성되어 공유되는 리소스에 접근하게 될지는 모른다.
            // 하지만 리소스에 순차적으로 한번헤 하나씩만 접근은 보장된다.
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
