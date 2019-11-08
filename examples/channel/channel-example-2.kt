package channel.example2

import channel.*

suspend fun sum(s: List<Int>, c: SendChannel<Int>) {
    var sum = 0
    for (v in s) {
        sum += v
    }
    // 결과값을 c로 보낸다
    c.send(sum)
}

fun main(args: Array<String>) = mainBlocking {
    val s = listOf(7, 2, 8, -9, 4, 0)
    val c = Channel<Int>()
    // listOf(7, 2, 8), Channel c
    go { sum(s.subList(s.size / 2, s.size), c) }
    // listOf(-9, 4, 0) Channel c
    go { sum(s.subList(0, s.size / 2), c) }
    // 채널에 전송된 값 받기
    val x = c.receive()
    val y = c.receive()
    println("$x $y ${x + y}")
}