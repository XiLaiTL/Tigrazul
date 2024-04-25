package `fun`.vari.tigrazul.tree

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun analysis(code:String){
    val input = CharStreams.fromString(code)

    val lexer = TrigrazulLexer(input)
    val tokens= CommonTokenStream(lexer)
    val parser = TrigrazulParser(tokens).apply {
        buildParseTree=true
        addErrorListener(TigrazulParserErrorListener)
    }
    val tree = parser.application()
    val visitor = TigrazulMainVisitor()
    for(statement in tree.statement()){
        val atom = visitor.visit(statement)
        println(atom.debugInfo())
    }
}