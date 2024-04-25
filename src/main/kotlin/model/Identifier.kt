package `fun`.vari.tigrazul.model

class Identifier(
    var name: String
):Atom() {
    override var type: Atom = Unknown
    var value:Atom = this
    override val first: Atom = value
    override var next: Atom? = null
    override fun debugInfo() = "[$name : ${type.info()}"+
                if(value==this) "]" else " := ${value.debugInfo()} ]"
    override fun info() = "[$name : ${type.info()}]"
}
/*
class Bound(
    var name: String
):Atom(){
    override var type: Atom = Unknown
    override val first: Atom = this
    override var next: Atom? = null
}
 */