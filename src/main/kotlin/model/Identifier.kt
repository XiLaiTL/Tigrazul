package `fun`.vari.tigrazul.model

import `fun`.vari.tigrazul.action.copy


class Term():UnaryAtom(Unknown){
    override var type: Atom
        get() = value
        set(type){value=type}

    override fun debugInfo() = "[Term : ${type.info()}]"
    override fun info() = "[Term : ${type.info()}]"
    override fun plainWithType() = ""
    override fun plain() = ""
    override fun uniform() = "_"
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
    override fun plainWithType() ="$name:${type.plain()}"
    override fun plain() = name
    override fun uniform() = if(value is Term) "($name:${type.uniform()})" else uniformName //nominal的比较方式，只比较名字！
    val uniformName
        get() = "($name<-$resourceLocation)"

    companion object{
        fun Atom.isUnknownType():Boolean = this is Identifier && this.value is Term && this.type == Unknown
        fun Atom.getValue():Atom{
            return when(this){
                is Identifier->when(this.value){
                        is Term -> this
                        else -> this.value.getValue()
                    }
                is Verified->this.left.getValue()
                else ->this
            }
        }
    }
}
