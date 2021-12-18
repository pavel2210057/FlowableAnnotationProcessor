package me.flowable.app.samples

import me.flowable.domain.annotation.Flowable
import me.flowable.domain.annotation.Skip

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
