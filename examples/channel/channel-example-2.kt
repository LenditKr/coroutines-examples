package channel.example2

import channel.Channel
import channel.SendChannel
import channel.go
import channel.mainBlocking

suspend fun sum(s: List<Int>, c: SendChannel<Int>) {
    var sum = 0
    for (v in s) {
        sum += v
    }
    c.send(sum)
}

// Main Blocking은 CommonPool을 활옹한다.
fun main(args: Array<String>) = mainBlocking {
    val s = listOf(7, 2, 8, -9, 4, 0)
    val c = Channel<Int>()
    // async 뒤에 절반 합산해서 채널 c로 보내고
    go { sum(s.subList(s.size / 2, s.size), c) }
    // async 하게 앞에 절반 합산해서 채널 c로 보낸다.
    go { sum(s.subList(0, s.size / 2), c) }

    // 받아서
    val x = c.receive()
    val y = c.receive()

    // 찍어주기
    println("$x $y ${x + y}")
}