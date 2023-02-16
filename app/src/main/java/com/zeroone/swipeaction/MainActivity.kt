package com.zeroone.swipeaction

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroone.swipeaction.swipe_action.HorizontalSwipeAction
import com.zeroone.swipeaction.ui.theme.SwipeActionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeActionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    val state = rememberScrollState()

    val items = listOf("This is first title")
    Column(
        Modifier
            .verticalScroll(state)
            .background(Color.Gray)
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Todo List 📝",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))



        HorizontalSwipeAction(
            modifier = Modifier.clip(RoundedCornerShape(25)),
            content = { ItemContent(title = items[0]) },
            leadingContent = { LeadingContent() },
            trailingContent = { TrailingContent2() },
            )

        Divider()
        Spacer(modifier = Modifier.height(25.dp))


        HorizontalSwipeAction(
            modifier = Modifier.clip(CircleShape),
            trailingContentThresholds = 25.dp,
            leadingContentThresholds = 25.dp,
            trailingContentBackgroundColor = Color.Red,
            leadingContentBackgroundColor = Color.Blue,
            trailingContent = { TrailingContent2() },
            leadingContent = { LeadingContent() },
            content = { ItemContent2() }
        )

    }
}


@Composable
fun ItemContent(title: String) {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = null,
            tint = Color.Green
        )

        Button(onClick = { Log.d("SwipeActionTag", "ItemContent: Clicked") }) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

    }
}

@Composable
fun ItemContent2() {
    Row(
        modifier = Modifier
            .background(Color.Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Person,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier
                .height(150.dp)
                .width(200.dp)
        )
    }
}



@Composable
fun LeadingContent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Green),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
        // .fillMaxHeight(),
    ) {
        Button(onClick = {
            Log.d("SwipeActionTag", "TrailingContent:Clicked ")
        }) { Text(text = "Search") }
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    }
}


@Composable
fun TrailingContent2() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Delete")
        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
    }
}