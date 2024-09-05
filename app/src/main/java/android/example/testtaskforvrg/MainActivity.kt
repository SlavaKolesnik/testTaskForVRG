package android.example.testtaskforvrg

import android.content.Intent
import android.content.res.Configuration
import android.example.testtaskforvrg.adapter.EntryAdapter
import android.example.testtaskforvrg.constance.Constance
import android.example.testtaskforvrg.databinding.ActivityMainBinding
import android.example.testtaskforvrg.retrofit.Entry
import android.example.testtaskforvrg.retrofit.MainApi
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: EntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "onCreate called")
        enableEdgeToEdge()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun replaceFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentHistory, fragment)
                .commit()
        }

        binding.bNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.db -> {
                    replaceFragment(HistoryFragment())
                    binding.rcView.visibility = View.GONE
                    binding.fragmentHistory.visibility = View.VISIBLE
                    true
                }
                R.id.reddit -> {

                    binding.rcView.visibility = View.VISIBLE
                    binding.fragmentHistory.visibility = View.GONE
                    true
                }
                else -> false
            }
        }

        val orientation = resources.configuration.orientation
        val numberOfColumns = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            4 } else { 2
        }

        adapter = EntryAdapter { entry ->
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra(Constance.KEY_1, entry.thumbnail)
            startActivity(intent)
//            Toast.makeText(this, "Author: ${entry.subreddit}", Toast.LENGTH_SHORT).show()
        }
        binding.rcView.layoutManager = GridLayoutManager(this@MainActivity, numberOfColumns)
        binding.rcView.adapter = adapter

        val inspector = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().addInterceptor(inspector).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val mainApi = retrofit.create(MainApi::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("API Call", "Starting API call")
            try {
                val listing = mainApi.getTopPosts()
                Log.d("API Response", listing.toString())
                val entries = listing.data.children.map { it.data }
                Log.d("APIResponse", listing.toString())
                runOnUiThread {
                    Log.d("EntriesSize", entries.size.toString())
                    adapter.submitList(entries)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("API Error", "Error occurred: ${e.message}", e)
            }
        }
    }

}
