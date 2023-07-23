package me.seclerp.rider.extensions.observables

import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.ObservableProperty
import com.intellij.openapi.rd.createLifetime
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IProperty
import com.jetbrains.rd.util.reactive.IPropertyView

class RdObservableProperty<T> (
    private val propertyView: IPropertyView<T>,
    private val propertyLifetime: Lifetime
) : ObservableProperty<T> {
    override fun get() = propertyView.value

    override fun afterChange(parentDisposable: Disposable?, listener: (T) -> Unit) {
        val lifetime = parentDisposable?.createLifetime() ?: propertyLifetime
        propertyView.change.advise(lifetime, listener)
    }
}