package `fun`.vari.tigrazul.model

class Applied(
    var argument: Atom,
    var function: Atom
):Atom() {
    override val type: Atom
        get() = Applied(argument.type,function.type)
    override val first: Atom
        get() = argument
    override var next: Atom?
        get() = function
        set(value) { if(value!=null) function = value; }
    override fun debugInfo() = "[${argument.debugInfo()} ▷ ${function.debugInfo()}]"
    override fun info() = "(${argument.info()} ▷ ${function.info()})"
}