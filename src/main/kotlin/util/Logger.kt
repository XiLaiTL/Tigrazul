package `fun`.vari.tigrazul.util

object Logger {
    val messageList:MutableList<String> = ArrayList()
    private fun print(tag:String,message: Any?){
        "[$tag]: $message".let {
            println(it)
            messageList.add(it)
        }
    }
    fun clear(){ messageList.clear() }
    fun info(message: Any?)= print("INFO",message)
    fun warn(message: Any?)= print("WARN",message)
    fun error(message: Any?)= print("ERROR",message)
}