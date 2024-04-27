package `fun`.vari.tigrazul.model

class PatternSet(
    override val type: Atom
):SingleAtom() {
    private var patterns: MutableList<Atom> = mutableListOf()
    fun add(pattern:Atom){
        patterns.add(pattern)
    }

    override fun debugInfo() = "{\n\t" + patterns.map { it.debugInfo() }.joinToString("\n\t") + "\n}"
    override fun info() = "{\n\t" + patterns.map { it.info() }.joinToString("\n\t") + "\n}"

}