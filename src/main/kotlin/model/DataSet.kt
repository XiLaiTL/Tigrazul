package `fun`.vari.tigrazul.model

class DataSet():SingleAtom() {
    private var terms:MutableMap<String,Identifier> = mutableMapOf()
    override val type: Atom = Type
    fun add(identifier: Identifier) {
        terms[identifier.name] = identifier
    }
    override fun debugInfo() = "{\n\t"+ terms.values.map { it.debugInfo() }.joinToString("\n\t") +"\n}"
    override fun info() = "{\n\t"+ terms.values.map { it.info() }.joinToString("\n\t") +"\n}"

}