package me.flowable.app.samples

import me.flowable.domain.annotation.Flowable
import me.flowable.domain.annotation.Skip
import me.flowable.domain.annotation.State

/**
 * will be generated following class:
 *
 *  class SkipArrayPropertyFlowable(
 *      val prop: Array<String>
 *  )
 */
@Flowable
data class SkipArrayProperty(
    @Skip val prop: Array<String>
)

/**
 * will be generated following class:
 *
 *  class StateArrayPropertyFlowable(
 *      initialPropState: Array<String>
 *  ) {
 *      val propStateFlow: MutableStateFlow<Array<String>> = MutableStateFlow(initialPropState)
 *  }
 */
@Flowable
data class StateArrayProperty(
    @State val prop: Array<String>
)

/**
 * will be generated following class:
 *
 *  class SkipListPropertyFlowable(
 *      val prop: List<String>
 *  )
 */
@Flowable
data class SkipListProperty(
    @Skip val prop: List<String>
)

/**
 * will be generated following class:
 *
 *  class StateListPropertyFlowable(
 *      initialPropState: List<String>
 *  ) {
 *      val propStateFlow: MutableStateFlow<List<String>> = MutableStateFlow(initialPropState)
 *  }
 */
@Flowable
data class StateListProperty(
    @State val prop: List<String>
)
