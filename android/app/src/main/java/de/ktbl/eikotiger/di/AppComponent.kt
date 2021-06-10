package de.ktbl.eikotiger.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import de.ktbl.eikotiger.EiKoTiGerApp
import javax.inject.Singleton

/**
 * AppComponent interface that ties injection modules together.
 * It is implemented as an interface that Dagger 2 will use to generate the code necessary to
 * perform the dependency injection
 */
@Singleton
@Component(modules = [ /* Use Android[Support]InjectionModule.class if you're [not] using support library */AndroidSupportInjectionModule::class, AppModule::class, MainActivityModule::class])
interface AppComponent {
    fun inject(testApp: EiKoTiGerApp?)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}