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

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// 4: HiltAndroidApp 어노테이션은 Hilt의 베이스 코드들을 생성시켜준다. 또한, Parent Container의 역할로
// 최상단에서 의존성을 제공해주는 역할을 한다. => 즉, 모든 Container들이 이 클래스에 접근이 가능하다.
@HiltAndroidApp
class LogApplication : Application() {

    lateinit var serviceLocator: ServiceLocator

    override fun onCreate() {
        super.onCreate()
        serviceLocator = ServiceLocator(applicationContext)
    }
}
