package `fun`.vari.tigrazul.struct

/**
 * 侵入式链表的结点
 * @param <Elements> 元素的类型
 * @param <Container> 含有侵入式链表的class的类型
</Container></Elements> */
/*
采用prev、next两个IListNode来链接其他结点。

因此可以不用通过IList即可达成添加子元素的作用。可以通过getParentList()返回侵入式链表，通过getData()返回存储的数据对象。
 */
class IListNode<Elements, Container>
    (var data: Elements?) {
    var prev: IListNode<Elements, Container>? = null
    fun hasPrev(): Boolean = prev != null

    var next: IListNode<Elements, Container>? = null
    fun hasNext(): Boolean = next != null

    var parentList: IList<Elements, Container>? = null
    fun hasParentList(): Boolean = parentList != null

    val container: Container?
        get() = if (hasParentList()) parentList!!.container
        else null

    /** this(list)->node  */
    fun append(node: IListNode<Elements, Container>) {
        node.parentList = this.parentList
        node.prev = this
        node.next = this.next
        if (hasNext()) next!!.prev = node
        if (hasParentList()) parentList!!.modifySize(1)
        this.next = node
    }

    /** node->this(list)  */
    fun prepend(node: IListNode<Elements, Container>) {
        node.parentList = this.parentList
        node.prev = this.prev
        node.next = this
        if (hasPrev()) prev!!.next = node
        if (hasParentList()) parentList!!.modifySize(1)
        this.prev = node
    }

    /** node(list)->this */
    fun insertAfter(node: IListNode<Elements, Container>) {
        node.append(this)
    }

    /** this->node(list) */
    fun insertBefore(node: IListNode<Elements, Container>) {
        node.prepend(this)
    }

    fun remove() {
        if (hasParentList()) {
            parentList!!.modifySize(-1)
            this.parentList = null
            if (hasPrev()) {
                prev!!.next = this.next
            }
            if (hasNext()) {
                next!!.prev = this.prev
            }
        }
    }
}
