package context

import kotlin.coroutines.*

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
    companion object Key : CoroutineContext.Key<AuthUser>
}
