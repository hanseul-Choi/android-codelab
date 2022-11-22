package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R

/**
 *  Worker : 백그라운드에서 실행하고자 하는 작업, doWork()에서 재정의하여 구현합니다.
 *  WorkRequest : Worker를 전달하여 작업 실행 요청을 진행합니다. 제약조건이 지정될 수 있습니다.
 *  WorkManager : 실제로 WorkRequest를 예약하고 실행합니다.
 */

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val appContext = applicationContext

        makeStatusNotification("Blurred Imager", appContext)

        return try {
            // 먼저 cupcake 이미지 Bitmap을 가져옵니다.
            val picture = BitmapFactory.decodeResource(
                appContext.resources,
                R.drawable.android_cupcake
            )

            // bitmap 파일을 저장소에 저장합니다.
            val uri = writeBitmapToFile(appContext, blurBitmap(picture, appContext))

            makeStatusNotification("Blurred Uri is $uri", appContext)

            // 성공했음을 리턴합니다.
            Result.success()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")

            // 에러가 나왔기 때문에 실패를 리턴합니다.
            Result.failure()
        }
    }
}