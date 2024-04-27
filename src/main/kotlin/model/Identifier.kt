package `fun`.vari.tigrazul.model

import `fun`.vari.tigrazul.struct.IList
import `fun`.vari.tigrazul.struct.IRUse
import `fun`.vari.tigrazul.struct.IRUser


class Term():SingleAtom(){
    override var type: Atom = Unknown

    override fun debugInfo() = "[Term : ${type.info()}]"
    override fun info() = "[Term : ${type.info()}]"
}

class Identifier(
    var name: String,
    var resourceLocation:ResourceLocation = ResourceLocation.default
):UnaryAtom(Term()) {
    override var type: Atom
        get() = this.value.type
        set(type)=
            when(val value = this.value){
                is Term -> value.type = type
                else -> {}
            }

    override fun debugInfo() = "[$name : ${type.info()}"+
                if(value is Term) "]" else " := ${value.debugInfo()} ]"
    override fun info() = "[$name : ${type.info()}]"
}
