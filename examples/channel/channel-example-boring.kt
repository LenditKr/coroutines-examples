package channel.boring

import channel.*
import delay.*
import java.util.*

// https://talks.golang.org/2012/concurrency.slide#25

// 계속 랜덤한 인터벌을 두고 string을 송신하는 채널을 리턴한다.
suspend fun boring(msg: String): ReceiveChannel<String> { // returns receive-only channel of strings
    val c = Channel<String>()
    val rnd = Random()
    go {
        var i = 0
        while (true) {
            c.send("$msg $i")
            delay(rnd.nextInt(1000).toLong())
            i++
        }
    }
    return c // return the channel to the caller
}

// https://talks.golang.org/2012/concurrency.slide#26

fun main(args: Array<String>) = mainBlocking {
    val joe = boring("Joe")
    val ann = boring("Ann")
    for (i in 0..4) {
        println(joe.receive())
        println(ann.receive())
    }
    println("Your're both boring; I'm leaving.")
}
