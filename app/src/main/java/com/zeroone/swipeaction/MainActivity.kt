package com.zeroone.swipeaction

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

    Column(
        Modifier
            .verticalScroll(state)
            .background(Color.Gray)
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Todo List 📝",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        HorizontalSwipeAction(
            modifier = Modifier
                .clip(RoundedCornerShape(25)),
            content = {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25))
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.image_1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            },
            leadingContentSize = 60.dp,
            leadingContentBackgroundColor = Color.Blue,
            trailingContentBackgroundColor = Color.Red,
            trailingContentSize = 60.dp,
            leadingContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {

                        }
                        .background(Color.Blue)
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                    }

                    Text(text = "Share")
                }

            },
            trailingContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }

                    Text(text = "Delete")
                }
            },
        )

        Divider()
        Spacer(modifier = Modifier.height(25.dp))
    }
}
