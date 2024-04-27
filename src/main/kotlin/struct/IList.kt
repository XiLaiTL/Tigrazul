package `fun`.vari.tigrazul.struct

import java.util.*

/**
 * 侵入式链表本身，是一个双向链表
 * 采用侵入式链表结构的思路，让结点和链表本身的控制权分离
 * @param <Elements> 元素的类型
 * @param <Container> 含有侵入式链表的class的类型
 *
 * @see [
 * Linux Intrusive List Implementation](https://github.com/torvalds/linux/blob/v5.18/include/linux/list.h)
</Container></Elements> */
/*
侵入式链表本身，采用first、last两个IListNode来索引并组合链表。可以通过getContainer()返回包含侵入式链表的对象。IList是List的一个List\<IListNode>实现，因此可以被视为IListNode的一个容器。
 */
class IList<Elements, Container>
    (val container: Container) : AbstractSequentialList<IListNode<Elements, Container>>(),
    MutableList<IListNode<Elements, Container>> {
    override var size: Int = 0

    fun modifySize(num: Int) {
        size += num
    }

    //Node本身的append和prepend是操作链的
    private val first = IListNode<Elements, Container>(null)
    private val last = IListNode<Elements, Container>(null)

    init {
        first.parentList = this
        last.parentList = this
        first.next = this.last
        last.prev = this.first
    }

    val firstNode: IListNode<Elements, Container>?
        get() = if (!isEmpty()) first.next else null

    val lastNode: IListNode<Elements, Container>?
        get() = if (!isEmpty()) last.prev else null

    fun addFirstNode(e: IListNode<Elements, Container>) = first.append(e)

    fun addLastNode(e: IListNode<Elements, Container>) = last.prepend(e)

    fun removeFirstNode() {
        if (!isEmpty()) first.next?.remove()
    }

    fun removeLastNode() {
        if (!isEmpty()) last.prev?.remove()
    }

    private fun checkElementIndex(index: Int) {
        if (index !in 0..<size) throw IndexOutOfBoundsException("Index: $index, Size: $size")
    }

    override fun get(index: Int): IListNode<Elements, Container> {
        checkElementIndex(index)
        return node(index)
    }

    fun node(index: Int): IListNode<Elements, Container> {
        if (index < (size shr 1)) {
            var x = first
            for (i in 0..index) x = x.next!!
            return x
        } else {
            var x = last
            for (i in size - 1 downTo index) x = x.prev!!
            return x
        }
    }

    fun toList(): List<Elements> = this.map { it.data!! }

    fun toReadOnlyList(): List<Elements> = Collections.unmodifiableList(toList())

    override fun listIterator(index: Int): MutableListIterator<IListNode<Elements, Container>> =
        IListNodeListIterator(index)

    private inner class IListNodeListIterator(private var nextIndex: Int) :
        MutableListIterator<IListNode<Elements, Container>> {
        private var lastReturned: IListNode<Elements, Container>? = null
        private var next: IListNode<Elements, Container>?

        init {
            next = if ((nextIndex == size)) null else node(nextIndex)
        }

        override fun hasNext(): Boolean = nextIndex < size

        override fun next(): IListNode<Elements, Container> {
            if (!hasNext()) throw NoSuchElementException()
            lastReturned = next
            next = next!!.next
            nextIndex++
            return lastReturned!!
        }

        override fun hasPrevious(): Boolean = nextIndex > 0

        override fun previous(): IListNode<Elements, Container> {
            if (!hasPrevious()) throw NoSuchElementException()
            next = if ((next == null)) last else next!!.prev
            lastReturned = next
            nextIndex--
            return lastReturned!!
        }

        override fun nextIndex(): Int = nextIndex

        override fun previousIndex(): Int = nextIndex - 1

        override fun remove() {
            checkNotNull(lastReturned)
            val lastNext = lastReturned!!.next
            lastReturned!!.remove()
            if (next === lastReturned) next = lastNext
            else nextIndex--
            lastReturned = null
        }

        override fun set(element: IListNode<Elements, Container>) {
            checkNotNull(lastReturned)
            element.parentList=this@IList
            element.prev = lastReturned!!.prev
            element.next = lastReturned!!.next
            if (lastReturned!!.hasPrev()) lastReturned!!.prev!!.next = element
            if (lastReturned!!.hasNext()) lastReturned!!.next!!.prev = element
            lastReturned = element
        }

        override fun add(element: IListNode<Elements, Container>) {
            lastReturned = null
            Objects.requireNonNullElse(next, this@IList.last)!!.prepend(element)
            nextIndex++
        }
    }
}
