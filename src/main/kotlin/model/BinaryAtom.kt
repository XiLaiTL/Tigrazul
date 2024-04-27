package `fun`.vari.tigrazul.model

import `fun`.vari.tigrazul.struct.IList
import `fun`.vari.tigrazul.struct.IRUse
import `fun`.vari.tigrazul.struct.IRUse.Companion.use
import `fun`.vari.tigrazul.struct.IRUser

sealed class BinaryAtom(left:Atom,right:Atom):Atom(),IRUser {
    override val childrenUsesIList: IList<IRUse, IRUser> = IList(this)

    init {
        use(left)
        use(right)
    }
    var left:Atom
        get() = get(0) as Atom
        set(value) = set(0,value)
    var right:Atom
        get() = get(1) as Atom
        set(value) = set(1,value)

    override val current: Atom
        get() = left
    override val next: Atom?
        get() = right
}