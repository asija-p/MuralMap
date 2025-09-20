package com.anastasija.muralmap.ui.pages.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    mapViewModel: MapViewModel = viewModel()
) {
    LaunchedEffect(Unit) { mapViewModel.start() }

    val ui by mapViewModel.uiState.collectAsState()

    val userLatLng: LatLng? = ui.latitude?.let { lat ->
        ui.longitude?.let { lng -> LatLng(lat, lng) }
    }

    var mapLoaded by rememberSaveable { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(userLatLng) {
        userLatLng?.let {
            if (cameraPositionState.position.zoom == 0f) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
            } else {
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = true
            ),
            uiSettings = MapUiSettings(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { mapLoaded = true }
        ) {
            userLatLng?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "You are here"
                )
            }
        }

        val showLoading = userLatLng == null || !mapLoaded
        if (showLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = if (userLatLng == null) "Please turn on Location to show your position."
                        else "Loading map…",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}