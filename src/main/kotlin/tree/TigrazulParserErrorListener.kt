package `fun`.vari.tigrazul.tree

import `fun`.vari.tigrazul.util.Logger
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer


object TigrazulParserErrorListener: BaseErrorListener(){
    override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
        val stack=(recognizer as Parser).ruleInvocationStack.apply { reverse() }
        Logger.error("rule stack: $stack")
        Logger.error("line $line:$charPositionInLine at $offendingSymbol: $msg")
    }
}