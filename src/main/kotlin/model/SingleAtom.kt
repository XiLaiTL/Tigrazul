package `fun`.vari.tigrazul.model

sealed class SingleAtom:Atom() {
    override val current: Atom = this
    override val next: Atom? = null
}

object Unknown: SingleAtom() {
    override val type: Atom = Unknown
    override fun debugInfo() = "Unknown"
    override fun info() = "Unknown"
}

//这里是作为值的时候的名称
object Kind: SingleAtom() {
    override val type: Atom = Kind // 此处循环
    override fun debugInfo() = "Kind"
    override fun info() = "Kind"
}

object Type: SingleAtom() {
    override val type: Atom = Kind
    override fun debugInfo() = "Type"
    override fun info() = "Type"
}

