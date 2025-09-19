package com.anastasija.muralmap.ui.pages.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anastasija.muralmap.data.LocationRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocationRepository
    private var started = false
    private var updatesJob: Job? = null

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState

    init {
        val fusedClient = LocationServices.getFusedLocationProviderClient(application)
        repository = LocationRepository(application, fusedClient)
    }


    fun start() {
        if (started) return
        started = true

        updatesJob = viewModelScope.launch {

            repository.getLocationUpdates()
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { location ->
                    _uiState.update {
                        it.copy(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            error = null
                        )
                    }
                }
        }
    }

    override fun onCleared() {
        updatesJob?.cancel()
        super.onCleared()
    }
}