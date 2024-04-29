package `fun`.vari.tigrazul.action

import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function

//这里可以用dfs实现
fun checkUnknownType(atom:Atom):Boolean{
    if(atom.current==atom) return atom.type == Unknown
    val temp = checkUnknownType(atom.current)
    if(atom.next==null) return temp
    return temp && checkUnknownType(atom.next!!)
}

fun matchingBoundType(functionType:Atom,function:Atom){
    if(!((functionType is Function || functionType is MapsToFunction)
                &&(function is Function ||function is MapsToFunction))) return
    if(function.current is Identifier && function.current.type==Unknown) {
        (function.current as Identifier).type=functionType.current
        return matchingBoundType(functionType.next!!,function.next!!)
    }
    if(function.current.type == functionType.current){
        return matchingBoundType(functionType.next!!,function.next!!)
    }
}
//要怎么比较两个类型相等呢？而且是依赖类型呢

//首先需要把value的最终形式求出来，然后得到type
//把type的最终形式求出来
//然后比较两个type是否一样
data class CheckerResult(
    val matchedType:Atom,
    val success:Boolean
)

fun matchingType(type:Atom,value:Atom):CheckerResult{
    val target = type.typeReduce()
    val matched = value.type.typeReduce()

    //TODO: 这里还是得删掉uses
    return  CheckerResult( matched,target uniformEqual matched )
}

//这里只做字符串比较！
infix fun Atom.uniformEqual(value:Atom):Boolean{
    return this.uniform() == value.uniform()
}

fun Atom.copy():Atom{
    return when(this){
        is Applied -> Applied(this.left.copy(),this.right.copy())
        is Function -> Function(this.left.copy(),this.right.copy())
        is MapsToFunction -> MapsToFunction(this.left.copy(),this.right.copy())
        is DataSet -> this
        is PatternSet -> this
        is Term -> Term().apply { type = this@copy.type }
        Kind -> Kind
        Type -> Type
        Unknown -> Unknown
        is Identifier -> this //TODO
        is Verified -> Verified(this.value,this.type.copy()) //TODO
    }
}