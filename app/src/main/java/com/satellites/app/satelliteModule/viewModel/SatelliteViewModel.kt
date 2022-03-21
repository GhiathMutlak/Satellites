package com.satellites.app.satelliteModule.viewModel

import androidx.lifecycle.*
import com.satellites.app.entities.Satellite
import com.satellites.app.repository.SatelliteRepository
import kotlinx.coroutines.launch

class SatelliteViewModel(private val repository: SatelliteRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allSatellite: LiveData<List<Satellite>> = repository.allSatellites.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertSatellite(satellite: Satellite) = viewModelScope.launch {
        repository.insertSatellite(satellite)
    }

    fun delete(satellite: Satellite) = viewModelScope.launch {
        repository.deleteSatellite(satellite)
    }

}

class SatelliteViewModelFactory(private val repository: SatelliteRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SatelliteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SatelliteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}