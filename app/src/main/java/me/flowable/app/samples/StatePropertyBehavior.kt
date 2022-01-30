package me.flowable.app.samples

import me.flowable.core.Flowable
import me.flowable.core.State

/**
 * will be generated following class:
 *
 *  class SingleStatePropertyFlowable(
 *      initialPropState: String
 *  ) {
 *      val propStateFlow: MutableStateFlow<String> = MutableStateFlow(initialPropState)
 *  }
 */
@Flowable
class SingleStateProperty(
    @State val prop: String
)
