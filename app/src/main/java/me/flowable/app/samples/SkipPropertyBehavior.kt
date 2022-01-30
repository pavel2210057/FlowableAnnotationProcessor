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
