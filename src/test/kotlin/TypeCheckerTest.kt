import kotlin.test.Test

class TypeCheckerTest {
    @Test
    fun test1(){
        val code = """
        |-Nat:Type;
        |-t:= Nat:Type; -- (1) -- 通过检查
        |-t:= (x:Nat |> (x:Nat|->y:Nat)):Nat; -- (2) -- 通过检查
        |-t:= x:A; -- (3) -- 通过检查，但无法说明A是未知类型
        |-t:Nat = x:Nat |> (x:Nat|->y:Nat); -- (5) -- 通过检查
    """.trimIndent()
    }
    @Test
    fun test2(){
        val code = """
            |-Nat:Type := {
                <-Succ: Nat -> Nat;
                <-Zero: Nat;
            };
            |-Add:Nat->Nat->Nat := {
                | Zero<-Nat |-> n |-> n;
                | m |> Succ<-Nat |-> n |->  (m |> n |> Add) |> Succ<-Nat;
            }; -- (4)-- 通过检查
        """.trimIndent()
    }
}