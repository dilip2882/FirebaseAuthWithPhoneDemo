package com.dilip.firebaseauthdemo.di

import com.dilip.firebaseauthdemo.services.auth.AuthService
import com.dilip.firebaseauthdemo.services.auth.AuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthServiceModule {

   @Binds
   abstract fun providesFirebaseAuthService(
       authService: AuthServiceImpl
   ): AuthService

}

