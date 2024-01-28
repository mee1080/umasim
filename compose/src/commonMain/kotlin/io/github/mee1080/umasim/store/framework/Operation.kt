package io.github.mee1080.umasim.store.framework


import io.github.mee1080.umasim.store.AppContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlin.reflect.KClass

/**
 * Operation
 *
 * 画面でのユーザ操作に対する一連の処理を実装する。
 */
sealed interface Operation<S>

typealias Reducer<S> = (S) -> S

operator fun <S> Reducer<S>.plus(other: Reducer<S>): Reducer<S> = {
    other(this(it))
}

class ActionContext<S>(appContext: AppContext, collector: FlowCollector<Reducer<S>>) : AppContext by appContext,
    FlowCollector<Reducer<S>> by collector

/**
 * 二重起動時の処理
 */
sealed interface OnRunning {

    open class Tag

    val tag: Tag?

    val cancelOnStartTags: List<Tag>

    val cancelOnStartClasses: List<KClass<*>>

    /**
     * 並列で処理を実行する
     */
    class Parallel(
        override val cancelOnStartTags: List<Tag> = emptyList(),
        override val cancelOnStartClasses: List<KClass<out Tag>> = emptyList(),
    ) : OnRunning {
        override val tag = null
    }

    /**
     * 実行中の処理をキャンセルし、新たに実行する
     */
    class CancelAndRun(
        override val tag: Tag,
        override val cancelOnStartTags: List<Tag> = emptyList(),
        override val cancelOnStartClasses: List<KClass<out Tag>> = emptyList(),
    ) : OnRunning

    /**
     * 実行中の処理を継続し、新たに実行しない
     */
    class Ignore(
        override val tag: Tag,
        override val cancelOnStartTags: List<Tag> = emptyList(),
        override val cancelOnStartClasses: List<KClass<out Tag>> = emptyList(),
    ) : OnRunning

    companion object {
        val parallel = Parallel()
    }
}

/**
 * AsyncOperation
 *
 * Operationの中で、非同期処理を行うもの。
 * 処理開始時のStateを引数で受け取る。
 * 処理結果は、その時点のStateを新たなStateに変換するラムダ式を返す。
 * 処理結果の反映は並列で行われないよう、StateHolderで制御される。
 */
interface AsyncOperation<S> : Operation<S> {

    /**
     * 二重起動時の処理
     */
    val onRunningPolicy: OnRunning

    suspend fun execute(appContext: AppContext, state: S): Flow<Reducer<S>>
}

private class AsyncOperationImpl<S>(
    private val action: suspend ActionContext<S>.(S) -> Unit,
    override val onRunningPolicy: OnRunning,
) : AsyncOperation<S> {

    override suspend fun execute(appContext: AppContext, state: S) = flow<Reducer<S>> {
        ActionContext(appContext, this).action(state)
    }
}

fun <S> AsyncOperation(
    action: suspend ActionContext<S>.(S) -> Unit,
    onRunningPolicy: OnRunning,
): AsyncOperation<S> = AsyncOperationImpl(action, onRunningPolicy)

/**
 * DirectOperation
 *
 * Operationの中で、非同期処理が不要なもの。
 * Stateを引数として、新たなStateを返す。
 * 処理が並列で行われないよう、StateHolderで制御される。
 */
interface DirectOperation<S> : Operation<S> {
    fun reduce(state: S): S
}

private class DirectOperationImpl<S>(
    private val action: Reducer<S>,
) : DirectOperation<S> {
    override fun reduce(state: S) = action(state)
}

fun <S> DirectOperation(action: Reducer<S>): DirectOperation<S> = DirectOperationImpl(action)
