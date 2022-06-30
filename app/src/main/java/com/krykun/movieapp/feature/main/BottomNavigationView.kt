package com.krykun.movieapp.feature.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.krykun.movieapp.R
import com.krykun.movieapp.navigation.Screen

@Composable
fun BottomNavigationView(
    navController: NavController,
    items: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorResource(id = R.color.bottom_bar_start),
                        colorResource(id = R.color.bottom_bar_end)
                    )
                )
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach {
            BasicItem(
                title = it.title,
                icon = it.icon,
                onClick = {
                    navController.navigate(it.route)
                },
                isSelected = currentRoute == it.route
            )
        }
    }
}

@Composable
fun BasicItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(30.dp)
            )
            .background(
                if (isSelected) {
                    colorResource(id = R.color.selected_container)
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (!isSelected) {
                    onClick()
                }
            }
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.width(15.dp))
        Icon(
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
            imageVector = icon,
            contentDescription = "",
            tint = if (isSelected) {
                Color.White
            } else {
                colorResource(id = R.color.tint_unselected)
            }
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = title,
                color = Color.White,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
    }
}
