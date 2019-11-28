package context

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * coroutine 컨텍스트는 코루틴에 유저가 정한 오브젝트를 저장할 수 있는 set
 * Thread Local 과 유사함. 차이점은 Thread local은 mutable 하지만 코루틴 컨텍스트의 밸류는 immutable. 이유는 코루틴은 가볍기 때문에 쉽게 새로운 컨텍스트를 생성할 수 있기 때문.
 *
 * 코루틴 컨텍스트는 유니크 키를 가 Element 가 색인된 set.
 *
 * Element 는 그 자체로 singleton 코루틴 컨텍스트. 이 특징은 plus / + 를 통해 컨테스트간의 join을 쉽게 해줌.
 *
 * All third-party context elements shall extend AbstractCoroutineContextElement class that is provided by the standard library (in kotlin.coroutines package).
 * The following style is recommended for library defined context elements.
 * The example below shows a hypothetical authorization context element that stores current user name:
 *
 * context key는 해당 context element 클래스에 유연한 접근을 가능하게 함
 */
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
