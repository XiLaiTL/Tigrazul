package `fun`.vari.tigrazul.struct

interface IRUsee{
    val parentsUsesIList:IList<IRUse,IRUsee>

    val parentsUses:List<IRUse>
        get() = parentsUsesIList.toReadOnlyList()

    fun removeParentsUses()=parentsUsesIList.toReadOnlyList().forEach { it.remove() }

    fun addParentUse(use:IRUse)=parentsUsesIList.addLastNode(use.useeNode)
}