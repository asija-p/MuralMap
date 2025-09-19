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

    // default position (Niš)
    val defaultLatLng = LatLng(43.321445, 21.896104)

    val userLatLng = if (uiState.value.latitude != null && uiState.value.longitude != null) {
        LatLng(uiState.value.latitude!!, uiState.value.longitude!!)
    } else {
        defaultLatLng
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLatLng, 15f)
    }

    LaunchedEffect(userLatLng) {
        cameraPositionState.animate(
            update = com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                userLatLng,
                15f
            )
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(mapType = MapType.NORMAL),
        uiSettings = MapUiSettings(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = userLatLng),
            title = "You are here"
        )
    }
}