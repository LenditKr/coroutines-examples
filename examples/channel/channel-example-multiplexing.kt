package channel.multiplexing

import channel.*
import channel.boring.*

// https://talks.golang.org/2012/concurrency.slide#27

suspend fun fanIn(input1: ReceiveChannel<String>, input2: ReceiveChannel<String>): ReceiveChannel<String> {
    val c = Channel<String>()
    go {
        for (v in input1){
            c.send(v)
        }
    }
    go {
        for (v in input2)
            c.send(v)
    }
    return c // return combo channel
}

fun main(args: Array<String>) = mainBlocking {
    // boring("Joe"), boring("Ann")에서 결과값을 send한 ReceiveChannel를 각각 생성 - 수행시간은 가변적임
    // fanIn에서 새로운 ReceiveChannel을 리턴(해당 채널에 변수로 받은 두개의 ReceiveChannel들의 값을 async하게 send)
    val c = fanIn(boring("Joe"), boring("Ann"))
    for (i in 0..9) {
        println(c.receive())
    }
    println("Your're both boring; I'm leaving.")
}
