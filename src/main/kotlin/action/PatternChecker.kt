package `fun`.vari.tigrazul.action

import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function
import `fun`.vari.tigrazul.model.Identifier.Companion.getValue

//拿到第一个构造器，如果没有用构造器的话返回Unknown？
//先不做m |> n |> Add |> Succ，只支持 (m |> n |> Add) |> Succ
fun checkHead(type: DataSet, value:Atom):Atom{
    when (value) {
        is Applied -> {
            val identifier = value.next
            if(identifier is Identifier && type.contains(identifier)){
                return identifier
            }
        }

        is Identifier -> {
            return if(type.contains(value)) value else Unknown
        }
        else->{}
    }
    return Unknown
}

//假设不做递归解构 如(Zero |> Succ)
fun depart(type:Atom,patterns:List<Atom>):Boolean{
    if(!(type is Function || type is MapsToFunction)) return true
    val firstTypeAtom = type.current.getValue()
    //var firstTypeList = mutableListOf<Atom>()
    //填入dataset的构造子作为key，剩下的作为value
    val map = mutableMapOf<Atom,MutableList<Atom>>()
    if(firstTypeAtom !is DataSet)
        return false //TODO() //这个也要出局
    for(pattern in patterns){
        if(!(pattern is Function || pattern is MapsToFunction))
            return false//TODO()//出局

        val constructor =  checkHead(firstTypeAtom ,pattern.current)
        map.getOrPut(constructor){mutableListOf()}.add(pattern.next!!)
    }
    val defaultOne = if(map.contains(Unknown) ) 1 else 0
    if( map.size==firstTypeAtom.size + defaultOne  ){
        //说明完整
        //这里每个行都要检查
        for((key,value) in map){
            val successful = depart(type.next!!,value)
            if(!successful) return false
        }
        return true
    }
    if(defaultOne == 1 && map.size<firstTypeAtom.size + 1){
        //说明不完整
        //这里只检查Unknown的行
        return depart(type.next!!,map[Unknown]!!)
    }

    return false
}


fun matchingPatternType(type: Atom, value: PatternSet):Result<Atom>{
    val target = type.reduce()
    for(pattern in value.patterns){
        val matched = pattern.type.reduce()
        val successful = target typeEqual  matched
        if(!successful) return Result.failure(Error("\n    target: ${target.uniform()}; \n    findit: ${matched.uniform()}"))
    }

    //取出列，覆盖范围检查
    val successful = depart(target,value.patterns)
    if(!successful) return Result.failure(Error("target: ${target.uniform()}; find: ${value.uniform()}"))

    return Result.success(target)
}