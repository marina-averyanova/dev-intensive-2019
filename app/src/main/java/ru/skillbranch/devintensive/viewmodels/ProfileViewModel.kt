package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ProfileViewModel: ViewModel() {
    private val repository: PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()

    init {
        Log.d("M_ProfileViewModel", "init profile view model")
        profileData.value = repository.getProfile()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("M_ProfileViewModel", "profile model view cleared")
    }

    fun getProfileData(): LiveData<Profile> {
        Log.d("M_ProfileViewModel", "get profile data $profileData")
        return profileData
    }

    fun saveProfileData(profile: Profile) {
        Log.d("M_ProfileViewModel", "profile in view model: $profile")
        repository.saveProfile(profile)
        profileData.value = profile
    }
}