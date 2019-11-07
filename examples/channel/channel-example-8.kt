package channel.example8

import channel.Channel
import channel.SendChannel
import channel.go
import channel.mainBlocking
import java.util.Collections

// https://tour.golang.org/concurrency/7
// https://tour.golang.org/concurrency/8

val treeSize = 10

// preorder traversal
suspend fun Tree.walk(ch: SendChannel<Int>): Unit {
    left?.walk(ch)
    ch.send(value)
    right?.walk(ch)
}

suspend fun same(t1: Tree, t2: Tree): Boolean {
    val c1 = Channel<Int>()
    val c2 = Channel<Int>()
    // 2개의 Tree를 preorder 순회한 결과를 Channel에 기록해서
    go { t1.walk(c1) }
    go { t2.walk(c2) }
    var same = true
    // 그 2개의 채널이 같은지 비교한다.
    for (i in 1..treeSize) {
        val v1 = c1.receive()
        val v2 = c2.receive()
        if (v1 != v2) same = false
    }
    return same
}

fun main(args: Array<String>) = mainBlocking {
    val t1 = newTree(1)
    val t2 = newTree(1)
    val t3 = newTree(2)
    println("t1 = $t1")
    println("t2 = $t2")
    println("t3 = $t3")
    println("t1 same as t2? ${same(t1, t2)}")
    println("t1 same as t3? ${same(t1, t3)}")
}

// https://github.com/golang/tour/blob/master/tree/tree.go
// https://en.wikipedia.org/wiki/Tree_(data_structure)
data class Tree(val value: Int, val left: Tree? = null, val right: Tree? = null)

fun Tree?.insert(v: Int): Tree {
    if (this == null) return Tree(v)
    if (v < value)
        return Tree(value, left.insert(v), right)
    else
        return Tree(value, left, right.insert(v))
}

fun newTree(k: Int): Tree {
    var t: Tree? = null
    val list = (1..treeSize).toMutableList()
    Collections.shuffle(list)
    for (v in list) {
        t = t.insert(v * k)
    }
    return t!!
}
