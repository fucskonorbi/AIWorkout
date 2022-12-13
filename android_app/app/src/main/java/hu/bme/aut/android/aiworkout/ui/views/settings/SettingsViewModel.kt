package hu.bme.aut.android.aiworkout.ui.views.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val sharedPreferences: SharedPreferences
): ViewModel(){

    val editor = sharedPreferences.edit();

}