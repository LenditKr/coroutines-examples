package channel

import delay.delay
import java.time.Instant

object Time {

    // Instant (long: 초, int: 나노초) 로 구성된 시간의 한 순간
    // 일정 시간마다 시간을 보낸다.
    fun tick(millis: Long): ReceiveChannel<Instant> {
        val c = Channel<Instant>()
        go {
            // 일정 시간마다 항상 시간을 보내는 채널
            while (true) {
                delay(millis)
                c.send(Instant.now())
            }
        }
        return c
    }

    // 1번만 시간을 보내고 채널을 닫는다.
    fun after(millis: Long): ReceiveChannel<Instant> {
        val c = Channel<Instant>()
        go {
            delay(millis)
            c.send(Instant.now())
            c.close()
        }
        return c
    }
}
