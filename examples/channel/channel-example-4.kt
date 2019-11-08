package channel.example4

import channel.*
import delay.delay

// https://tour.golang.org/concurrency/4

suspend fun fibonacci(n: Int, c: SendChannel<Int>) {
    var x = 0
    var y = 1
    for (i in 0..n - 1) {
        c.send(x)
        println("send #$i: $x")
        val next = x + y
        x = y
        y = next
    }
    c.close()
}

/**
 * capaity 는 피보나치 알고리즘에 영향은 전혀 없음
 */
fun main(args: Array<String>) = mainBlocking {
    val c = Channel<Int>(2)
    go { fibonacci(10, c) }

    var cnt = 0
    // channel 에 수신되서 iterable 할 값이 있을 때 마다 for 문 안에가 실행된다.
    // capaicty 값을 조정해가며 실행해보자.
    for (i in c) {
        delay(3000)
        println("receive #${cnt++}: $i")
    }
}
