package com.example.permissionhandlingwithcompose

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.permissionhandlingwithcompose.ui.theme.PermissionHandlingWithComposeTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

class MainActivity : ComponentActivity() {
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionHandlingWithComposeTheme {

                val permissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS
                    )
                )
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner, effect = {
                    val observer = LifecycleEventObserver{ _, evet ->
                        if(evet ==Lifecycle.Event.ON_START){
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                })

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        permissionState.permissions.forEach {
                            when(it.permission){
                                Manifest.permission.CAMERA->{
                                    OnPermissionResult(
                                        status = it.status,
                                        isGranted = { Text(text = "CAMERA granted") },
                                        shouldShowRationale = {Text(text = "CAMERA should show")},
                                        isPermanentlyDenied = {Text(text = "CAMERA denied")}
                                    )
                                }
                                Manifest.permission.READ_CONTACTS-> {
                                    OnPermissionResult(
                                        status = it.status,
                                        isGranted = { Text(text = "RECORD_AUDIO granted") },
                                        shouldShowRationale = { Text(text = "RECORD_AUDIO should show") },
                                        isPermanentlyDenied = { Text(text = "RECORD_AUDIO denied") }
                                    )
                                }
                            }
                        }
                    }



                }
            }
        }
    }
}

