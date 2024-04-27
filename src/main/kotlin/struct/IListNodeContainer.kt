package `fun`.vari.tigrazul.struct

/**
 * 本身作为结点的类
 * @param <Elements> 元素的类型
 * @param <Container> 含有侵入式链表的class的类型
</Container></Elements> */
/*
实现了该接口的目的是：本身可以当作结点，并把自身作为getData()的返回对象。
 */
interface IListNodeContainer<Elements : IListNodeContainer<Elements, Container>, Container : IListContainer<Elements, Container>> {
    fun asNode(): IListNode<Elements, Container>

    fun prepend(elements: Elements) = asNode().prepend(elements.asNode())

    fun append(elements: Elements) = asNode().append(elements.asNode())

    fun hasNext(): Boolean = asNode().hasNext()

    fun hasPrev(): Boolean = asNode().hasPrev()

    val next: Elements?
        get() = asNode().next?.data

    val prev: Elements?
        get() = asNode().prev?.data

    val container: Container?
        get() = asNode().container

    var parentList: IList<Elements, Container>?
        get() = asNode().parentList
        set(parentList) {
            asNode().parentList=parentList
        }

    fun removeFromParentList() = asNode().remove()
}
