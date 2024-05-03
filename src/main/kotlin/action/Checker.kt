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

//把type填入Unknown里面去，这里其实在做模式解构！
fun matchingBoundType(type:Atom, value:Atom){

    if(value is PatternSet){
        value.type = type
        for(pattern in value.patterns) {
            matchingBoundType(type,pattern)
        }
        return
    }
    if(!(((type is Function || type is MapsToFunction)
            &&(value is Function ||value is MapsToFunction))
        || (type is Applied && value is Applied))
        ) return
    if(value.current is Identifier && value.current.type==Unknown) {
        (value.current as Identifier).type=type.current
    }
    if(type.next!=null && value.next!=null){
        return matchingBoundType(type.next!!,value.next!!)
    }
//    if(value.current.type == type.current){
//        if(type.next!=null && value.next!=null)
//            return matchingBoundType(type.next!!,value.next!!)
//        return
//    }
}
//要怎么比较两个类型相等呢？而且是依赖类型呢

//首先需要把value的最终形式求出来，然后得到type
//把type的最终形式求出来
//然后比较两个type是否一样
fun <T> Result.Companion.from(successful:Boolean,value: T): Result<T> {
    return if(successful) success(value) else failure(Error())
}

fun matchingType(type:Atom,value:Atom):Result<Atom>{
    val target = type.typeReduce()
    val matched = value.type.typeReduce()

    //TODO: 这里还是得删掉uses
    return Result.from(target uniformEqual  matched,matched)
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