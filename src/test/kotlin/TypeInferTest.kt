import kotlin.test.Test

class TypeInferTest {
    @Test
    fun one(){
        val code ="""
            |-Nat:Type; -- (4) -- 不需要推导或无法推导
            |-t:Nat := x:Nat |> y; -- (1) (5) (7) -- 无法推导
            |-t:Nat := x |> y:Nat->Nat; -- (1) (5) (8) -- x:Nat
            |-t:Nat := x:Nat |> y:Nat->Nat; -- (1) (5) (9) -- 不需要推导
            |-t:Nat := x |> y; -- (1) (5) (10) -- 无法推导
            |-t := x:Nat |> y; -- (1) (6) (7) -- 无法推导
            |-t := x |> y:Nat->Nat; -- (1) (6) (8) -- x:Nat
            |-t:Nat->Nat := x:Nat -> y; -- (2) (5) (7) -- y:Nat
            |-t:Nat->Nat := x:Nat |-> y; -- (3) (5) (7) -- y:Nat
            |-t:Nat->Nat := x |-> y:Nat; -- (3) (5) (8) -- x:Nat
            |-t:Nat->Nat := x |-> y:Nat; -- (3) (5) (10) -- x:Nat,y:Nat
            |-t := x:Nat |-> y; -- (3) (6) (7) -- 无法推导
        """.trimIndent()
    }
}