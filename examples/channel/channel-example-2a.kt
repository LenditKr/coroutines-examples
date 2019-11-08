package channel.example2a

import channel.Channel
import channel.SendChannel
import channel.go
import channel.mainBlocking
import kotlin.system.measureTimeMillis

suspend fun sum(s: List<Int>, c: SendChannel<Int>) {
    // simulate long-running CPU-consuming computation
    // s에 있는 int list를 1억번 반복해서 더함
    printThreadName("sum")
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
    printThreadName("mainBlocking")
    //
    go {
        printThreadName("first go")
        sum(s.subList(s.size /2, s.size), c)
    }
    go {
        printThreadName("second go")
        sum(s.subList(0, s.size / 2), c)
    }

    val time = measureTimeMillis {
        val x = c.receive()
        val y = c.receive()
        println("$x $y ${x + y}")
    }

    println("Main code took $time ms")
}

fun printThreadName(label: String) {
    // ForkJoinPool.commonPool-worker를 확인가능
    println("${label} thread Id: ${Thread.currentThread().getId()}, thread name: ${Thread.currentThread().name}")
}