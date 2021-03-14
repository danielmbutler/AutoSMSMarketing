package com.dbtechprojects.autosmsmarketing.util

import android.content.Context
import com.dbtechprojects.autosmsmarketing.db.DatabaseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class AppModule {


    @Provides
    fun provideDB(@ActivityContext context: Context): DatabaseHandler = DatabaseHandler(context)

}