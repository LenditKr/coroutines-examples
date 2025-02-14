package channel.example5

import channel.Channel
import channel.ReceiveChannel
import channel.SendChannel
import channel.go
import channel.mainBlocking
import channel.whileSelect

// https://tour.golang.org/concurrency/5

suspend fun fibonacci(c: SendChannel<Int>, quit: ReceiveChannel<Int>) {
    var x = 0
    var y = 1

    // when 문과 비슷하다.
    // 이 피보나치 함수는 두개의 채널 송신, 수신을 갖고 있고
    // 이 예제에서는 수신이 일어나거나 송신 중 하나의 역할을 한다.
    whileSelect {
        c.onSend(x) {
            val next = x + y
            x = y
            y = next
            true // continue while loop
        }
        quit.onReceive {
            println("quit")
            false // break while loop
        }
    }
}

fun main(args: Array<String>) = mainBlocking {
    val c = Channel<Int>(2)
    val quit = Channel<Int>(2)

    // 실행되고 c.receive() 대기 총 10번 대기중
    go {
        for (i in 0..9)
            println(c.receive())
        quit.send(0)
    }

    // fibonnacci가 계산되면서 channel에 전달
    fibonacci(c, quit)
}
