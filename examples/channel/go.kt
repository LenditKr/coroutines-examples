package channel

import context.CommonPool
import run.runBlocking

// Java Stream의 parallelStream과 같이 ForkJoinPool.commonPool을 활용
// Blocking하게 block을 CommonPool에서 실행시킨다.
fun mainBlocking(block: suspend () -> Unit) = runBlocking(CommonPool, block)

// block을 CommonPool에서 parallel하게 실행시킨다.
fun go(block: suspend () -> Unit) = CommonPool.runParallel(block)

