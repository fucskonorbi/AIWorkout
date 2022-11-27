package hu.bme.aut.android.aiworkout.ui.views.sign_in

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)