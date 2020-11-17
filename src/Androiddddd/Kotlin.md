# 可见性修饰符
## 包
+ 函数、属性和类、对象和接口可以在顶层声明，即直接在包内：
    ```
    // 文件名：example.kt
    package foo

    fun baz() { …… }
    class Bar { …… }
    var test ...
    ```
+ 如果你不指定任何可见性修饰符，默认为 public，这意味着你的声明将随处可见；
+ 如果你声明为 private，它只会在声明它的文件内可见；
+ 如果你声明为 internal，它会在相同模块内随处可见；
+ protected 不适用于顶层声明。

    ```
    // 文件名：example.kt
    package foo

    private fun foo() { …… } // 在 example.kt 内可见

    public var bar: Int = 5 // 该属性随处可见
        private set         // setter 只在 example.kt 内可见
        
    internal val baz = 6    // 相同模块内可见
    ```
+ 什么是模块可见？？

## 类和接口
+ 对于类内部声明的成员：
    + private 意味着只在这个类内部（包含其所有成员）可见；
    + protected—— 和 private一样  在子类中可见。
    + internal —— 能见到类声明的 本模块内 的任何客户端都可见其 internal 成员；
    + public —— 能见到类声明的任何客户端都可见其 public 成员。
+ 在 Kotlin 中，外部类不能访问内部类的 private 成员。

