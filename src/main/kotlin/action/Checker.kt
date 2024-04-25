package `fun`.vari.tigrazul.action

import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function

//这里可以用dfs实现
fun checkUnknownType(atom:Atom):Boolean{
    if(atom.first==atom) return atom.type == Unknown
    val temp = checkUnknownType(atom.first)
    if(atom.next==null) return temp
    return temp && checkUnknownType(atom.next!!)
}

fun matchingBoundType(functionType:Atom,function:Atom){
    if(!((functionType is Function || functionType is MapsToFunction)
                &&(function is Function ||function is MapsToFunction))) return
    if(function.first is Identifier && function.first.type==Unknown) {
        (function.first as Identifier).type=functionType.first
        return matchingBoundType(functionType.next!!,function.next!!)
    }
    if(function.first.type == functionType.first){
        return matchingBoundType(functionType.next!!,function.next!!)
    }
}
//要怎么比较两个类型相等呢？而且是依赖类型呢
fun matchingType():Boolean{
    TODO()
}