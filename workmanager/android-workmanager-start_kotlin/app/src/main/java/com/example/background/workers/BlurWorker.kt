package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
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

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurred Imager", appContext)

        sleep() // 3초간 멈추는 작업

        return try {
//            // error : blur 적용 안되는 이슈 -> 높은 해상도 때문에
//            val sizeDownOption = BitmapFactory.Options()
//            sizeDownOption.inSampleSize = 2
//
//            // 먼저 cupcake 이미지 Bitmap을 가져옵니다.
//            val picture = BitmapFactory.decodeResource(
//                appContext.resources,
//                R.drawable.android_cupcake,
//                sizeDownOption
//            )
//
//            // bitmap 파일을 저장소에 저장합니다.
//            val uri = writeBitmapToFile(appContext, blurBitmap(picture, appContext))
//
//            makeStatusNotification("Blurred Uri is $uri", appContext)

            if(TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )

            val output = blurBitmap(picture, appContext)

            val outputUri = writeBitmapToFile(appContext, output) // 출력 값을 Uri로 변환

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            // 성공했음을 리턴합니다.
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")

            // 에러가 나왔기 때문에 실패를 리턴합니다.
            Result.failure()
        }
    }
}