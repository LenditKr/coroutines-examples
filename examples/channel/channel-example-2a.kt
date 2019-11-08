package channel.example2a

import channel.*
import kotlin.system.*

suspend fun sum(s: List<Int>, c: SendChannel<Int>) {
    // simulate long-running CPU-consuming computation
    var sum = 0
    val time = measureTimeMillis {
        repeat(100_000_000) {
            for (v in s) {
                sum += v
            }
        }
        c.send(sum)
    }
    println("Sum took $time ms")
}

fun main(args: Array<String>) = mainBlocking {
    val s = listOf(7, 2, 8, -9, 4, 0)
    val c = Channel<Int>()
    go { sum(s.subList(s.size /2, s.size), c) }
    go { sum(s.subList(0, s.size / 2), c) }
    // receive 할때까지 대기(sum에서 로직 수행 후 채널 c에 send 하여 값을 받을 때까지)
    val time = measureTimeMillis {
        val x = c.receive()
        val y = c.receive()
        println("$x $y ${x + y}")
    }
    println("Main code took $time ms")
}