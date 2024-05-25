package `fun`.vari.tigrazul.action

import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function
import `fun`.vari.tigrazul.util.Scope

object DeBruijnLevel{
    private var scope = Scope<Atom>()
    fun Scope<Atom>.getLevel() = getScopeInner().size - 1
    fun Scope<Atom>.getDeBruijnLevel(name:String):Int?{
        //val cur = getLevel()
        for (i in getScopeInner().indices.reversed()) {
            if (this.getScopeInner()[i].containsKey(name)) {
                return i // cur-i
            }
        }
        return null
    }

    fun Atom.toDeBruijnLevelAtom():Atom{
        return when(this){
            is Applied -> Applied(left.toDeBruijnLevelAtom(),right.toDeBruijnLevelAtom())
            is Function -> {
                scope.pushStack()
                val left = this.left.toDeBruijnLevelAtom()
                val right = this.right.toDeBruijnLevelAtom()
                val level = scope.getLevel()
                scope.popStack()
                MapsToFunction(Identifier("${level}").apply { type = left },right)
            }
            is MapsToFunction->{
                scope.pushStack()
                val left = this.left
                var leftConverted:Atom;
                if(left is Identifier && left.value is Term){
                    scope[left.uniformName]  = Identifier("${scope.getLevel()}").apply { type = left.type.toDeBruijnLevelAtom() }
                    leftConverted = left.toDeBruijnLevelAtom()
                }
                else if(left is Verified){
                    leftConverted = Identifier("${scope.getLevel()}").apply { type = left.type.toDeBruijnLevelAtom() }
                }
                else{
                    leftConverted = left.toDeBruijnLevelAtom()
                }
                val right = right.toDeBruijnLevelAtom()
                scope.popStack()
                MapsToFunction(leftConverted,right)
            }
            is Identifier->{
                scope[this.uniformName]?:this
            }
            is Verified->{
                left.toDeBruijnLevelAtom()
            }
            else -> this
        }
    }

    fun Atom.toDeBruijnLevelUniform():String{
        fun uniform(atom:Atom):String = when(atom){
            is Applied -> "(${uniform(atom.left)}|>${uniform(atom.right)})"
            is MapsToFunction -> "(${uniform(atom.left)}|->${uniform(atom.right)})"
            is Verified -> atom.left.uniform()
            else -> atom.uniform()
        }
        val dbl = this.toDeBruijnLevelAtom()
        return uniform(dbl)
    }
//        return when(this){
//            is Applied -> "(${left.toDeBruijnIndex()}|>${right.toDeBruijnIndex()})"
//            is Function -> {
//                scope.pushStack()
//                val left = this.left.toDeBruijnIndex()
//                val right = this.right.toDeBruijnIndex()
//                "((${scope.getLevel()}:$left)|->$right)".also {
//                    scope.popStack()
//                }
//            }
//            is MapsToFunction -> {
//                scope.pushStack()
//                val res = this.copy() as MapsToFunction
//                val left = res.left
//                if(left is Identifier && left.value is Term){
//                    scope[left.uniformName] = this
//                    val level = scope.getDeBruijnLevel(left.uniformName)
//                    if(level!=null){
//                        res.replace(left, Identifier("$level").apply { type = left.type })
//                    }
//                }
//                val leftConverted = res.left.toDeBruijnIndex()
//                val right = res.right.toDeBruijnIndex()
//                scope.popStack()
//                "($leftConverted|->$right)"
//            }
//            else -> this.uniform()
//        }
//    }

}