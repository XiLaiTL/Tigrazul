package `fun`.vari.tigrazul.tree

import `fun`.vari.tigrazul.util.Logger
import kotlin.reflect.KClass

class TigrazulVisitorException : RuntimeException {
    constructor() : super() {}
    constructor(message: String) : super(message) {
        Logger.error(message)
    }

    companion object {
        infix fun <T : Any> at(e: KClass<T>): TigrazulVisitorException {
            return TigrazulVisitorException(e.qualifiedName?:e.toString())
        }
    }
}