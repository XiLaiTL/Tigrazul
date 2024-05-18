import `fun`.vari.tigrazul.tree.analysis
import kotlin.test.Test

class NatureNumberTest {
    @Test
    fun test1(){
        val code = """
            |-Nat:Type := {
                <-Succ: Nat -> Nat;
                <-Zero: Nat;
            };
            |-Add:Nat->Nat->Nat := {
                | Zero<-Nat |-> n |-> n;
                | m |> Succ<-Nat |-> n |->  (m |> n |> Add) |> Succ<-Nat;
            };
        """.trimIndent();
        analysis(code)
    }

    @Test
    fun test2(){
        val code = """
            |-Nat:Type := {
                <-Succ: Nat -> Nat;
                <-Zero: Nat;
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
            |-Nat:Type
                 := {
                <-Succ: Nat -> Nat;
                <-Zero: Nat;
            };
            -- |-b:Nat->Nat->Nat
                  := x |-> y|->z:Nat |> e:(Nat->Nat) ;
            |-c:Nat->Nat->Nat:= x |-> y|->z |> e:(Nat->Nat) ;
        """.trimIndent()
        analysis(code)
    }

    @Test
    fun scopeTest(){
        val code = """
            |-Nat:Type;
            |-c:Nat->Nat->Nat := x |-> y |-> x  ;
        """.trimIndent()
        analysis(code)
    }

    @Test
    fun depTypeTest1(){
        val code = """
            |-Nat: Type;
            |-Identify : (A:Type)|->A->A;
            |-Test: Nat->Nat 
                  := Nat |> Identify;
        """.trimIndent()
        analysis(code)

    }

    @Test
    fun depTypeTest2(){
        val code = """
            |-Nat: Type;
            |-Identify := (A:Type)|->A->A;
            |-Test: Nat |> Identify 
                  := n :Nat |-> n :Nat;
        """.trimIndent()
        analysis(code)
    }

    @Test
    fun depTypeTest(){
        val code = """
            |-Nat:Type := {
                <-Succ: Nat -> Nat;
                <-Zero: Nat;
            };
            |-Add:Nat->Nat->Nat := {
                | Zero<-Nat |-> n |-> Zero<-Nat;
                | m |> Succ<-Nat |-> n |->  (m |> n |> Add) |> Succ<-Nat;
            };
            |-Mul:Nat->Nat->Nat := {
                | Zero<-Nat |-> n |-> Zero<-Nat;
                | m |> Succ<-Nat |-> n |-> ((m |> n |> Mul) |> n )|> Add;
            };
            |-Vect:(A:Type)|->Nat->Type :={
                <-vnil: (A:Type) |-> Zero<-Nat |> A |> Vect;
                <-vcons: (A:Type) |-> (n:Nat) |-> A -> n |> A |> Vect -> (n |> Succ<-Nat) |> A |> Vect;
            };
            
            |-inner: (n:Nat)|-> n|>Nat|>Vect -> n|>Nat|>Vect -> Nat := {
                | Zero<-Nat |-> Nat |> vnil<-Vect |-> Nat |> vnil<-Vect |-> Zero<-Nat;
                | n |> Succ<-Nat |-> xs |> x |> n |> Nat |> vcons<-Vect |-> ys |> y |> n |> Nat |> vcons<-Vect |-> ((y |> x |> Mul) |> ( ys |> xs |> n |> inner )) |> Add;
            };

        """.trimIndent()
    }
}