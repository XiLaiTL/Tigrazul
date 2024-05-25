package `fun`.vari.tigrazul.tree

import `fun`.vari.tigrazul.action.reduce
import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function
import `fun`.vari.tigrazul.util.Logger
import `fun`.vari.tigrazul.util.Scope
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun analysis(code:String){
    val scope = getScope(code)
    for(atom in scope.getScopeInner()[0].values){
        Logger.info(atom.debugInfo())
    }
}

fun uniform(code:String){
    val scope = getScope(code)
    for(atom in scope.getScopeInner()[0].values){
        println(atom.debugInfo())
        val value1 = (atom as Identifier).value
        print(atom.name +": ")
        if(value1 is Term){
            println(value1.type.reduce().uniform())
        }
        else{
            println(value1.type.reduce().uniform())
            println(":="+value1.uniform())
        }
    }
}

fun getScope(code:String):Scope<Atom>{
    val input = CharStreams.fromString(code)
    val lexer = TrigrazulLexer(input)
    val tokens= CommonTokenStream(lexer)
    val parser = TrigrazulParser(tokens).apply {
        buildParseTree=true
        addErrorListener(TigrazulParserErrorListener)
    }
    val tree = parser.application()
    val visitor = TigrazulMainVisitor()
    visitor.visit(tree)
    return visitor.scope
}

fun depart(code:String,atomName:String="temp"):List<String>{
    val scope = getScope(code)
    var index = 0;
    val name = "arg"
    val resList = mutableListOf<String>()
    fun getArgu(atom:Atom){
        when(atom){
            is Function ->{
                resList.add("$name${index++}:${atom.left.plain()}")
                getArgu(atom.right)
            }
            is MapsToFunction ->{
                resList.add(atom.left.plainWithType())
                getArgu(atom.right)
            }
            else->{}
        }
    }
    val tempAtom = scope[atomName]
    if(tempAtom!=null){
        getArgu((tempAtom as Identifier).type.reduce())
    }
    return resList
}