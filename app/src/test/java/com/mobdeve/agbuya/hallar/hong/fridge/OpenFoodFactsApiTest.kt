package com.mobdeve.agbuya.hallar.hong.fridge

import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.OpenFoodFactsApi
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(MockitoJUnitRunner::class)
class OpenFoodFactsApiTest {

    private lateinit var api: OpenFoodFactsApi

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(OpenFoodFactsApi::class.java)
    }

    @Test
    fun testGetProduct_shouldReturnProduct() = runBlocking {
        // Only has title
        val barcode = "889896601160" // try any known UPC code

        val response = api.getProduct(barcode)

        assertTrue(response.isSuccessful)
        assertNotNull(response.body()?.product)
        println("Product name: ${response.body()?.product?.productNameEN}")
    }
}