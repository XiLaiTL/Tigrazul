package `fun`.vari.tigrazul.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import `fun`.vari.tigrazul.tree.analysis
import `fun`.vari.tigrazul.util.Logger
import kotlinx.coroutines.CoroutineScope
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
fun CodeCalculatorPage() {
    var inputText by remember { mutableStateOf("""
            |-Nat: Type;
            |-Identify : (A:Type)|->A->A;
            |-Test: Nat->Nat 
                  := Nat |> Identify;
    """.trimIndent()) }
    var outputText by remember { mutableStateOf("") }


    Row (
        modifier = Modifier.fillMaxSize()

//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
    ){

        Column(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.5f)
        ){
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxSize()
                //label = { Text("输入框") }

            )

        }

        Column (
            modifier = Modifier.fillMaxSize()
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Button(
                    onClick = {
                        analysis(inputText)
                        outputText = Logger.messageList.joinToString("\n")
                    }
                ) {
                    Icon(Icons.Default.Done,"运行")
                    Text("运行")
                }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        Logger.clear()
                        outputText = ""
                    }
                ){
                    Icon(Icons.Default.Clear,"清空输出")
                    Text("清空输出")
                }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(outputText),null)
                    }
                ){
                    Icon(Icons.Default.AddCircle,"复制")
                    Text("复制")
                }
                Spacer(Modifier.width(10.dp))


            }
            LazyColumn (
                modifier = Modifier.fillMaxWidth()
            ){
                if(outputText.isNotBlank()){
                    itemsIndexed(outputText.split("\n")){ index,str->
                        if(str.contains("[INFO]:")) Text(str )
                        else if(str.contains("[WARN]:"))  Text(str, color = Color.Yellow )
                        else if(str.contains("[ERROR]:"))  Text(str, color = Color.Red )
                        else Text(str)
                        Spacer(Modifier.height(10.dp))
//                        if(str.couldParseLatex()){
//                            Latex(str.replace(" ","\\;"), "codeOutputText$index", modifier = Modifier.fillMaxWidth(0.98f), alignment = Alignment.Start)
//                        }
//                        else{
//                            Text(str)
//                        }
                    }
                }
            }

        }
    }


}