package app.appworks.school.stylish.data

import app.appworks.school.stylish.network.moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarketingHotsResultTest {
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

    private val result = moshi.adapter(MarketingHotsResult::class.java).fromJson(rawHot)

    @Test
    fun test_hots_converter() {
        assertTrue(result != null)
        assertTrue(result!!.hotsList != null)
        assertTrue(result.hotsList!!.size == 2)

        val list = result.toHomeItems()
        assertTrue(list.size == 8)
        assertTrue(list[0] is HomeItem.Title)
        assertEquals("冬季新品搶先看", (list[0] as HomeItem.Title).title)

        assertTrue(result.hotsList!![0].products.size == 3)

        assertTrue(list[1] is HomeItem.FullProduct)
        assertEquals(result.hotsList!![0].products[0], (list[1] as HomeItem.FullProduct).product)
        assertEquals(result.hotsList!![0].products[0].id, list[1].id)

        assertTrue(list[2] is HomeItem.CollageProduct)
        assertEquals(result.hotsList!![0].products[1], (list[2] as HomeItem.CollageProduct).product)
        assertEquals(result.hotsList!![0].products[1].id, list[2].id)

        assertTrue(list[3] is HomeItem.FullProduct)
        assertEquals(result.hotsList!![0].products[2], (list[3] as HomeItem.FullProduct).product)
        assertEquals(result.hotsList!![0].products[2].id, list[3].id)



        assertTrue(list[4] is HomeItem.Title)
        assertEquals("百搭穿搭必敗品", (list[4] as HomeItem.Title).title)

        assertTrue(result.hotsList!![1].products.size == 3)

        assertTrue(list[5] is HomeItem.FullProduct)
        assertEquals(result.hotsList!![1].products[0], (list[5] as HomeItem.FullProduct).product)
        assertEquals(result.hotsList!![1].products[0].id, list[5].id)

        assertTrue(list[6] is HomeItem.CollageProduct)
        assertEquals(result.hotsList!![1].products[1], (list[6] as HomeItem.CollageProduct).product)
        assertEquals(result.hotsList!![1].products[1].id, list[6].id)

        assertTrue(list[7] is HomeItem.FullProduct)
        assertEquals(result.hotsList!![1].products[2], (list[7] as HomeItem.FullProduct).product)
        assertEquals(result.hotsList!![1].products[2].id, list[7].id)

    }
}