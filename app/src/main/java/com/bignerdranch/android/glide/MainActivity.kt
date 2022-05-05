package com.bignerdranch.android.glide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.bignerdranch.android.glide.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.javafaker.Faker
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    private var useKeyword:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Используем Glide
        Glide.with(this)
            .load("https://source.unsplash.com/random/800x600")
            .into(binding.randomImage)

        binding.keywordEditText.setOnEditorActionListener{_, actionId, _ ->
            if(actionId==EditorInfo.IME_ACTION_DONE){
                return@setOnEditorActionListener onGetRandomImagePressed()
            }
            return@setOnEditorActionListener false
        }

        binding.randomImageButton.setOnClickListener {
            onGetRandomImagePressed()
        }


        binding.randomQuoteButton.setOnClickListener {
            binding.quote.text = Faker.instance().leagueOfLegends().quote()
        }

        binding.useKeywordCheckBox.setOnClickListener {
            useKeyword = binding.useKeywordCheckBox.isChecked
            updateUi()
        }

        updateUi()
    }

    private fun updateUi() = with(binding){
        useKeywordCheckBox.isChecked = useKeyword
        if(useKeyword){
            keywordEditText.visibility = View.VISIBLE
        } else{
            keywordEditText.visibility = View.GONE
        }
    }

//        binding.randomImageButton.setOnClickListener {
//            Glide.with(this)
//                .load("https://source.unsplash.com/random/800x600")
//                .skipMemoryCache(true)
//                .diskChangeStrategy()//тут можно запретить кэширование
//                .placeholder()//изображение которое будет показываться пока грузится нужное
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(binding.randomImage)
//        }

    private fun onGetRandomImagePressed():Boolean{
        val keyword = binding.keywordEditText.text.toString()
        if(useKeyword && keyword.isBlank()){
            binding.keywordEditText.error = "Keyword is empty"
            return true
        }

        val encodedKeyword = URLEncoder.encode(keyword,StandardCharsets.UTF_8.name())
        Glide.with(this)
            .load("https://source.unsplash.com/random/800x600?${if(useKeyword) "?$encodedKeyword" else ""  }")
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_baseline_sync_24)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.randomImage)

        return false
    }
}