import kotlin.test.Test

class PatternTest {
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
            }; -- (2) (4) (6) (7) -- 通过模式匹配类型检查
            |-t:Nat->Nat := {
                | Zero<-Nat |-> Zero<-Nat;
                | n |-> n;
            }; -- (2) (3) (7) -- 通过模式匹配类型检查
            |-t:Nat->Nat := {
                | Zero<-Nat |-> Zero<-Nat;
                | m |> Succ<-Nat |-> Zero<-Nat;
                | n |-> n;
            }; -- (2) (3) (5) -- 通过模式匹配类型检查
            |-t:Nat->Nat :={
                | Zero<-Nat |->Zero<-Nat;
            }; -- (1) (3) (8) -- 不通过模式匹配类型检查
        """.trimIndent()
    }
}