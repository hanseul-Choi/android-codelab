/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hilt

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.example.android.hilt.data.AppDatabase
import com.example.android.hilt.data.LoggerLocalDataSource
import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import com.example.android.hilt.util.DateFormatter

// 3: ServiceLocator는 Application에서 인스턴스화 되어 있다. => 즉 App이 destory되면 영구적으로 있지 않고 삭제된다.
// 3: ServiceLocator는 dependency를 관리하는 클래스이다. : 항상 같은 요청에 같은 인스턴스를 부름
// 3: ServiceLocator는 보일러-플레이트 코드가 존재하고 확장성에도 불리하다. => 대안으로 Hilt
class ServiceLocator(applicationContext: Context) {

    private val logsDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "logging.db"
    ).build()

    // 5: loggerLocalDataSource의 경우, 항상 같은 인스턴스를 제공한다. => 어떻게? Singleton 어노테이션!
    val loggerLocalDataSource = LoggerLocalDataSource(logsDatabase.logDao())

    // 5: DateFormatter의 경우, 항상 다른 인스턴스를 제공한다. => Hilt에서도 해당 방법으로 주입이 가능하다!
    fun provideDateFormatter() = DateFormatter()

    fun provideNavigator(activity: FragmentActivity): AppNavigator {
        return AppNavigatorImpl(activity)
    }
}
