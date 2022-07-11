package com.example.android.hilt.di

import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


// 8: NavigationModule은 기존 DatabaseModule에 넣지않고 새로만들었다. 이유는? database의 네이밍과 맞지 않고,
// DatabaseModule은 Application 연관성을 가지지만, NavgationModule의 경우 Activity의 연관성을 가지고 있고,
// Hilt 모듈에 Provides와 Binds를 같이 사용할 수 없기 때문에 새로 만들었다. (추상표현과 구현된 것은 같이 사용할 수 없다!)

// 8: Provides는 클래스의 인스턴스를 제공하고, Binds는 인터페이스의 인스턴스를 제공한다.

@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {

    // 8: AppNavigatorImpl을 통해 AppNavigator Interface를 구현함
    @Binds
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}