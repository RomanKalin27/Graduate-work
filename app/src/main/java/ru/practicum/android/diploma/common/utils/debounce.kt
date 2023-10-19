package ru.practicum.android.diploma.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    cancelPrevious: Boolean, // переименован useLastParam
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    val lock = Any() // объект для синхронизации

    return { param: T ->
        synchronized(lock) {
            if (cancelPrevious) {
                debounceJob?.cancel()
            }
            if (debounceJob?.isCompleted != false || cancelPrevious) {
                debounceJob = coroutineScope.launch {
                    delay(delayMillis)
                    try {
                        action(param)
                    } catch (e: Exception) {
                        // Обрабатываем ошибку, например, логируем или "тостим"
                    }
                }
            }
        }
    }
}