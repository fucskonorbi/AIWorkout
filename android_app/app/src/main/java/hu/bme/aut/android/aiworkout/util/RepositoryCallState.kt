package hu.bme.aut.android.aiworkout.util

data class RepositoryCallState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
