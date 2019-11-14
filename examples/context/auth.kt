package context

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class AuthUser(val name: String) : AbstractCoroutineContextElement(AuthUser) {
    // AbstractCoroutineContextElement 에 AuthUser만으로 충분한 이유:
    // The name of a class used by itself (not as a qualifier to another name) acts as a reference to the companion object of the class (whether named or not):
    // https://kotlinlang.org/docs/reference/object-declarations.html#companion-objects
    companion object Key : CoroutineContext.Key<AuthUser>
}

/**
 * CoroutineContext
 * Coroutine이 실행되는 문맥 (Kotlin standard libarary에 정의된 CouroutineContext 타입의 값들로 표현됨)
 * JOB과 Dispather들로 구성됨
 * https://kotlinlang.org/docs/reference/coroutines/coroutine-context-and-dispatchers.html
 *
 * JOB: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-job/index.html
 *
 */
