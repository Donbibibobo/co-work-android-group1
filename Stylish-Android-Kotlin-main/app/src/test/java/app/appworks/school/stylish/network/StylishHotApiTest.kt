package app.appworks.school.stylish.network

import app.appworks.school.stylish.data.Product
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class StylishHotApiTest : StylishApiServiceTest() {

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_hot_get() {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(
            mockResponse.setBody(rawHot)
        )

        runBlocking {
            val hotResult = service.getMarketingHots()
            val hots = hotResult.hotsList
            assertTrue(hots != null)
            assertTrue(hots!!.size == 2)
            assertEquals("冬季新品搶先看", hots.first().title)
            assertEquals("百搭穿搭必敗品", hots.last().title)
            assertEquals(3, hots.first().products.size)
            assertProducts(hots.first().products.first())
            assertImages(hots.first().products.first())
            assertVariants(hots.first().products.first())
            assertColors(hots.first().products.first())
            assertSizes(hots.first().products.first())

        }
    }

    private fun assertProducts(product: Product) {
        assertEquals(201807202157, product.id)

        //TODO: missing category
        assertEquals("活力花紋長筒牛仔褲", product.title)
        assertEquals("厚薄：薄\r\n彈性：無", product.description)
        assertEquals(1299, product.price)
        assertEquals("棉 100%", product.texture)
        assertEquals("手洗，溫水", product.wash)
        assertEquals("中國", product.place)
        assertEquals("實品顏色依單品照為主", product.note)
        assertEquals(
            "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
            product.story
        )
        assertEquals(
            "https://api.appworks-school.tw/assets/201807202157/main.jpg",
            product.mainImage
        )
    }

    private fun assertImages(product: Product) {
        assertEquals(4, product.images.size)
        assertEquals("https://api.appworks-school.tw/assets/201807202157/0.jpg", product.images[0])
        assertEquals("https://api.appworks-school.tw/assets/201807202157/1.jpg", product.images[1])
        assertEquals("https://api.appworks-school.tw/assets/201807202157/0.jpg", product.images[2])
        assertEquals("https://api.appworks-school.tw/assets/201807202157/1.jpg", product.images[3])
    }

    private fun assertVariants(product: Product) {
        assertEquals(9, product.variants.size)
        assertEquals("DDF0FF", product.variants[0].colorCode)
        assertEquals("S", product.variants[0].size)
        assertEquals(8, product.variants[0].stock)

        assertEquals("DDF0FF", product.variants[1].colorCode)
        assertEquals("M", product.variants[1].size)
        assertEquals(5, product.variants[1].stock)

        assertEquals("DDF0FF", product.variants[2].colorCode)
        assertEquals("L", product.variants[2].size)
        assertEquals(6, product.variants[2].stock)

        assertEquals("CCCCCC", product.variants[3].colorCode)
        assertEquals("S", product.variants[3].size)
        assertEquals(0, product.variants[3].stock)

        assertEquals("CCCCCC", product.variants[4].colorCode)
        assertEquals("M", product.variants[4].size)
        assertEquals(6, product.variants[4].stock)

        assertEquals("CCCCCC", product.variants[5].colorCode)
        assertEquals("L", product.variants[5].size)
        assertEquals(5, product.variants[5].stock)

        assertEquals("334455", product.variants[6].colorCode)
        assertEquals("S", product.variants[6].size)
        assertEquals(2, product.variants[6].stock)

        assertEquals("334455", product.variants[7].colorCode)
        assertEquals("M", product.variants[7].size)
        assertEquals(7, product.variants[7].stock)

        assertEquals("334455", product.variants[8].colorCode)
        assertEquals("L", product.variants[8].size)
        assertEquals(9, product.variants[8].stock)
    }

    private fun assertColors(product: Product) {
        assertEquals(3, product.colors.size)
        assertEquals("DDF0FF", product.colors[0].code)
        assertEquals("淺藍", product.colors[0].name)

        assertEquals("CCCCCC", product.colors[1].code)
        assertEquals("淺灰", product.colors[1].name)

        assertEquals("334455", product.colors[2].code)
        assertEquals("深藍", product.colors[2].name)

    }

    private fun assertSizes(product: Product) {
        assertEquals(3, product.sizes.size)
        assertEquals("S", product.sizes[0])
        assertEquals("M", product.sizes[1])
        assertEquals("L", product.sizes[2])
    }

    private val rawHot = """
        {
          "data": [
            {
              "title": "冬季新品搶先看",
              "products": [
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
                  "id": 201807242216,
                  "category": "men",
                  "title": "時尚輕鬆休閒西裝",
                  "description": "厚薄：薄\r\n彈性：無",
                  "price": 2399,
                  "texture": "棉 100%",
                  "wash": "手洗，溫水",
                  "place": "中國",
                  "note": "實品顏色依單品照為主",
                  "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
                  "main_image": "https://api.appworks-school.tw/assets/201807242216/main.jpg",
                  "images": [
                    "https://api.appworks-school.tw/assets/201807242216/0.jpg",
                    "https://api.appworks-school.tw/assets/201807242216/1.jpg",
                    "https://api.appworks-school.tw/assets/201807242216/0.jpg",
                    "https://api.appworks-school.tw/assets/201807242216/1.jpg"
                  ],
                  "variants": [
                    {
                      "color_code": "FFFFFF",
                      "size": "S",
                      "stock": 10
                    },
                    {
                      "color_code": "FFFFFF",
                      "size": "M",
                      "stock": 5
                    },
                    {
                      "color_code": "FFFFFF",
                      "size": "L",
                      "stock": 6
                    },
                    {
                      "color_code": "CCCCCC",
                      "size": "S",
                      "stock": 1
                    },
                    {
                      "color_code": "CCCCCC",
                      "size": "M",
                      "stock": 3
                    },
                    {
                      "color_code": "CCCCCC",
                      "size": "L",
                      "stock": 10
                    }
                  ],
                  "colors": [
                    {
                      "code": "FFFFFF",
                      "name": "白色"
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
                  "id": 201807242232,
                  "category": "accessories",
                  "title": "卡哇伊多功能隨身包",
                  "description": "厚薄：薄\r\n彈性：無",
                  "price": 1299,
                  "texture": "棉 100%",
                  "wash": "手洗，溫水",
                  "place": "中國",
                  "note": "實品顏色依單品照為主",
                  "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
                  "main_image": "https://api.appworks-school.tw/assets/201807242232/main.jpg",
                  "images": [
                    "https://api.appworks-school.tw/assets/201807242232/0.jpg",
                    "https://api.appworks-school.tw/assets/201807242232/1.jpg",
                    "https://api.appworks-school.tw/assets/201807242232/0.jpg",
                    "https://api.appworks-school.tw/assets/201807242232/1.jpg"
                  ],
                  "variants": [
                    {
                      "color_code": "FFFFFF",
                      "size": "F",
                      "stock": 1
                    },
                    {
                      "color_code": "FFDDDD",
                      "size": "F",
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
                    "F"
                  ]
                }
              ]
            },
            {
              "title": "百搭穿搭必敗品",
              "products": [
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
                  "id": 201807242211,
                  "category": "men",
                  "title": "純色輕薄百搭襯衫",
                  "description": "厚薄：薄\r\n彈性：無",
                  "price": 799,
                  "texture": "棉 100%",
                  "wash": "手洗，溫水",
                  "place": "中國",
                  "note": "實品顏色依單品照為主",
                  "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
                  "main_image": "https://api.appworks-school.tw/assets/201807242211/main.jpg",
                  "images": [
                    "https://api.appworks-school.tw/assets/201807242211/0.jpg",
                    "https://api.appworks-school.tw/assets/201807242211/1.jpg",
                    "https://api.appworks-school.tw/assets/201807242211/0.jpg",
                    "https://api.appworks-school.tw/assets/201807242211/1.jpg"
                  ],
                  "variants": [
                    {
                      "color_code": "FFFFFF",
                      "size": "M",
                      "stock": 5
                    },
                    {
                      "color_code": "FFFFFF",
                      "size": "L",
                      "stock": 7
                    },
                    {
                      "color_code": "FFFFFF",
                      "size": "XL",
                      "stock": 1
                    },
                    {
                      "color_code": "DDF0FF",
                      "size": "M",
                      "stock": 1
                    },
                    {
                      "color_code": "DDF0FF",
                      "size": "L",
                      "stock": 4
                    },
                    {
                      "color_code": "DDF0FF",
                      "size": "XL",
                      "stock": 3
                    }
                  ],
                  "colors": [
                    {
                      "code": "FFFFFF",
                      "name": "白色"
                    },
                    {
                      "code": "DDF0FF",
                      "name": "淺藍"
                    }
                  ],
                  "sizes": [
                    "M",
                    "L",
                    "XL"
                  ]
                },
                {
                  "id": 201807242228,
                  "category": "accessories",
                  "title": "夏日海灘戶外遮陽帽",
                  "description": "厚薄：薄\r\n彈性：無",
                  "price": 1499,
                  "texture": "棉 100%",
                  "wash": "手洗，溫水",
                  "place": "中國",
                  "note": "實品顏色依單品照為主",
                  "story": "O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.",
                  "main_image": "https://api.appworks-school.tw/assets/201807242228/main.jpg",
                  "images": [
                    "https://api.appworks-school.tw/assets/201807242228/0.jpg",
                    "https://api.appworks-school.tw/assets/201807242228/1.jpg",
                    "https://api.appworks-school.tw/assets/201807242228/0.jpg",
                    "https://api.appworks-school.tw/assets/201807242228/1.jpg"
                  ],
                  "variants": [
                    {
                      "color_code": "DDF0FF",
                      "size": "M",
                      "stock": 7
                    },
                    {
                      "color_code": "DDF0FF",
                      "size": "L",
                      "stock": 1
                    },
                    {
                      "color_code": "BB7744",
                      "size": "M",
                      "stock": 3
                    },
                    {
                      "color_code": "BB7744",
                      "size": "L",
                      "stock": 1
                    }
                  ],
                  "colors": [
                    {
                      "code": "DDF0FF",
                      "name": "淺藍"
                    },
                    {
                      "code": "BB7744",
                      "name": "淺棕"
                    }
                  ],
                  "sizes": [
                    "M",
                    "L"
                  ]
                }
              ]
            }
          ]
        }
    """.trimIndent()
}