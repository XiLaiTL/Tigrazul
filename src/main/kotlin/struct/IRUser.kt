package `fun`.vari.tigrazul.struct

interface IRUser:IRUsee {
    val childrenUsesIList:IList<IRUse,IRUser>

    val childrenUses:List<IRUse>
        get() = childrenUsesIList.toReadOnlyList()

    val children:List<IRUsee>
        get() = childrenUsesIList.map { it.data!!.usee }

    fun removeChildrenUses() = childrenUsesIList.toReadOnlyList().forEach { it.remove() }

    fun addChildUse(use:IRUse) = childrenUsesIList.addLastNode(use.userNode)

    operator fun get(i:Int) = childrenUsesIList[i].data?.usee

    operator fun set(i:Int,usee:IRUsee) {
        val node = childrenUsesIList[i]
        node.data?.useeNode?.remove()
        usee.addParentUse(node.data!!)
    }

    val size:Int
        get() = childrenUsesIList.size

}