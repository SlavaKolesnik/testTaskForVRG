package android.example.testtaskforvrg

import android.example.testtaskforvrg.constance.Constance
import android.example.testtaskforvrg.databinding.ActivityItemBinding
import android.example.testtaskforvrg.databinding.ActivityMainBinding
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val info = intent.getStringExtra(Constance.KEY_1)

        binding.apply {
            Picasso.get()
                .load(info)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imItem)

            download.setOnClickListener {
                Picasso.get()
                    .load(info)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(object : com.squareup.picasso.Target {
                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            Toast.makeText(this@ItemActivity, Constance.SAVE, Toast.LENGTH_LONG).show()
                            saveImage(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                            Toast.makeText(this@ItemActivity, "$e $errorDrawable", Toast.LENGTH_LONG).show()
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            Toast.makeText(this@ItemActivity, Constance.LOADING, Toast.LENGTH_LONG).show()
                        }
                    })
            }

            cancel.setOnClickListener {
                finish()
            }
        }
    }
    private fun saveImage(bitmap: Bitmap?) {
        if (bitmap == null) return

        val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(storageDir, fileName)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}