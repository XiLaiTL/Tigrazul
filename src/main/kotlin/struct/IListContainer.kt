package `fun`.vari.tigrazul.struct

/**
 * 本身作为容器的类
 * @param <Elements> 元素的类型
 * @param <Container> 含有侵入式链表的class的类型
</Container></Elements> */
/*
实现了该接口的目的是：本身可以当作侵入式链表，并把自身作为getContainer()的返回对象。该接口是Iterable\<Element>的实现，和IList不同。
 */
interface IListContainer<Elements : IListNodeContainer<Elements, Container>, Container : IListContainer<Elements, Container>>
    : Iterable<Elements> {
    fun asList(): IList<Elements, Container>

    fun addFirst(e: Elements) = asList().addFirstNode(e.asNode())

    fun addLast(e: Elements) = asList().addLastNode(e.asNode())

    fun removeFirst() = asList().removeFirstNode()

    fun removeLast() = asList().removeLastNode()

    val first: Elements?
        get() = if (!asList().isEmpty()) asList().firstNode!!.data else null
    val last: Elements?
        get() = if (!asList().isEmpty()) asList().lastNode!!.data else null

    operator fun get(index: Int): Elements? = asList()[index].data

    fun contains(elements: Elements): Boolean = elements.container === this

    fun isEmpty(): Boolean = asList().isEmpty()

    fun toList(): List<Elements> = asList().toList()

    fun toReadOnlyList(): List<Elements> = asList().toReadOnlyList()

    override fun iterator(): MutableIterator<Elements> {
        val iter: MutableIterator<IListNode<Elements, Container>> = asList().iterator()
        return object : MutableIterator<Elements> {
            override fun hasNext(): Boolean = iter.hasNext()
            override fun next(): Elements = iter.next().data!!
            override fun remove() = iter.remove()
        }
    }
}


