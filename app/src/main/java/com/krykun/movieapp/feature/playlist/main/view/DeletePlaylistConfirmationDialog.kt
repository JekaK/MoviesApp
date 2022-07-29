package com.krykun.movieapp.feature.playlist.main.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.krykun.domain.model.local.Playlist

@Composable
fun DeletePlaylistConfirmationView(
    onApply: (Long) -> Unit,
    onDismiss: () -> Unit,
    item: Playlist
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }) {

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Delete ${item.name}?"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text("No")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        onClick = {
                            onApply(item.playlistId)
                        }
                    ) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}