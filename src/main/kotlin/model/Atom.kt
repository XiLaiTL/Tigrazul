package `fun`.vari.tigrazul.model

interface Node<T:Node<T>>{
    val first:T;
    var next:T?;
}

object Unknown: Atom() {
    override val type: Atom = Unknown
    override val first: Atom = this
    override var next: Atom? = null
    override fun debugInfo() = "Unknown"
    override fun info() = "Unknown"
}

//这里是作为值的时候的名称
object Kind: Atom() {
    override val type: Atom = Kind // 此处循环
    override val first: Atom = this
    override var next: Atom? = null
    override fun debugInfo() = "Kind"
    override fun info() = "Kind"
}

object Type: Atom() {
    override val type: Atom = Kind
    override val first: Atom = this
    override var next: Atom? = null
    override fun debugInfo() = "Type"
    override fun info() = "Type"
}

sealed class Atom:Node<Atom>{
    abstract val type:Atom
    abstract fun debugInfo():String
    abstract fun info():String
}