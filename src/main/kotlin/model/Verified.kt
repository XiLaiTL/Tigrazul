package `fun`.vari.tigrazul.model

import `fun`.vari.tigrazul.action.DeBruijnLevel.toDeBruijnLevelUniform
import `fun`.vari.tigrazul.action.copy

class Verified( value:Atom,verifiedType: Atom): BinaryAtom(value,verifiedType) {
    override val type:Atom
        get() = right.copy() //这里只提供view
    override fun debugInfo()="[${left.debugInfo()} : ${type.info()}]"
    override fun info() = "[${left.info()} : ${type.info()}]"
    override fun plainWithType() = left.plain()
    override fun plain() = left.plain()
    override fun uniform() = this.toDeBruijnLevelUniform() //left.uniform()
}