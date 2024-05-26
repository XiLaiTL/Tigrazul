import `fun`.vari.tigrazul.tree.analysis
import `fun`.vari.tigrazul.tree.uniform
import kotlin.test.Test

class LogicTest {
    @Test
    fun beta(){
        val code = """
            |-f:a:Type |-> a;
            |-test : A:Type := A |> f ;
        """.trimIndent()
        analysis(code)
    }

    @Test
    fun uni(){
        val code = """
            |-B:Type;
            |-test:= x|->y|->z|->k|->x |> y;
        """.trimIndent()
        uniform(code)
    }
    @Test
    fun t1(){
        val code ="""
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
            |-FORALL := s:Type |-> p:(s->Type) |->x:s|->x |> p;
            |-A:Type;
            |-B:Type;
            |-C:Type;
            |-test1 : (A -> B) -> (B -> C) -> (A->C) 
                := f1:(A->B)|->f2:(B->C)|->(x:A|->(x |>f1) |> f2);
        """.trimIndent()
        uniform(code)

    }

    @Test
    fun t2(){
        val code ="""
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
            |-FORALL := s:Type |-> p:(s->Type) |->x:s|->x |> p;
            |-A:Type;
            |-B:Type;
            |-test1 : A -> B -> B |> A |> AND
                := a:A 
                |-> b:B 
                -- f0:(B |> A |> AND)
                -- f0:(C:Type|->(A->B->C)->C)
                |-> C:Type 
                |-> f:(A->B->C) 
                |-> (b|>a|>f);
        """.trimIndent()
        uniform(code)

    }

    @Test
    fun t3(){
        val code ="""
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
            |-FORALL := s:Type |-> p:(s->Type) |->x:s|->x |> p;
            |-A:Type;
            |-B:Type;
            |-f:FALSE;
            --|-test : A := A |> f ;
            |-test1 : A -> B |> A |> OR
                := a:A |-> C:Type |-> f1:(A->C)|->f2:(B->C)|-> a |> f1;
        """.trimIndent()
        uniform(code)

    }



    @Test
    fun t4(){
        val code = """
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
            |-FORALL := s:Type |-> p:(s->Type) |->x:s|->x |> p;
            |-A:Type;
            |-B:Type;
            |-test1 : (B |> A |> AND) -> (A |> B |> AND)
                := f1:(C1:Type |-> (A->B->C1)->C1) |-> C:Type |-> f2:(B->A->C)|->(a:A|-> b:B|-> (a |> b|> f2) )|>  C |> f1 ;
        """.trimIndent()
        uniform(code)
    }

    @Test
    fun t5(){
        val code ="""
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
            |-FORALL := s:Type |-> p:(s->Type) |->x:s|->x |> p;
            |-A:Type;
            |-B:Type;
            |-test1 :  (B |> A |> OR) -> ((A |> NOT)->B) 
                := f1:(C1:Type |-> (A->C1)->(B->C1)->C1) 
                |-> f2:(A->FALSE) 
                |-> (z:B |-> z) |> (z:A |->B |> z |> f2) |> B |> f1;
        """.trimIndent()
        uniform(code)
    }

    @Test
    fun t6(){
        val code ="""
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
            |-FORALL := s:Type |-> p:(s->Type) |->x:s|->x |> p;
            |-P:Type;
            |-Q:Type;
            |-R:Type;
            |-test1 : ((Q |> P |> OR) |-> R) -> (P->R) |> (Q->R) |> AND 
                := ;
        """.trimIndent()


        uniform(code)

    }

    @Test
    fun nest(){
        val code ="""
            |-FALSE := b:Type |-> b;
            |-S:Type;
            |-P:S->Type;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);


            |-f1:((P |> S |> EXIST)->S);
            |-ff: y:S|-> (y |> P) -> (P |> S |> EXIST)
                := y1:S|-> v:(y1 |> P) |-> (a:Type|->w:(x:S|->((x|>P)->a)) |-> v |> y1 |> w);
            
            |-test: y:S|-> (y |> P) -> S
                := y1:S|-> v:(y1 |> P) |-> (a:Type|->w:(x:S|->((x|>P)->a)) |-> v |> y1 |> w) |> f1;
        """.trimIndent()
        uniform(code)
    }

    @Test
    fun type(){
        val code = """
            |-FALSE := a:Type |-> a;
            |-FORALL := s:Type |-> p:(s->Type) |->x:s|->x |> p;
            |-S:Type;
            |-P:S->Type;
            |-Q: (s:S)|->(s|>P -> FALSE);
            |-test:(S->Type) := Q;
            |-test1:(y:S |-> (y |> P ->FALSE));
            |-test2:(Q |> S |> FORALL);
        """.trimIndent()
        uniform(code)
    }

    @Test
    fun t16(){
        val code = """
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
            |-FORALL := s:Type |-> p:(s->Type) |-> x:s |->x |> p;
            |-S:Type;
            |-P:S->Type;
            |-Q: (s:S)|->(s|>P -> FALSE);
            |-test1: (Q |> S |> EXIST) -> ((P |> S |> FORALL)->FALSE)
                := f1:(Q |> S |> EXIST) 
                -- f1: (a:Type|->((x:S|->((x |> P->FALSE)->a))->a))
                -- f2:((P |> S |> FORALL)->FALSE) 
                -- f2:((x:S|->x |> P) ->FALSE)
                |-> p1:(x:S|-> x |> P)
                |-> (a:Type|->w:(x:S|->((x |> P->FALSE)->a))|->) ;
        """.trimIndent()
        uniform(code)
    }

    @Test
    fun t17(){
        val code ="""
            |-FALSE := a:Type |-> a;
            |-NOT := a:Type |-> (a->FALSE);
            |-AND := a:Type |-> b:Type |-> c:Type |-> (a->b->c)->c;
            |-OR := a:Type |-> b:Type |-> c:Type |-> (a->c)->(b->c)->c;
            |-EXIST := s:Type |-> p:(s->Type)|->a:Type|->((x:s|->((x |> p)->a))->a);
            |-FORALL := s:Type |-> p:(s->Type) |-> x:s |->x |> p;
            |-S:Type;
            |-P:S->Type;
            |-Q: (s:S)|->(s|>P -> FALSE);
            |-test1 : ((P |> S |> EXIST)->FALSE) ->  (Q |> S |> FORALL)
                := f1:((P |> S |> EXIST)->FALSE) 
                -- f1: ((a:Type |->((x:S|->((x |> P)->a))->a))->FALSE)
                -- f2: (Q |> S |> FORALL) 
                -- f2: (x:S |-> x |> Q)
                -- f2: (x:S |-> x |> P -> FALSE)
                |-> y1:S |-> v:(y1 |> P) 
                |-> (a:Type|->w:(x:S|->((x|>P)->a)) |-> v |> y1 |> w ) |> f1;
        """.trimIndent()


        uniform(code)

    }


}