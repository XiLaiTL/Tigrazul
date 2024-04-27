package `fun`.vari.tigrazul.struct

class IRUse {
    val useeNode: IListNode<IRUse,IRUsee> = IListNode(this)
    val usee:IRUsee
        get()=useeNode.container!!

    val userNode: IListNode<IRUse,IRUser> = IListNode(this)
    val user:IRUser
        get()=userNode.container!!

    fun remove(){
        useeNode.remove()
        userNode.remove()
    }
    fun modifyUsee(newUsee:IRUsee){
        useeNode.remove()
        newUsee.addParentUse(this)
    }
    companion object{
        fun IRUser.use(usee:IRUsee)=
            IRUse().also {
                usee.addParentUse(it)
                this.addChildUse(it)
            }

        fun IRUsee.usedBy(user:IRUser)=
            user.use(this)
    }
}