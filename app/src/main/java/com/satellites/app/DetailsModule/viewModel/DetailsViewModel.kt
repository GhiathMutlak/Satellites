package com.satellites.app.DetailsModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.satellites.app.entities.Details
import com.satellites.app.entities.Position
import com.satellites.app.entities.SatelliteDetails
import com.satellites.app.repository.SatelliteRepository
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: SatelliteRepository) : ViewModel() {

    fun getSatelliteDetails(id: Int): List<SatelliteDetails> {
        return repository.getSatelliteDetails(id)
    }

    fun getDetailsBySatelliteId(id: Int): Details? {
        return repository.getDetailsBySatelliteId(id)
    }

    fun getPositionsBySatelliteId(id: Int): List<Position> {
        return repository.getAllPositionsBySatelliteId(id)
    }


    fun insertDetails(details: Details) = viewModelScope.launch {
        repository.insertDetails(details)
    }

    fun insertPosition(details: Position) = viewModelScope.launch {
        repository.insertPosition(details)
    }


    class DetailsViewModelFactory(private val repository: SatelliteRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}