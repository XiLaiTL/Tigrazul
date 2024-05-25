package `fun`.vari.tigrazul.model

class Applied(
    argument: Atom,
    function: Atom
):BinaryAtom(argument,function) {
    override val type: Atom
        get() {
            //if(right.type is MapsToFunction) //在所有情况中都规定前一项是完整项
                return Applied(left,right.type)
        }

    override fun debugInfo() = "[${left.debugInfo()} ▷ ${right.debugInfo()}]"
    override fun info() = "(${left.info()} ▷ ${right.info()})"
    override fun plainWithType() = "(${left.plain()} ▷ ${right.plain()})"
    override fun plain() = "(${left.plain()} ▷ ${right.plain()})"
    override fun uniform() = "(${left.uniform()}|>${right.uniform()})"
}