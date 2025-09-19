package com.anastasija.muralmap.ui.pages.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    mapViewModel: MapViewModel = viewModel()
) {
    LaunchedEffect(Unit) { mapViewModel.start() }
    val uiState = mapViewModel.uiState.collectAsState()
    val ui = mapViewModel.uiState.collectAsState()

    val userLatLng = ui.value.latitude?.let { lat ->
        ui.value.longitude?.let { lng -> LatLng(lat, lng) }
    }

    val cameraPositionState = rememberCameraPositionState()


    LaunchedEffect(userLatLng) {
        userLatLng?.let {
            if (cameraPositionState.position.zoom == 0f) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
            } else {
                cameraPositionState.animate(
                    com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(it, 15f)
                )
            }
        }
    }

    if (userLatLng == null) {
        androidx.compose.foundation.layout.Box(Modifier.fillMaxSize())
        return
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = true
        ),
        uiSettings = MapUiSettings(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(state = MarkerState(position = userLatLng), title = "You are here")
    }
}