package `fun`.vari.tigrazul.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import `fun`.vari.tigrazul.tree.analysis
import `fun`.vari.tigrazul.tree.depart
import `fun`.vari.tigrazul.util.Logger
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
fun SingleCalculatorPage(){
    var inputText by remember { mutableStateOf("""A \to B \to (B \rhd A \rhd AND)""") }
    var scriptInputText by remember { mutableStateOf("""
        |-FALSE := a:Type |-> a;
        |-NOT := a:Type |-> (a->FALSE);
        |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
        |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
        |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
        |-FORALL := s:Type |-> p:(s->Type) |->x:s|->x |> p;
        |-A:Type;
        |-B:Type;
    """.trimIndent()) }
    var funcInputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }
    var showScript by remember { mutableStateOf(false) }
    val inputList = remember { (mutableStateListOf<String>()) }
    val checkedList = remember { mutableStateListOf<Int>() }
    Row (
        modifier = Modifier.fillMaxSize()
    ){
        Column (
            modifier = Modifier.fillMaxWidth(0.5f)
        ){
            if(showScript){
                TextField(
                    value = scriptInputText,
                    onValueChange = { scriptInputText = it },
                    modifier = Modifier.fillMaxHeight(0.3f).fillMaxWidth(),
                    label = { Text("预设前提") }
                )
            }

            TextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("命题/类型") }
            )
            Latex(inputText.replace(" ","\\;"),"inputText")

            LazyColumn {
                itemsIndexed(inputList){index,str->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = str,
                            onValueChange = { inputList.set(index,it)  },
                            modifier = Modifier.fillMaxWidth(0.9f),
                        )
                        Checkbox(checked = checkedList.contains(index), onCheckedChange = {
                            if(checkedList.contains(index)) checkedList.remove(index)
                            else checkedList.add(index)
                        })
                    }

                }
            }
        }
        Row(
            Modifier.fillMaxWidth(0.3f).fillMaxHeight().background(MaterialTheme.colors.background)
        ) {
            Spacer(Modifier.width(5.dp))
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(Modifier.height(1.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (!showScript) ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                    else ButtonDefaults.buttonColors(),
                    onClick = {
                        if (!showScript) {
                            showScript = true
                        } else {
                            showScript = false
                        }
                    }
                ) {
                    if (!showScript)
                        Text("预设前提")
                    else {
                        Text("收起预设")
                        IconButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                scriptInputText = ""
                                showScript = false
                            }
                        ) {
                            Icon(Icons.Default.Clear, "清除证明前提")
                        }
                    }
                }
                Spacer(Modifier.height(5.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        Logger.clear()
                        outputText=""
                    }
                ){
                    Text("清空输出")
                }
                Spacer(Modifier.height(5.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val code = """
                            ${scriptInputText}
                            |-temp:${inputText};
                            ${ if(inputList.size >= 1) 
                                    inputList.map { it.replace(Regex("^(:=|\\|->)"),"|-")+";" }.dropLast(1).joinToString("\n")
                                else ""
                            }
                        """.trimIndent()
                        val res = depart(code, funcInputText.ifBlank { "temp" })
                        inputList.addAll(res.map { "|->$it" })
                    }
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text("函数拆解")
                        TextField(
                            value = funcInputText,
                            onValueChange = { funcInputText = it },
                            label = { Text("函数名") }
                        )
                    }
                }
                Spacer(Modifier.height(5.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        checkedList.sortDescending()
                        if(checkedList.isEmpty()) inputList.add("|->")
                        else inputList.add(checkedList[0]+1,"|->")
                    }
                ){
                    Text("下一步骤")
                }
                Spacer(Modifier.height(5.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        checkedList.sortDescending()
                        checkedList.forEach { inputList.removeAt(it) }
                        checkedList.clear()
                    }
                ){
                    Text("删除选中")
                }
                Spacer(Modifier.height(5.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                    onClick = {
                        inputList.set(0,inputList[0].replace(Regex("^\\|->"),":="))
                        val code = """
                            ${scriptInputText}
                            |-temp:${inputText}
                                ${inputList.joinToString ( "\n")};
                        """.trimIndent()
                        print(code)
                        analysis(code)
                        outputText = Logger.messageList.joinToString("\n")
                    }
                ){
                    Text("开始检查")
                }
                Spacer(Modifier.height(5.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                    onClick = {
                        inputList.set(0,inputList[0].replace(Regex("^\\|->"),":="))
                        val code = """
                            ${scriptInputText}
                            |-temp:${inputText}
                                ${inputList.joinToString ( "\n")};
                        """.trimIndent()
                        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(code),null)
                    }
                ){
                    Text("复制证明")
                }
                Spacer(Modifier.width(10.dp))
            }
        }
        Spacer(Modifier.width(5.dp))


        LazyColumn (
            modifier = Modifier.fillMaxWidth()
        ){
            if(outputText.isNotBlank()){
                itemsIndexed(outputText.split("\n")){ index,str->
                    if(str.contains("[INFO]:")) {
                        if(str.contains("temp"))
                            Text(str, color = Color.Cyan )
                        else
                            Text(str)
                    }
                    else if(str.contains("[WARN]:"))  Text(str, color = Color.Yellow )
                    else if(str.contains("[ERROR]:"))  Text(str, color = Color.Red )
                    else Text(str)
                    Spacer(Modifier.height(10.dp))
//                    Latex(str.replace(" ","\\;"), "outputText$index",
//                        alignment = if(index==0) Alignment.CenterHorizontally else Alignment.Start)
                }
            }
        }
    }
}