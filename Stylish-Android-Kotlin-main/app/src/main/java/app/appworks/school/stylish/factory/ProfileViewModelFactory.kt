package app.appworks.school.stylish.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.profile.ProfileViewModel

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Factory for all ViewModels which need [User].
 */
@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val stylishRepository: StylishRepository,
    private val user: User?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(stylishRepository, user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
