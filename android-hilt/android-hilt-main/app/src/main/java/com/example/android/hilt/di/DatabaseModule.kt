package com.example.android.hilt.di

import android.content.Context
import androidx.room.Room
import com.example.android.hilt.data.AppDatabase
import com.example.android.hilt.data.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// 7: HiltModule은 다른 타입의 인스턴스를 어떻게 제공하는지 알려준다. (ex. interface는 어떻게 인스턴스 제공? - builder를 통해 제공)
// 7: Module 어노테이션은 Hilt 모듈임을 알리고,
// InstallIn 어노테이션은 특정 힐트 컴포넌트를 지정하여 바인딩해준다.
// 7: SingletonComponent는 Application과 FragmentComponent는 Fragment와 연관성이 있으므로 생성주기에 맞추에 InstallIn을 진행하면 된다.

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    // 7: Provides 어노테이션은 생성자 주입으로 제공할 수 없는 것들을 제공시켜준다.
    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }

    // 7: AppDatabase는 Room 라이브러리에서 인스턴스화를 하고있어 우리가 생성할 수는 없어 Provides 어노테이션을 통해 제공해야한다.
    // 7: Application 어노테이션은 어디에서는 ApplicationContext를 제공해줄 수 있다.
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "logging.db"
        ).build()
    }
}