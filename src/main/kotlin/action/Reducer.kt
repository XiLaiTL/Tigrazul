package `fun`.vari.tigrazul.action

import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function
import `fun`.vari.tigrazul.model.Identifier.Companion.getValue

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
        is Function -> Function(this.left.reduce(),this.right.reduce()) //TODO 不一定是这样啊
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
            else if(this.getValue()==this)
                this
            else
                this.getValue()
        }
        is Verified -> this
    }
}


//这里规定，是作为type的applied进行reduce
fun Applied.reduce():Atom{
    val funcType = right.reduce()
    val arguType = left.reduce()
    when(funcType){
        is Function->{
            if((arguType uniformEqual funcType.left)) { //这是简单类型论的情况
                this.removeChildrenUses()
                return funcType.right
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
            val funcTypeArguType = funcType.left.type
            val arguTypeType = arguType.type
            if(arguTypeType uniformEqual funcTypeArguType){
                //this.removeChildrenUses() 结构的是类型才对，等等再改
                val funcTypeRight = funcType.right
                val funcTypeLeft = funcType.left
                funcTypeRight.replace(funcTypeLeft,arguType)
                return funcTypeRight
            }
        }
        else -> {}
    }
    return this
}

//这里规定，是作为value的atom，的type进行reduce
//已知 Vect:={ <-vnil: (A:Type)|-> xxxx }
//判断 Nat |> vnil<-Vect 的类型
fun Atom.typeReduce():Atom{
    return when(this){
        is Applied -> this.typeReduce() //调用专属typeReduce
        is Function -> Function(this.left.typeReduce(),this.right.typeReduce()) //TODO 不一定是这样啊
        is MapsToFunction -> Function(this.left.typeReduce(),this.right.typeReduce()) //TODO 不一定是这样啊
        is DataSet -> this.type
        is PatternSet -> this.type
        is Term -> this.type
        Kind -> Kind
        Type -> Type.type
        Unknown -> Unknown
        is Identifier -> this.type
        is Verified -> this.type
    }
}

fun Applied.typeReduce():Atom{
    val funcType = right.typeReduce()
    val argu = left
    val arguType = argu.typeReduce()
    when(funcType){
        is Function-> {
            if((arguType uniformEqual funcType.left)) { //这是简单类型论的情况
                //this.removeChildrenUses() 结构的是类型才对，等等再改
                return funcType.right
            }
            /* 这里在做的事情是，Applied的传递 (A |> B) |> C 的组合问题。先跳过一下
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
            val funcTypeArguType = funcType.left.type
            if(arguType uniformEqual funcTypeArguType){
                //this.removeChildrenUses() 结构的是类型才对，等等再改
                val funcTypeRight = funcType.right
                val funcTypeLeft = funcType.left
                funcTypeRight.replace(funcTypeLeft,argu)
                return funcTypeRight
            }

        }
        else -> {}
    }
    return this
}

fun Atom.replace(old:Atom,new:Atom){
    when(this){
        is Applied ,
        is Function ,
        is MapsToFunction -> { //TODO： 如果等于参数应该不做了
            val user = this as BinaryAtom
            for(use in user.childrenUses) {
                if(use.usee == old){
                    use.modifyUsee(new)
                }
                else{
                    (use.usee as Atom).replace(old,new)
                }
            }
        }
        else->{}
    }
}