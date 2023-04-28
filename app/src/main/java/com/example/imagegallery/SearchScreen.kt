package com.example.imagegallery

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.imagegallery.modals.SinglePhotoX
import com.example.imagegallery.widgets.LoadingItem
import com.example.imagegallery.widgets.SoloImage

@Composable
fun SearchScreen(viewModal: ImageViewModal, ctx: Context){
    Column {
        SearchBar(viewModal)
        SearchImageList(viewModel = viewModal, ctx)
    }

}

@Composable
fun SearchBar(viewModal: ImageViewModal) {
    val state = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val name = remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                width = 1.dp, color = MaterialTheme.colorScheme.onBackground, shape = CircleShape
            )
    ) {
        BasicTextField(value = state.value,
            onValueChange = {
                state.value = it
                viewModal.searchImage(state.value.text)

            },
            modifier = Modifier
                .heightIn(min = 45.dp)
                .testTag("search_bar")
                .fillMaxWidth(),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            maxLines = 1,
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                    )
                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.value == TextFieldValue("")) Text(
                            text = "SearchImage",
                            fontWeight = FontWeight.ExtraLight,
                        )
                        innerTextField()
                    }
                    if (state.value != TextFieldValue("")) {
                        Icon(imageVector = Icons.Default.Close,
                            contentDescription = "clear",
                            modifier = Modifier
                                .clickable {
                                       state.value = TextFieldValue("")
                                    viewModal.searchImage(state.value.text)

                                }
                                .testTag("clear_search"))
                    }
                }
            })
    }
}

@Composable
fun SearchImageList(viewModel: ImageViewModal, ctx: Context){
    // val imgData: LazyPagingItems<SinglePhotoX> = viewModel.imageSearchPager.collectAsLazyPagingItems()
    val images = remember {
        mutableStateListOf<SinglePhotoX>()
    }
    val isLoading = remember {
        mutableStateOf(false)
    }
    viewModel.imageData.observe(ctx as MainActivity, Observer {
        when(it){
            is NetWorkResult.Loading -> {
               isLoading.value = true
            }
            is NetWorkResult.Error -> {
                Toast.makeText(ctx, "Error image", Toast.LENGTH_SHORT).show()
            }
            is NetWorkResult.Success -> {
                println( "kanti response ${it.data?.photos?.photo.toString()}")
                isLoading.value = false
                it.data?.photos?.photo.let { it1 ->
                    images.clear()
                    if (it1 != null) {
                        images.addAll(it1)
                    }
                }
            }
        }
    })

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 100.dp)) {
        items(images) { item ->
            SoloImage(data = item)
        }
    }
    if (isLoading.value)
        LoadingItem()

}