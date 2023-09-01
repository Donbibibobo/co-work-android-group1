package app.appworks.school.stylish.catalog.item.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.LazyPagingItems
import app.appworks.school.stylish.data.Product
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication


@Composable
fun CatalogItemScreen(products: LazyPagingItems<Product>, onClick: (Product) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(vertical = 8.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            products.itemCount
        ) { index ->
            products[index]?.let {
                GridCatalogItem(it, onClick)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GridCatalogItem(product: Product, onClick: (Product) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick.invoke(product)
            }
    ) {
        val (image, title, price) = createRefs()
        GlideImage(
            model = product.mainImage,
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.matchParent
                }
                .aspectRatio(ratio = 5f / 7f),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        ) {
            it.placeholder(R.drawable.ic_placeholder)
        }

        Text(text = product.title,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(image.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
            color = colorResource(id = R.color.black_3f3a3a),
            fontSize = 15.sp,
            letterSpacing = 0.15f.sp,
            maxLines = 2,
//            fontFamily = FontFamily(Font(R.font.noto_sans)),
            overflow = TextOverflow.Ellipsis
        )

        Text(text = StylishApplication.instance.getString(R.string.nt_dollars_, product.price),
            modifier = Modifier
                .constrainAs(price) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    start.linkTo(title.start)
                    end.linkTo(title.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
            color = colorResource(id = R.color.gray_646464),
            fontSize = 15.sp,
            letterSpacing = 0.15f.sp,
            maxLines = 1,
//            fontFamily = FontFamily(Font(R.font.noto_sans)),
            overflow = TextOverflow.Ellipsis
        )
    }
}