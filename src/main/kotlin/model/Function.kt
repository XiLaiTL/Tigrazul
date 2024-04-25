package `fun`.vari.tigrazul.model

import kotlin.Function


//1. value: MapsToFunction type: Function
//2. value: MapsToFunction type: MapsToFunction
//3. 模式匹配函数呢

class Function(
    var parameter:Atom,
    var body:Atom,
):Atom() {
    override val type: Atom
        get() = Function(parameter.type,body.type) //TODO: 对于依赖类型来说，这是否定的！！
    override val first: Atom
        get() = parameter
    override var next: Atom?
        get() = body
        set(value) { if(value!=null) body = value; }
    override fun debugInfo() = "[${parameter.debugInfo()} → ${body.debugInfo()}]"
    override fun info() = "(${parameter.info()} → ${body.info()})"

}

class MapsToFunction(
    var parameter:Atom,
    var body:Atom,
):Atom(){
    override val type: Atom
        get() {
            return Function(parameter.type,body.type) //TODO: 对于依赖类型来说，这是否定的！！
            //return MapsToFunction(parameter,body.type)
        }
    override val first: Atom
        get() = parameter
    override var next: Atom?
        get() = body
        set(value) { if(value!=null) body = value; }
    override fun debugInfo() = "[${parameter.debugInfo()} ↦ ${body.debugInfo()}]"
    override fun info() = "(${parameter.info()} ↦ ${body.info()})"

}