import `fun`.vari.tigrazul.tree.analysis
import kotlin.test.Test

class NatureNumberTest {
    @Test
    fun test1(){
        val code = """
            |-Nat:Type := {
                .Succ: Nat -> Nat;
                .Zero: Nat;
            };
            |-Add:Nat->Nat->Nat := {
                | Zero |-> n |-> Zero;
                | m |> Succ |-> n |->  m |> n |> Add |> Succ;
            };
        """.trimIndent();
    }

    @Test
    fun test2(){
        val code = """
            |-Nat:Type := {
                .Succ: Nat -> Nat;
                .Zero: Nat;
            };
            |-a:Nat;
            |-b:Nat->Nat->Nat:= x |-> y|->z |> e:(Nat->Nat) ;
            |-c:= x:Nat |-> y:Nat;
            |-d:= x:Bit:Type |-> y:Nat |> e:(Nat->Nat);
        """.trimIndent()
        analysis(code)
    }

    @Test
    fun typeCheck(){
        val code = """
            |-Nat:Type := {
                .Succ: Nat -> Nat;
                .Zero: Nat;
            };
            |-b:Nat->Nat->Nat:= x |-> y|->z:Nat |> e:(Nat->Nat) ;
            |-c:Nat->Nat->Nat:= x |-> y|->z |> e:(Nat->Nat) ;
        """.trimIndent()
        analysis(code)
    }
}