package `fun`.vari.tigrazul.model

import kotlin.Function


//1. value: MapsToFunction type: Function
//2. value: MapsToFunction type: MapsToFunction
//3. 模式匹配函数呢

class Function(parameter:Atom, body:Atom):BinaryAtom(parameter,body) {
    override val type: Atom
        get() = Function(left.type,right.type) //TODO: 对于依赖类型来说，这是否定的！！

    override fun debugInfo() = "[${left.debugInfo()} → ${right.debugInfo()}]"
    override fun info() = "(${left.info()} → ${right.info()})"
    override fun uniform() = "(${left.uniform()}->${right.uniform()})"
}

class MapsToFunction(parameter:Atom, body:Atom):BinaryAtom(parameter,body){
    override val type: Atom
        get() {
            return Function(left.type,right.type) //TODO: 对于依赖类型来说，这是否定的！！
            //return MapsToFunction(parameter,body.type)
        }

    override fun debugInfo() = "[${left.debugInfo()} ↦ ${right.debugInfo()}]"
    override fun info() = "(${left.info()} ↦ ${right.info()})"
    override fun uniform() = "(${left.uniform()}|->${right.uniform()})"
}