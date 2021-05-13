package com.example.subscribewebpage

import com.example.subscribewebpage.common.Const
import com.example.subscribewebpage.common.AppDateUtils
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GrammarReference {
    // https://kotlinlang.org/docs/basic-syntax.html
    @Test
    fun gt() {
        println(sum(5, 10))

        /*  ====================================
             val : 최초 단 한번만 할당할 수 있다.
             var : 변수
            ==================================== */
        val a: Int = 1  // immediate assignment
        val b = 2   // `Int` type is inferred (추론)
        val c: Int  // Type required when no initializer is provided
        c = 3       // deferred assignment
        printAll(a, b, c)
        scopefun()

        /*  ====================================
         val? : non-null val
         var? : non-null var
        ==================================== */
        val d = "d"
        var e = "e"
        printAll(d, e)
        printAll(null)

        /*  ====================================
         Array
            [1, 2, 3]
            arrayOf(1, 2, 3)
            arrayOfNulls(1, 2, 3)
        ==================================== */
        val arr0 = arrayOf("1", "2","3").apply {
            this[0] = "5"
            this[1] = "6"
            this[2] = "7"
        }
        val arr1 = intArrayOf(1,2, 3)
        var arr2 = IntArray(3) { 5 }
        var arr3 = IntArray(3) { it + 1 }
        printAll(arr0)
        printAll(arr1)
        printAll(arr2)
        printAll(arr3)
    }

    // https://kotlinlang.org/docs/basic-syntax.html#for-loop
    private fun printAll(vararg args: Any?) {
        if (args is Array) {
            println("===Array===")
            for (arg in args) {

                if (arg !is String){
                    print(arg)
                }else{
                    print(arg as String)
                }
            }
        }else{
            for (arg in args) {
                print(arg)
            }
            for (idx in args.indices){
                print("index : $idx ")
                print("arg : ${args[idx]}\n")
            }
        }
    }

    // nullable
    private fun printAll(vararg args: String?) {
        for (arg in args) {
            print(arg)
        }
        for (idx in args.indices){
            print("index : $idx ")
            print("arg : ${args[idx]}\n")
        }
    }

    // https://kotlinlang.org/docs/basic-syntax.html#functions
    private fun sum(a: Int, b: Int) = a + b

    // Scope functions
    // https://kotlinlang.org/docs/scope-functions.html
    fun scopefun(){
        val name = "kim";
        name.run {
            println("name size : $length")
        }.let {
            println("name size : $it")
        }

        name.let {
            println("name size : $it ${it.length}")
        }
    }

    @Test fun getDate() {
        print(Const.DEBUG_TAG + AppDateUtils.getStringDate())
    }
}