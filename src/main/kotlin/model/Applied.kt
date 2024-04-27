package `fun`.vari.tigrazul.model

class Applied(
    argument: Atom,
    function: Atom
):BinaryAtom(argument,function) {
    override val type: Atom
        get() = Applied(left.type,right.type)

    override fun debugInfo() = "[${left.debugInfo()} ▷ ${right.debugInfo()}]"
    override fun info() = "(${left.info()} ▷ ${right.info()})"
    fun reduce(){


    }
}