package channel.example6

import channel.Time
import channel.mainBlocking
import channel.whileSelect
import delay.delay

// https://tour.golang.org/concurrency/6

fun main(args: Array<String>) = mainBlocking {
    val tick = Time.tick(100)
    val boom = Time.after(500)
    whileSelect {
        // tick은 지속적으로 시간을 보내므로 100ms마다 receive
        tick.onReceive {
            println("tick.")
            true // continue loop
        }
        // 500ms후에 while은 끝난다.
        boom.onReceive {
            println("BOOM!")
            false // break loop
        }
        // 50ms마다 default 확인
        onDefault {
            println("    .")
            delay(50)
            true // continue loop
        }
    }
}
