package `fun`.vari.tigrazul.model

import `fun`.vari.tigrazul.action.copy

class Verified(value:Atom,private val verifiedType: Atom): UnaryAtom(value) {
    override val type:Atom
        get() = verifiedType.copy() //这里只提供view
    override fun debugInfo()="[${value.debugInfo()} : ${type.info()}]"
    override fun info() = "[${value.info()} : ${type.info()}]"
    override fun uniform() = "(${type.uniform()})"
}