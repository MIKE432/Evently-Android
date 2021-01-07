package com.apusart

import android.app.Application
import android.content.Context
import com.apusart.di.AppModule
import com.apusart.evently_android.guest.initial_activity.InitialActivity
import com.apusart.evently_android.guest.login_activity.LoginActivity
import com.apusart.evently_android.guest.register_activity.RegisterActivity
import com.apusart.evently_android.logged.create_event.CreateEventActivity
import com.apusart.evently_android.logged.profile.ProfileFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun inject(activity: InitialActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: RegisterActivity)
    fun inject(fragment: ProfileFragment)
    fun inject(activity: CreateEventActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: EventlyApplication): Builder

        fun build(): ApplicationComponent
    }
}

class EventlyApplication: Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
    }
}

val Context.appComponent get() = (applicationContext as EventlyApplication).appComponent