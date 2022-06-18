package me.flowable.app.samples

import me.flowable.core.Flowable
import me.flowable.core.Skip

/**
 * will be generated following class:
 *
 *  class SingleSkipPropertyFlowable(
 *      public val prop: String
 *  )
 */
@Flowable
data class SingleSkipProperty(
    @Skip val prop: String
)

open class A {

    open val a = 123

    fun i() = object : A() {

        override val a: Int
            get() = a
    }
}
