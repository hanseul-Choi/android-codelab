package com.example.android.codelabs.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.math.max

private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLS = 3_000L

private val firstArticleCreatedTime = LocalDateTime.now()

class ArticlePagingSource : PagingSource<Int, Article>() {

    // 사용자 스크롤 시 표시할 데이터를 가져오는 작업
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        // 첫 load의 LoadParams.key = null
        // 더이상 데이터가 없는 경우, nextKey나 prevKey가 null이다.
        val start = params.key ?: STARTING_KEY
        val range = start.until(start + params.loadSize)

        // 일부러 지연시간을 줘서 확인
        if(start != STARTING_KEY) delay(LOAD_DELAY_MILLS)

        /*
             return 되는 LoadResult
             LoadResult.Page : 성공
                - data : 항목의 list
                - prevKey : 앞의 항목을 가져오는 경우 사용해야하는 key
                - nextKey : 뒤의 항목을 가져오는 경우 사용해야하는 key
             LoadResult.Error : 오류
             LoadResult.Invalid : PagingSource 무결성 보장 불가(무효)
         */
        return LoadResult.Page(
            data = range.map { number ->
                Article(
                    id = number,
                    title = "Article $number",
                    description = "This describes article $number",
                    created = firstArticleCreatedTime.minusDays(number.toLong())
                )
            },
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },
            nextKey = range.last + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? { // UI 관련 항목을 새로고침할 때 호출
        val anchorPosition = state.anchorPosition ?: return null // 지금까지 있던 데이터까지의 양을 그대로 기본 데이터와 바꾸기 위해 현재 위치를 확인
        val article = state.closestItemToPosition(anchorPosition) ?: return null // 가장 가까운 키를 가져와 loadKey로 사용

        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    // 페이징 키 유효성 확인 : 여기서는 id값이 1부터 오르기 때문에 STARTING_KEY값보다 작은 경우가 없음
    /*
         무효화? PagingSource의 기본 데이터가 변경되어 모든 데이터를 업데이트해야하는 상황
            - PagingAdapter에서 refresh를 호출하는 경우
            - PagingSource에서 invalidate를 호출하는 경우
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}