package `fun`.vari.tigrazul.model

import `fun`.vari.tigrazul.action.DeBruijnLevel.toDeBruijnLevelUniform


//1. value: MapsToFunction type: Function
//2. value: MapsToFunction type: MapsToFunction
//3. 模式匹配函数呢

class Function(parameter:Atom, body:Atom):BinaryAtom(parameter,body) {
    override val type: Atom
        get() = Function(left.type,right.type)
        //如果这样，所有类型都需要考虑在调用的时候进行解构，很明显这非常麻烦！
    override fun debugInfo() = "[${left.debugInfo()} → ${right.debugInfo()}]"
    override fun info() = "(${left.info()} → ${right.info()})"
    override fun plainWithType() ="(${left.plain()} -> ${right.plain()})"
    override fun plain() = "(${left.plain()} -> ${right.plain()})"
    override fun uniform(): String {
        return this.toDeBruijnLevelUniform() // "(${left.uniform()}->${right.uniform()})"
    }
}

class MapsToFunction(parameter:Atom, body:Atom):BinaryAtom(parameter,body){
    override val type: Atom
        get() {
            return MapsToFunction(left,right.type)
        }

    override fun debugInfo() = "[${left.debugInfo()} ↦ ${right.debugInfo()}]"
    override fun info() = "(${left.info()} ↦ ${right.info()})"
    override fun plainWithType() ="(${left.plainWithType()} |-> ${right.plain()})"
    override fun plain() = "(${left.plainWithType()} |-> ${right.plain()})"
    override fun uniform(): String {
        return this.toDeBruijnLevelUniform() //"(${left.uniform()}|->${right.uniform()})"
    }
}