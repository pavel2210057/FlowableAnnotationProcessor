# FlowableAnnotationProcessor

This annotation processor provides an opportunity to automatize work with Kotlin Flows.

# How to work with it?

1. Create a data class and describe its properties.
2. Annotate the class with [@Flowable](https://github.com/pavel2210057/FlowableAnnotationProcessor/blob/main/core/src/main/java/me/flowable/domain/annotation/Flowable.kt)
3. Annotate the class' fields with one of following annotations:
    - [@Shared](https://github.com/pavel2210057/FlowableAnnotationProcessor/blob/main/core/src/main/java/me/flowable/domain/annotation/Shared.kt)
    - [@State](https://github.com/pavel2210057/FlowableAnnotationProcessor/blob/main/core/src/main/java/me/flowable/domain/annotation/State.kt)
    - [@Skip](https://github.com/pavel2210057/FlowableAnnotationProcessor/blob/main/core/src/main/java/me/flowable/domain/annotation/Skip.kt)

You can omit annotating, in this case [Default Behavior](https://github.com/pavel2210057/FlowableAnnotationProcessor/blob/main/app/src/main/java/me/flowable/app/samples/DefaultPropertyBehavior.kt) will be applied.

After compilation, generated classes appear.

Generated classes have a name that consists of the original name and the "Flowable" postfix.

To get more info checkout [samples](https://github.com/pavel2210057/FlowableAnnotationProcessor/tree/main/app/src/main/java/me/flowable/app/samples).

# How does it work?

This processor generates **flowable classes** using the meta info of existing classes.
At the moment, there's one implementation which uses kapt3 to provide Annotation Processing Api.
