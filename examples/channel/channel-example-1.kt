package channel.example1

import channel.go
import channel.mainBlocking
import delay.delay
import java.time.LocalDateTime

// https://tour.golang.org/concurrency/1

// suspend: https://bit.ly/34AB8EK
suspend fun say(s: String) {
    println(LocalDateTime.now())
    for (i in 0..4) {
        delay(100)
        println(s)
    }
}

// CommonPool에 CodeBlock을 실행시킨다 -> MainBlocking
fun main(args: Array<String>) = mainBlocking {

    // CommonPool에서 parellel하게 실행한다 - async하게 동작하게 함
    // suspend function go()
    go { say("world") }

    // suspend function say()
    say("hello")
}

