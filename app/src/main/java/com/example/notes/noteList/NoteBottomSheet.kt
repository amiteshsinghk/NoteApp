package com.example.notes.noteList

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.OnApplyWindowInsetsListener
import kotlinx.coroutines.launch
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetLayout(
    onDismissRequest: () -> Unit,
    onTitleChange: (value: String) -> Unit,
    onDetailsChange: (value: String) -> Unit,
    onSaveContent: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpened by rememberSaveable { mutableStateOf(false) }
    var titleText by rememberSaveable { mutableStateOf("") }
    var detailText by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var keyboardHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val rootView = LocalContext.current as Activity
    // Detect keyboard visibility and height
    DisposableEffect(Unit) {
        val view = rootView.window.decorView
        val callback = OnApplyWindowInsetsListener { _, insets ->
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            keyboardHeight = with(density) { imeHeight.toDp() }
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(view, callback)
        onDispose { ViewCompat.setOnApplyWindowInsetsListener(view, null) }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = -keyboardHeight)
            .wrapContentHeight()
    ) {
        ModalBottomSheet(
            sheetState = sheetState,
            modifier = Modifier.wrapContentSize(),
            onDismissRequest = {
                onDismissRequest()
                isSheetOpened = false
            },
            dragHandle = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                }
            },
            windowInsets = WindowInsets.ime,

            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF001440))
                    .padding(
                        horizontal = 16.dp,
                        vertical = 20.dp
                    ) // Add internal padding for text alignment
                    .height(IntrinsicSize.Min)
            ) {
                Text(
                    text = "Add a new note",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = titleText,
                onValueChange = {
                    titleText = it
                    onTitleChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                singleLine = false,
                label = { Text(text = "Title") },
                placeholder = { Text(text = "Enter a title...") }
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = detailText,
                onValueChange = {
                    detailText = it
                    onDetailsChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                singleLine = false,
                label = { Text(text = "Content") },
                placeholder = { Text(text = "Enter content...") }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        Log.d("NoteDetailViewModel", "Buttom Click")
                        onSaveContent()
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            scope.launch {
                                if (!sheetState.isVisible) {
                                    sheetState.hide()
                                    isSheetOpened = false
                                    onDismissRequest()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF001440),
                        contentColor = Color.White
                    ),
                    enabled = titleText.isNotEmpty() && detailText.isNotEmpty()
                ) {
                    Text(text = "Save", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(15.dp))

                Button(
                    onClick = {
                        Log.d("NoteDetailViewModel", "Buttom Click")
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            scope.launch {
                                if (!sheetState.isVisible) {
                                    sheetState.hide()
                                    isSheetOpened = false
                                    onDismissRequest()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF001440),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Cancel", fontSize = 16.sp)
                }
            }
        }
    }
}