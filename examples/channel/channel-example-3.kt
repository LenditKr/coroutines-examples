package channel.example3

import channel.*

// https://tour.golang.org/concurrency/3
/**
 * channel 은 블로킹 큐와 비슷하다.
 * 처음 캐피시티를 설정하고 나면 캐피시티가 다 차기전까지는 전송이 중단된다.
 *
 * capacity 에 따라 channel의 동작방식이 조금씩 달라진다.
 *
 * capacity == 0 일 때 RendezvousChannel 이 생성
 * 보내는 쪽과 받는 쪽이 있을 때 즉시 송신과 수신이 이루워지며
 * 송신과 수신은 페어가 맞을 때까지 중지된다.
 *
 * Channel.UNLIMITED 일 때 LinkedListChannel 이 생성
 * 메모리가 허용하는한 무한한 size 를 갖는 linked list 의 형태로 사용할 수 있다.
 *
 * Channel.CONFLATED 일 때 ConflatedChannel 이 생성
 * 수신쪽은 송신에서 가장 최근에 보낸 데이터만 받게 되고
 * 수신쪽이 받기전에 송신에서 보낸 데이터들은 버려진다.
 *
 * capacity > 0 && capacity < UNLIMITED array-based channel 이 생성
 * 버퍼가 다 차면 송신쪽의 송신은 멈춤
 */
fun main(args: Array<String>) = mainBlocking {
    val c = Channel<Int>(2)
    c.send(1)
    c.send(2)
    println(c.receive())
    println(c.receive())

    // --
    c.send(1)
    c.send(2)

    // 1,2 가 있어 이미 full 이기 때문에 채널안에 있는 Waiter 에 저장된다. 대충 봤을 때 linked list 같은 인터페이스를 갖고 있다.
    c.send(3)

    println(c.receive())
    println(c.receive())
    println("아직 3을 받지 않았다.")
    c.send(4)
    println(c.receive())
    println(c.receive())
}