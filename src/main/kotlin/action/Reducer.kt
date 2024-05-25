package `fun`.vari.tigrazul.action

import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function
import `fun`.vari.tigrazul.model.Identifier.Companion.getValue
import `fun`.vari.tigrazul.struct.IRUser

//这里规定，是作为type的atom进行reduce
//reduce本身要遵守reduce的规则，就是
// Nat |> (A:Type)|->A->A
// reduce 结果：Nat->Nat
// Nat |> term:((A:Type)|->A->A)
// typeReduce 结果：Nat->Nat
fun Atom.reduce():Atom{
    //TODO 先这样
    return when(this){
        is Applied -> this.reduce() //调用专属typeReduce
        is Function -> Function(this.left.reduce(),this.right.reduce())
        is MapsToFunction -> MapsToFunction(this.left.reduce(),this.right.reduce())
        is DataSet -> this
        is PatternSet -> this
        is Term -> this
        Kind -> Kind
        Type -> Type
        Unknown -> Unknown
        is Identifier -> {
            if(this.value is DataSet)
                this
            else if(this.getValue()==this) { //Term
                this.type = this.type.reduce()
                this
            }
            else
                this.getValue().copy().reduce()
        }
        is Verified -> this.left.reduce()
        else -> this //DBI
    }
}


//这里规定，是作为type的applied进行reduce
fun Applied.reduce():Atom{
    val func = right.reduce()
    val argu = left.reduce()
    when(func){
        is Function->{
            val funcArguType = func.left
            val arguType = argu.type.reduce()
            if(funcArguType typeEqual arguType) {
                this.removeChildrenUses()
                return func.right.reduce()
            }
            /*
            when(arguType){
                is Function->{
                    this.removeChildrenUses()//要把原来那层use给去掉
                    return Function(arguType.left, Applied(arguType.right, funcType).reduce())
                }
                else ->{

                }
            }

             */
        }
        is MapsToFunction->{
            val funcArguType = func.left.type.reduce()
            val arguType = argu.type.reduce()
            if(funcArguType typeEqual arguType){
                //this.removeChildrenUses() 结构的是类型才对，等等再改
                func.replace(func.left,argu)
                return func.right.reduce()
            }
        }
        is Identifier->{ //只是类型上是函数
            if(func.value is Term){
                val funcType = func.type.reduce()
                val arguType = argu.type.reduce()
                if(funcType is Function){
                    val funcArguType = funcType.left
                    if(funcArguType typeEqual arguType){
                        return Verified(Applied(argu,func),funcType.right.reduce())
                    }
                }
                if(funcType is MapsToFunction){
                    val funcArguType = funcType.left.type.reduce()
                    if(funcArguType typeEqual arguType){
                        funcType.replace(funcType.left,argu)
                        return Verified(Applied(argu,func),funcType.right.reduce())
                    }
                }
            }
        }
        else -> {}
    }
    return Applied(argu,func)
}

//这里规定，是作为value的atom，的type进行reduce
//已知 Vect:={ <-vnil: (A:Type)|-> xxxx }
//判断 Nat |> vnil<-Vect 的类型
//fun Atom.typeReduce():Atom{
//    return when(this){
//        is Applied -> this.typeReduce() //调用专属typeReduce
//        is Function -> Function(this.left.typeReduce(),this.right.typeReduce()) //TODO 不一定是这样啊
//        is MapsToFunction -> Function(this.left.typeReduce(),this.right.typeReduce()) //TODO 不一定是这样啊
//        is DataSet -> this.type
//        is PatternSet -> this.type
//        is Term -> this.type
//        Kind -> Kind
//        Type -> Type.type
//        Unknown -> Unknown
//        is Identifier -> this.type
//        is Verified -> this.type
//    }
//}
//
//fun Applied.typeReduce():Atom{
//    val funcType = right.typeReduce()
//    val argu = left
//    val arguType = argu.typeReduce()
//    when(funcType){
//        is Function-> {
//            if((arguType uniformEqual funcType.left)) { //这是简单类型论的情况
//                //this.removeChildrenUses() 结构的是类型才对，等等再改
//                return funcType.right
//            }
//            /* 这里在做的事情是，Applied的传递 (A |> B) |> C 的组合问题。先跳过一下
//            when(arguType){
//                is Function->{
//                    this.removeChildrenUses()//要把原来那层use给去掉
//                    return Function(arguType.left, Applied(arguType.right, funcType).reduce())
//                }
//                else ->{
//
//                }
//            }
//
//             */
//        }
//        is MapsToFunction->{
//            val funcTypeArguType = funcType.left.type
//            if(arguType uniformEqual funcTypeArguType){
//                //this.removeChildrenUses() 结构的是类型才对，等等再改
//                val funcTypeRight = funcType.right
//                val funcTypeLeft = funcType.left
//                funcTypeRight.replace(funcTypeLeft,argu)
//                return funcTypeRight
//            }
//
//        }
//        else -> {}
//    }
//    return this
//}


fun Atom.replace(old:Atom,new:Atom):Atom{
    when(this){
        is Applied, is Function, is MapsToFunction,
        is Identifier,is Term,is Verified-> {
            val user = this as IRUser
            for(use in user.childrenUses) {
                val usee = use.usee as Atom
                if(usee == old){
                    use.modifyUsee(new)
                }
                else{
                    usee.replace(old,new)
                }
            }
        }
        else->{}
    }
    return this
}