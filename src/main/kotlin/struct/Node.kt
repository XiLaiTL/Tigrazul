package `fun`.vari.tigrazul.struct

interface Node<T: Node<T>>{
    val current:T;
    val next:T?;
}