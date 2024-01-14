package io.github.mee1080.umasim.store.framework

import io.github.mee1080.umasim.store.AppContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

/**
 * マーカーインタフェース
 */
interface State

/**
 * StateHolder
 *
 * Stateの保持と、並列処理の制御を行う。
 */
class StateHolder<S : State>(
    private val appContext: AppContext,
    mainDispatcher: CoroutineDispatcher,
    asyncDispatcher: CoroutineDispatcher,
    initialState: S,
) {

    private val _state = MutableStateFlow(initialState)
    val state = _state as StateFlow<S>

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    private val mainCoroutineContext = mainDispatcher + SupervisorJob() + exceptionHandler

    private val asyncCoroutineContext = asyncDispatcher + exceptionHandler

    private val jobs = mutableMapOf<OnRunning.Tag, Job>()

    suspend fun send(operation: Operation<S>) {
        withContext(mainCoroutineContext) {
            when (operation) {
                is DirectOperation -> {
                    _state.emit(operation.reduce(state.value))
                }

                is AsyncOperation -> {
                    var context: CoroutineContext = asyncCoroutineContext
                    val onRunning = operation.onRunningPolicy
                    val tag = onRunning.tag
                    when (onRunning) {
                        is OnRunning.CancelAndRun -> jobs[tag]?.cancel()
                        is OnRunning.Ignore -> if (jobs[tag] != null) return@withContext
                        is OnRunning.Parallel -> {}
                    }
                    if (tag != null) {
                        val job = Job()
                        jobs[tag] = job
                        context += job
                    }
                    onRunning.cancelOnStartTags.forEach {
                        jobs.remove(it)?.cancel()
                    }
                    onRunning.cancelOnStartClasses.forEach { tagClass ->
                        val tags = jobs.filterKeys { tagClass.isInstance(it) }
                        tags.forEach {
                            jobs.remove(it.key)?.cancel()
                        }
                    }
                    withContext(context) {
                        operation.execute(appContext, state.value).collect {
                            withContext(mainCoroutineContext) {
                                _state.emit(it(state.value))
                            }
                        }
                        if (tag != null) {
                            withContext(mainCoroutineContext) {
                                jobs.remove(tag)
                            }
                        }
                    }
                }
            }
        }
    }
}