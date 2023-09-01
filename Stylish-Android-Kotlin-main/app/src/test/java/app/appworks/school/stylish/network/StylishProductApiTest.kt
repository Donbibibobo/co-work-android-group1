package app.appworks.school.stylish.network

import app.appworks.school.stylish.data.Product
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class StylishProductApiTest : StylishApiServiceTest() {

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_product_women_get() {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(
            mockResponse.setBody(rawWomen)
        )

        runBlocking {
            val productResult = service.getProductList(type = "women", paging = "0")
            val products = productResult.products
            assertTrue(products != null)
            assertEquals(6, products!!.size)
            assertProducts(products.first())
            assertImages(products.first())
            assertVariants(products.first())
            assertColors(products.first())
            assertSizes(products.first())
            assertEquals("1", productResult.paging)
        }
    }

    private fun assertProducts(product: Product) {
        assertEquals(201807201824, product.id)

        //TODO: missing category
        assertEquals("前開衩扭結洋裝", product.title)
        assertEquals("厚薄：薄\r\n彈性：無", product.description)
        assertEquals(799, product.price)
        assertEquals("棉 100%", product.texture)
        assertEquals("手洗，溫水", product.wash)
        assertEquals("中國", product.place)
        assertEquals("實品顏色依單品照為主", product.note)
        assertEquals(
            "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
            product.story
        )
        assertEquals(
            "https://api.appworks-school.tw/assets/201807201824/main.jpg",
            product.mainImage
        )
    }

    private fun assertImages(product: Product) {
        assertEquals(4, product.images.size)
        assertEquals("https://api.appworks-school.tw/assets/201807201824/0.jpg", product.images[0])
        assertEquals("https://api.appworks-school.tw/assets/201807201824/1.jpg", product.images[1])
        assertEquals("https://api.appworks-school.tw/assets/201807201824/0.jpg", product.images[2])
        assertEquals("https://api.appworks-school.tw/assets/201807201824/1.jpg", product.images[3])
    }

    private fun assertVariants(product: Product) {
        assertEquals(9, product.variants.size)
        assertEquals("FFFFFF", product.variants[0].colorCode)
        assertEquals("S", product.variants[0].size)
        assertEquals(2, product.variants[0].stock)

        assertEquals("FFFFFF", product.variants[1].colorCode)
        assertEquals("M", product.variants[1].size)
        assertEquals(1, product.variants[1].stock)

        assertEquals("FFFFFF", product.variants[2].colorCode)
        assertEquals("L", product.variants[2].size)
        assertEquals(2, product.variants[2].stock)

        assertEquals("DDFFBB", product.variants[3].colorCode)
        assertEquals("S", product.variants[3].size)
        assertEquals(9, product.variants[3].stock)

        assertEquals("DDFFBB", product.variants[4].colorCode)
        assertEquals("M", product.variants[4].size)
        assertEquals(0, product.variants[4].stock)

        assertEquals("DDFFBB", product.variants[5].colorCode)
        assertEquals("L", product.variants[5].size)
        assertEquals(5, product.variants[5].stock)

        assertEquals("CCCCCC", product.variants[6].colorCode)
        assertEquals("S", product.variants[6].size)
        assertEquals(8, product.variants[6].stock)

        assertEquals("CCCCCC", product.variants[7].colorCode)
        assertEquals("M", product.variants[7].size)
        assertEquals(5, product.variants[7].stock)

        assertEquals("CCCCCC", product.variants[8].colorCode)
        assertEquals("L", product.variants[8].size)
        assertEquals(9, product.variants[8].stock)
    }

    private fun assertColors(product: Product) {
        assertEquals(3, product.colors.size)
        assertEquals("FFFFFF", product.colors[0].code)
        assertEquals("白色", product.colors[0].name)

        assertEquals("DDFFBB", product.colors[1].code)
        assertEquals("亮綠", product.colors[1].name)

        assertEquals("CCCCCC", product.colors[2].code)
        assertEquals("淺灰", product.colors[2].name)

    }

    private fun assertSizes(product: Product) {
        assertEquals(3, product.sizes.size)
        assertEquals("S", product.sizes[0])
        assertEquals("M", product.sizes[1])
        assertEquals("L", product.sizes[2])
    }

    private val rawWomen = """
        {
          "data": [
            {
              "id": 201807201824,
              "category": "women",
              "title": "前開衩扭結洋裝",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 799,
              "texture": "棉 100%",
              "wash": "手洗，溫水",
              "place": "中國",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201807201824/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201807201824/0.jpg",
                "https://api.appworks-school.tw/assets/201807201824/1.jpg",
                "https://api.appworks-school.tw/assets/201807201824/0.jpg",
                "https://api.appworks-school.tw/assets/201807201824/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "FFFFFF",
                  "size": "S",
                  "stock": 2
                },
                {
                  "color_code": "FFFFFF",
                  "size": "M",
                  "stock": 1
                },
                {
                  "color_code": "FFFFFF",
                  "size": "L",
                  "stock": 2
                },
                {
                  "color_code": "DDFFBB",
                  "size": "S",
                  "stock": 9
                },
                {
                  "color_code": "DDFFBB",
                  "size": "M",
                  "stock": 0
                },
                {
                  "color_code": "DDFFBB",
                  "size": "L",
                  "stock": 5
                },
                {
                  "color_code": "CCCCCC",
                  "size": "S",
                  "stock": 8
                },
                {
                  "color_code": "CCCCCC",
                  "size": "M",
                  "stock": 5
                },
                {
                  "color_code": "CCCCCC",
                  "size": "L",
                  "stock": 9
                }
              ],
              "colors": [
                {
                  "code": "FFFFFF",
                  "name": "白色"
                },
                {
                  "code": "DDFFBB",
                  "name": "亮綠"
                },
                {
                  "code": "CCCCCC",
                  "name": "淺灰"
                }
              ],
              "sizes": [
                "S",
                "M",
                "L"
              ]
            },
            {
              "id": 201807202140,
              "category": "women",
              "title": "透肌澎澎防曬襯衫",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 599,
              "texture": "棉 100%",
              "wash": "手洗，溫水",
              "place": "中國",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201807202140/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201807202140/0.jpg",
                "https://api.appworks-school.tw/assets/201807202140/1.jpg",
                "https://api.appworks-school.tw/assets/201807202140/0.jpg",
                "https://api.appworks-school.tw/assets/201807202140/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "DDFFBB",
                  "size": "S",
                  "stock": 7
                },
                {
                  "color_code": "DDFFBB",
                  "size": "M",
                  "stock": 5
                },
                {
                  "color_code": "DDFFBB",
                  "size": "L",
                  "stock": 8
                },
                {
                  "color_code": "CCCCCC",
                  "size": "S",
                  "stock": 1
                },
                {
                  "color_code": "CCCCCC",
                  "size": "M",
                  "stock": 6
                },
                {
                  "color_code": "CCCCCC",
                  "size": "L",
                  "stock": 2
                }
              ],
              "colors": [
                {
                  "code": "DDFFBB",
                  "name": "亮綠"
                },
                {
                  "code": "CCCCCC",
                  "name": "淺灰"
                }
              ],
              "sizes": [
                "S",
                "M",
                "L"
              ]
            },
            {
              "id": 201807202150,
              "category": "women",
              "title": "小扇紋細織上衣",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 599,
              "texture": "棉 100%",
              "wash": "手洗，溫水",
              "place": "中國",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201807202150/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201807202150/0.jpg",
                "https://api.appworks-school.tw/assets/201807202150/1.jpg",
                "https://api.appworks-school.tw/assets/201807202150/0.jpg",
                "https://api.appworks-school.tw/assets/201807202150/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "DDFFBB",
                  "size": "S",
                  "stock": 3
                },
                {
                  "color_code": "DDFFBB",
                  "size": "M",
                  "stock": 5
                },
                {
                  "color_code": "CCCCCC",
                  "size": "S",
                  "stock": 4
                },
                {
                  "color_code": "CCCCCC",
                  "size": "M",
                  "stock": 1
                },
                {
                  "color_code": "BB7744",
                  "size": "S",
                  "stock": 2
                },
                {
                  "color_code": "BB7744",
                  "size": "M",
                  "stock": 6
                }
              ],
              "colors": [
                {
                  "code": "DDFFBB",
                  "name": "亮綠"
                },
                {
                  "code": "CCCCCC",
                  "name": "淺灰"
                },
                {
                  "code": "BB7744",
                  "name": "淺棕"
                }
              ],
              "sizes": [
                "S",
                "M"
              ]
            },
            {
              "id": 201807202157,
              "category": "women",
              "title": "活力花紋長筒牛仔褲",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 1299,
              "texture": "棉 100%",
              "wash": "手洗，溫水",
              "place": "中國",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201807202157/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201807202157/0.jpg",
                "https://api.appworks-school.tw/assets/201807202157/1.jpg",
                "https://api.appworks-school.tw/assets/201807202157/0.jpg",
                "https://api.appworks-school.tw/assets/201807202157/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "DDF0FF",
                  "size": "S",
                  "stock": 8
                },
                {
                  "color_code": "DDF0FF",
                  "size": "M",
                  "stock": 5
                },
                {
                  "color_code": "DDF0FF",
                  "size": "L",
                  "stock": 6
                },
                {
                  "color_code": "CCCCCC",
                  "size": "S",
                  "stock": 0
                },
                {
                  "color_code": "CCCCCC",
                  "size": "M",
                  "stock": 6
                },
                {
                  "color_code": "CCCCCC",
                  "size": "L",
                  "stock": 5
                },
                {
                  "color_code": "334455",
                  "size": "S",
                  "stock": 2
                },
                {
                  "color_code": "334455",
                  "size": "M",
                  "stock": 7
                },
                {
                  "color_code": "334455",
                  "size": "L",
                  "stock": 9
                }
              ],
              "colors": [
                {
                  "code": "DDF0FF",
                  "name": "淺藍"
                },
                {
                  "code": "CCCCCC",
                  "name": "淺灰"
                },
                {
                  "code": "334455",
                  "name": "深藍"
                }
              ],
              "sizes": [
                "S",
                "M",
                "L"
              ]
            },
            {
              "id": 201902191210,
              "category": "women",
              "title": "精緻扭結洋裝",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 999,
              "texture": "棉 100%",
              "wash": "手洗",
              "place": "越南",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201902191210/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201902191210/0.jpg",
                "https://api.appworks-school.tw/assets/201902191210/1.jpg",
                "https://api.appworks-school.tw/assets/201902191210/0.jpg",
                "https://api.appworks-school.tw/assets/201902191210/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "FFFFFF",
                  "size": "S",
                  "stock": 0
                },
                {
                  "color_code": "FFFFFF",
                  "size": "M",
                  "stock": 9
                },
                {
                  "color_code": "FFDDDD",
                  "size": "S",
                  "stock": 2
                },
                {
                  "color_code": "FFDDDD",
                  "size": "M",
                  "stock": 1
                }
              ],
              "colors": [
                {
                  "code": "FFFFFF",
                  "name": "白色"
                },
                {
                  "code": "FFDDDD",
                  "name": "粉紅"
                }
              ],
              "sizes": [
                "S",
                "M"
              ]
            },
            {
              "id": 201902191242,
              "category": "women",
              "title": "透肌澎澎薄紗襯衫",
              "description": "厚薄：薄\r\n彈性：無",
              "price": 999,
              "texture": "棉 100%",
              "wash": "手洗",
              "place": "越南",
              "note": "實品顏色依單品照為主",
              "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
              "main_image": "https://api.appworks-school.tw/assets/201902191242/main.jpg",
              "images": [
                "https://api.appworks-school.tw/assets/201902191242/0.jpg",
                "https://api.appworks-school.tw/assets/201902191242/1.jpg",
                "https://api.appworks-school.tw/assets/201902191242/0.jpg",
                "https://api.appworks-school.tw/assets/201902191242/1.jpg"
              ],
              "variants": [
                {
                  "color_code": "DDFFBB",
                  "size": "M",
                  "stock": 3
                },
                {
                  "color_code": "DDFFBB",
                  "size": "L",
                  "stock": 9
                },
                {
                  "color_code": "DDF0FF",
                  "size": "M",
                  "stock": 2
                },
                {
                  "color_code": "DDF0FF",
                  "size": "L",
                  "stock": 6
                }
              ],
              "colors": [
                {
                  "code": "DDFFBB",
                  "name": "亮綠"
                },
                {
                  "code": "DDF0FF",
                  "name": "淺藍"
                }
              ],
              "sizes": [
                "M",
                "L"
              ]
            }
          ],
          "next_paging": 1
        }
    """.trimIndent()
}