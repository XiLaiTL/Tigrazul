package `fun`.vari.tigrazul.model



class Term():SingleAtom(){
    override var type: Atom = Unknown

    override fun debugInfo() = "[Term : ${type.info()}]"
    override fun info() = "[Term : ${type.info()}]"
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
    override fun uniform() = if(value is Term) "($name:${type.info()})" else "($name<-$resourceLocation)" //nominal的比较方式，只比较名字！

    companion object{
        fun Atom.isUnknownType():Boolean = this is Identifier && this.value is Term && this.type == Unknown
        fun Atom.getValue():Atom{
            return when(this){
                is Identifier->when(this.value){
                        is Term -> this
                        else -> this.value.getValue()
                    }
                is Verified->this.value.getValue()
                else ->this
            }
        }
    }
}
