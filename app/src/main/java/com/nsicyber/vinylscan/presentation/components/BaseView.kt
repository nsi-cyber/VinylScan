package com.nsicyber.vinylscan.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nsicyber.vinylscan.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BaseView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
    isPageLoading: Boolean = false,
    viewModel: BaseViewModel = BaseViewModel(),
    bottomSheetContent: @Composable () -> Unit = {},
    bottomSheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        ),
    ),
    bottomSheetDismiss: () -> Unit = {},

    ) {
    val popupResult by viewModel.popupResultState.collectAsState()
    var currentPopupEvent by remember { mutableStateOf<PopupEvent?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if (bottomSheetState.bottomSheetState.targetValue == SheetValue.Expanded) {
            keyboardController?.hide()
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent()
                    if (bottomSheetState.bottomSheetState.targetValue == SheetValue.Expanded) {
                        bottomSheetDismiss()
                    }
                }
            }


        },
        scaffoldState = bottomSheetState,
        sheetContent = {
            bottomSheetContent()
        }, sheetPeekHeight = 0.dp, content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {


                    Box(modifier = modifier) {
                        content()

                        PopupHost(
                            popupEvent = currentPopupEvent,
                            onDismiss = { currentPopupEvent = null },
                            viewModel = viewModel
                        )
                        popupResult?.let { result ->
                            if (result.actionTaken) {
                                viewModel.resetPopupResult()
                            }
                        }
                        if (isPageLoading) {
                            LoadingScreen(modifier = Modifier.align(Alignment.Center))
                        }

                    }

                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = FastOutSlowInEasing
                            )
                        )
                ) {

                    if (bottomSheetState.bottomSheetState.targetValue == SheetValue.Expanded) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Gray)
                        )
                    }
                }

            }

        })
}


open class BaseViewModel : ViewModel() {

    private val _popupResultState = MutableStateFlow<PopupResult?>(null)
    val popupResultState: StateFlow<PopupResult?> = _popupResultState.asStateFlow()

    fun showPopup(result: PopupResult) {
        _popupResultState.value = result
    }

    fun resetPopupResult() {
        _popupResultState.value = null
    }

    suspend fun sendPopupEvent(event: PopupEvent) {
        PopupController.sendEvent(event)
    }
}

data class PopupResult(val message: String, val actionTaken: Boolean)


data class PopupEvent(
    val message: String,
    val action: PopupAction? = null
)

data class PopupAction(
    val name: String,
    val action: suspend () -> Unit
)

object PopupController {

    private val _events = Channel<PopupEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: PopupEvent) {
        _events.send(event)
    }
}


data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val name: String,
    val action: suspend () -> Unit
)

object SnackbarController {

    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}


@Composable
fun PopupHost(
    modifier: Modifier = Modifier,
    popupEvent: PopupEvent?,
    onDismiss: () -> Unit,
    viewModel: BaseViewModel
) {
    val scope = rememberCoroutineScope()
    if (popupEvent != null) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = popupEvent.message, color = Color.Black)

                    popupEvent.action?.let { action ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            scope.launch {
                                viewModel.showPopup(PopupResult(popupEvent.message, true))
                                action.action()
                            }
                        }) {
                            Text(text = action.name)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LoadingScreen(modifier: Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.vinyl_anim
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(Color.White)


    ) {

        LottieAnimation(
            composition = preloaderLottieComposition,
            progress = preloaderProgress,
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )

    }

}