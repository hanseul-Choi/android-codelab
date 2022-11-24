/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.example.background

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.BlurMaskFilter.Blur
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.background.workers.BlurWorker
import com.example.background.workers.CleanupWorker
import com.example.background.workers.SaveImageToFileWorker


class BlurViewModel(application: Application) : ViewModel() {

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    init {
        imageUri = getImageUri(application.applicationContext)
    }
    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */

    private val workManager = WorkManager.getInstance(application)

    internal fun applyBlur(blurLevel: Int) {
//        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
//            .setInputData(createInputDataForUri())
//            .build()
//
//        workManager.enqueue(blurRequest) // WorkRequest를 만들어 큐에 추가합니다.

        /*
            UniqueWork(고유 작업 체인)이 필요한 경우?
            - 앱이 네트워크에 데이터를 동기화해야하는 경우, 'sync' Name의 시퀀스를 큐에 넣는다.
            'sync'가 이미 큐에 있는 경우, 지금 들어오는 'sync'작업을 무시(KEEP)하여 작업을 진행시킬 수 있다.
            - 사진 편집 앱에서 작업을 취소하고 다음 작업을 사용자가 눌러놓은 상태라면,
            앱에서 'undo' 체인을 제작하고 이에 맞게 교체(REPLACE), 무시(KEEP), 추가(APPEND)하여 작업시킬 수 있다.
         */
        var continuation = workManager
            .beginUniqueWork( // 이미지가 완료되기 전에 다른 이미지 블러 처리를 위해 beginUniqueWork 작업 수행
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            ) // WorkMangager의 Chain작업, CleanUpWorker부터 진행

        for(i in 0 until blurLevel) { // CleanupWorker를 진행 후에 blurRequest 진행
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

            if(i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build()) // blur level에 따라 blur 횟수 증가
        }

        val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java).build()

        continuation = continuation.then(save) // blurRequest 진행 후에 save 진행

        continuation.enqueue() // Workmanager 실행 작업
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        val imageUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
            .build()

        return imageUri
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }

    private fun createInputDataForUri(): Data { // Data는 키-값의 WorkRequest를 위한 자료구조입니다.
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }

        return builder.build()
    }

    class BlurViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(BlurViewModel::class.java)) {
                BlurViewModel(application) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
