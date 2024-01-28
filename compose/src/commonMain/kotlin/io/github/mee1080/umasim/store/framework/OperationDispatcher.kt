package io.github.mee1080.umasim.store.framework

import io.github.mee1080.umasim.store.AppContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface OperationDispatcher<T> {

    companion object {
        fun <S> empty() = object : OperationDispatcher<S> {
            override operator fun invoke(operation: Operation<S>) {}
            override operator fun invoke(reducer: Reducer<S>) {}
        }
    }

    operator fun invoke(operation: Operation<T>)
    operator fun invoke(reducer: Reducer<T>)
}

open class OperationDispatcherImpl<S : State>(
    private val stateHolder: StateHolder<S>,
    private val scope: CoroutineScope,
) : OperationDispatcher<S> {

    override operator fun invoke(operation: Operation<S>) {
        scope.launch {
            stateHolder.send(operation)
        }
    }

    override operator fun invoke(reducer: Reducer<S>) {
        invoke(DirectOperation(reducer))
    }
}

fun <S : State> OperationDispatcher(
    stateHolder: StateHolder<S>,
    scope: CoroutineScope,
): OperationDispatcher<S> = OperationDispatcherImpl(stateHolder, scope)

class MultipleOperationDispatcher<Main : State, Sub>(
    private val stateHolder: StateHolder<Main>,
    private val scope: CoroutineScope,
    private val getChild: (Main) -> Sub,
    private val setChild: Main.(Sub) -> Main,
) : OperationDispatcherImpl<Main>(stateHolder, scope) {

    fun sub(operation: Operation<Sub>) {
        scope.launch {
            stateHolder.send(wrapChildOperation(operation))
        }
    }

    fun sub(reducer: Reducer<Sub>) {
        sub(DirectOperation(reducer))
    }

    private fun wrapChildOperation(operation: Operation<Sub>): Operation<Main> {
        return when (operation) {
            is DirectOperation -> DirectOperationMapper(operation)
            is AsyncOperation -> AsyncOperationMapper(operation)
        }
    }

    private inner class DirectOperationMapper(
        private val operation: DirectOperation<Sub>,
    ) : DirectOperation<Main> {

        override fun reduce(state: Main): Main {
            return setChild(state, operation.reduce(getChild(state)))
        }
    }

    private inner class AsyncOperationMapper(
        private val operation: AsyncOperation<Sub>,
    ) : AsyncOperation<Main> {

        override suspend fun execute(appContext: AppContext, state: Main): Flow<Reducer<Main>> {
            return operation.execute(appContext, getChild(state)).map { reducer ->
                { parent ->
                    parent.setChild(reducer.invoke(getChild(parent)))
                }
            }
        }

        override val onRunningPolicy = operation.onRunningPolicy
    }
}
