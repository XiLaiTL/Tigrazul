import `fun`.vari.tigrazul.tree.analysis
import kotlin.test.Test

class LogicTest {
    @Test
    fun t1(){
        val code ="""
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-A:Type;
            |-B:Type;
            |-f:(a:Type |-> a);
            |-test : A := A |> f ;
        """.trimIndent()
        analysis(code)

    }
}