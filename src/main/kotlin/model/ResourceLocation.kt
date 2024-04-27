package `fun`.vari.tigrazul.model

data class ResourceLocation(
    val namespace:String,
    val path:String
) {
    companion object{
        val default = ResourceLocation("fun.vari","default")
    }

    override fun toString(): String = "$namespace:$path"
}