package com.arturlasok.maintodo.util

data class FormDataState<out T>(
    val ok: Boolean? = null,
    val error: String? = null,
) {
    companion object {
        fun <T> ok(ok: Boolean): FormDataState<T>
        {
            return FormDataState(ok = ok)
        }
        fun <T> error(message: String) : FormDataState<T>
        {
            return FormDataState(error = message)
        }
    }

}
