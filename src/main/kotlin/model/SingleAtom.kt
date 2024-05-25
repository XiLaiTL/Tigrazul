package `fun`.vari.tigrazul.model

sealed class SingleAtom:Atom() {
    override val current: Atom = this
    override val next: Atom? = null
}

object Unknown: SingleAtom() {
    override val type: Atom = Unknown
    override fun debugInfo() = "Unknown"
    override fun info() = "Unknown"
    override fun plainWithType() ="Unknown"
    override fun plain() = "Unknown"
    override fun uniform() = "Unknown"

}

//这里是作为值的时候的名称
object Kind: SingleAtom() {
    override val type: Atom = Kind // 此处循环
    override fun debugInfo() = "Kind"
    override fun info() = "Kind"
    override fun plainWithType() = "Kind"
    override fun plain() = "Kind"
    override fun uniform() = "Kind"
}

object Type: SingleAtom() {
    override val type: Atom = Kind
    override fun debugInfo() = "Type"
    override fun info() = "Type"
    override fun plainWithType() = "Type"
    override fun plain() = "Type"
    override fun uniform() = "Type"
}

