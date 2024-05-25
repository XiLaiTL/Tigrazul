package `fun`.vari.tigrazul.model

import `fun`.vari.tigrazul.struct.*

sealed class Atom: Node<Atom>, IRUsee {
    override val parentsUsesIList: IList<IRUse, IRUsee> = IList(this)

    abstract val type:Atom
    abstract fun debugInfo():String
    abstract fun info():String
    abstract fun plainWithType():String
    abstract fun plain():String
    abstract fun uniform():String
}