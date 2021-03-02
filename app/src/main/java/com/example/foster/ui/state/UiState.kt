package com.example.foster.ui.state

import java.lang.Exception

data class UiState<T>(
    val loading: Boolean = false,
    val exception: Exception? = null,
    val data: T? = null
) {

    val hasError: Boolean
        get() = exception != null

    val initialLoad: Boolean
        get() = data == null && loading && !hasError
}