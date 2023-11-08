package com.example.testjson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testjson.databinding.ActivityMainBinding
import com.example.testjson.helper.Constants.BASE_URL
import com.example.testjson.retrofit.AuthRequest
import com.example.testjson.retrofit.MainApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("ActivityMainBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY//перехватываем Body

        //создаем клиента для retrofit
        val client = OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()

        //создали инстранцию retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)// базовая ссылка
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // специальный конвертер
            .build()

        //создаем инстанцию интерфейса
        val mainApi = retrofit.create(MainApi::class.java)

        binding.button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = mainApi.auth(
                    AuthRequest(
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString()
                    )
                )
                runOnUiThread {
                    //с помощью пикассо загружаем фото
                    binding.apply {
                        Picasso.get().load(user.image).into(imFotoHeader)
                        tvFirstName.text = user.firstName
                        tvLastName.text = user.lastName
                    }

                }

            }

        }


    }
}