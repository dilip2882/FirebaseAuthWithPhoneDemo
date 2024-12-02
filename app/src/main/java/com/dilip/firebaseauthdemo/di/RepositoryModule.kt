package com.programminSimplified.firebaseproject.di

import com.dilip.firebaseauthdemo.features.repository.AuthRepository
import com.dilip.firebaseauthdemo.features.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

   @Binds
   abstract fun providesFirebaseAuthRepository(
       repo: AuthRepositoryImpl
   ): AuthRepository

}

