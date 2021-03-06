package com.apusart.di

import android.app.Application
import android.content.Context
import com.apusart.api.local_data_source.db.EventlyDatabase
import com.apusart.api.remote_data_source.IEventlyService
import com.apusart.evently_android.R
import com.apusart.tools.Defaults
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.storage.FirebaseStorage
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideGson(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {
            level =  HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRoomDatabase(): EventlyDatabase {
        return EventlyDatabase.db
    }

    @Singleton
    @Provides
    fun provideNormalRetrofitService(client: OkHttpClient, gsonCorverterFactory: GsonConverterFactory): IEventlyService {

        return Retrofit.Builder()
            .baseUrl(Defaults.baseUrl)
            .addConverterFactory(gsonCorverterFactory)
            .client(client)
            .build()
            .create(IEventlyService::class.java)
    }
}