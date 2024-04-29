package `fun`.vari.tigrazul.action

import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function

//这里规定，是作为type的atom进行reduce
fun Atom.typeReduce():Atom{
    //TODO 先这样
    return when(this){
        is Applied -> this.typeReduce() //调用专属typeReduce
        is Function -> Function(this.left.typeReduce(),this.right.typeReduce()) //TODO 不一定是这样啊
        is MapsToFunction -> MapsToFunction(this.left.typeReduce(),this.right.typeReduce())
        is DataSet -> this
        is PatternSet -> this
        is Term -> this
        Kind -> Kind
        Type -> Type
        Unknown -> Unknown
        is Identifier -> this
        is Verified -> this
    }
}


//这里规定，是作为type的applied进行reduce
fun Applied.typeReduce():Atom{
    val funcType = right
    val arguType = left
    when(funcType){
        is Function->{
            if((arguType uniformEqual funcType.left)) { //这是简单类型论的情况
                this.removeChildrenUses()
                return funcType.right
            }
            when(arguType){
                is Function->{
                    this.removeChildrenUses()//要把原来那层use给去掉
                    return Function(arguType.left, Applied(arguType.right, funcType).typeReduce())
                }
                else ->{

                }
            }
        }
        is MapsToFunction->{//这个没办法，必须用到元素//这就是矛盾点所在！！！
        }
        else -> {}
    }
    return this
}


//reduce的是atom，而不只是type！
fun reduce(atom: Atom){
    //只有在模式匹配的时候，才有可能mapsto前面是非identifier的

}


