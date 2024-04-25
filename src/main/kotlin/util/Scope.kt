package `fun`.vari.tigrazul.util

import kotlin.collections.ArrayList

class Scope<R> {
    private val stack: MutableList<MutableMap<String,R>> =ArrayList(listOf(HashMap()))
    fun getScopeInner() = stack

    fun clear(){
        stack.clear()
        stack.add(HashMap())
    }
    fun pushStack() {
        stack.add(HashMap())
    }

    fun popStack() {
        if (stack.size > 1) {
            stack.removeAt(stack.size - 1)
        }
    }

    operator fun set(name: String, value: R) {
        if (containsCurrent(name)) {
            throw RuntimeException("declare twice")
        } else {
            stack[stack.size - 1][name] = value
        }
    }

    operator fun get(name: String): R? {
        for (i in stack.indices.reversed()) {
            if (stack[i].containsKey(name)) {
                return stack[i][name]
            }
        }
        return null
    }

    inline fun <reified T:R > getDirectly(name: String): T? =
        when(val scopeValue = get(name)){
            is T -> scopeValue;
            else -> null;
    }

    // 查询是否重复声明？
    fun containsCurrent(name: String): Boolean {
        return stack[stack.size - 1].containsKey(name)
    }

    fun checkDuplication(name: String) {
        if (containsCurrent(name)) {
            throw RuntimeException("duplication error")
        }
    }

    val isGlobal: Boolean
        get() = stack.size == 1
}

