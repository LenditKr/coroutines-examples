package channel.example1

import channel.*
import delay.*

// https://tour.golang.org/concurrency/1
/**
 * Suspend 키워드는 함수를 suspend 하게 실행시킬 수 있게 하고
 * corountine context 안에서만 실행할 수 있다.
 */
suspend fun say(s: String) {
    for (i in 0..4) {
        delay(100)
        println(s)
    }
}

/**
 *
 */
fun main(args: Array<String>) = mainBlocking {
    go { say("world") }
    say("hello")
}

