package `fun`.vari.tigrazul.util

object Logger {
    val messageList:MutableList<String> = ArrayList()
    private fun print(tag:String,message: Any?){
        val printMessage = "[$tag]: $message".let {
            println(it)
            messageList.add(it)
        }
        println(printMessage)

    }
    fun info(message: Any?)= print("INFO")
    fun warn(message: Any?)= print("WARN")
    fun error(message: Any?)= print("ERROR")
}