package `fun`.vari.tigrazul.model

class PatternSet(
    override val type: Atom
):Atom() {
    private var patterns: MutableList<Atom> = mutableListOf()
    override val first: Atom = this
    override var next: Atom? = null
    fun add(pattern:Atom){
        patterns.add(pattern)
    }

    override fun debugInfo() = "{\n\t" + patterns.map { it.debugInfo() }.joinToString("\n\t") + "\n}"
    override fun info() = "{\n\t" + patterns.map { it.info() }.joinToString("\n\t") + "\n}"

}