package `fun`.vari.tigrazul.model

import `fun`.vari.tigrazul.struct.IList
import `fun`.vari.tigrazul.struct.IRUse
import `fun`.vari.tigrazul.struct.IRUse.Companion.use
import `fun`.vari.tigrazul.struct.IRUser

sealed class UnaryAtom(value:Atom):Atom(),IRUser{
    override val childrenUsesIList: IList<IRUse, IRUser> = IList(this)

    init{
        use(value)
    }
    var value:Atom
        get() = get(0) as Atom
        set(value) = set(0,value)
    override val current: Atom
        get() = value
    override val next: Atom? = null

}