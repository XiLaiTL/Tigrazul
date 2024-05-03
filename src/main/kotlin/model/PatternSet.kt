package `fun`.vari.tigrazul.model

class PatternSet():SingleAtom() {
    override var type: Atom = Unknown // 这里类型在matchingBound那边处理了
    var patterns: MutableList<Atom> = mutableListOf()
    fun add(pattern:Atom){
        patterns.add(pattern)
    }

    override fun debugInfo() = "{\n\t" + patterns.map { it.info() }.joinToString("\n\t") + "\n}"
    override fun info() = "{\n\t" + patterns.map { it.info() }.joinToString("\n\t") + "\n}"
    override fun uniform() = "{|"+ patterns.map { it.uniform() }.joinToString(";|") +";}"

}