package me.seclerp.rider.extensions.observables

import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.ObservableProperty
import com.intellij.openapi.rd.createLifetime
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IPropertyView

class RdObservableProperty<T>(
    private val property: IPropertyView<T>,
    private val propertyLifetime: Lifetime
) : ObservableProperty<T> {
    override fun get() = property.value

    override fun afterChange(parentDisposable: Disposable?, listener: (T) -> Unit) {
        val lifetime = parentDisposable?.createLifetime() ?: propertyLifetime
        property.change.advise(lifetime, listener)
    }
}