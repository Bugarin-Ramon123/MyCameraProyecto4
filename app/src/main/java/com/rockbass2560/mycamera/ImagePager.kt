package com.rockbass2560.mycamera

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import java.io.File
import java.io.FilenameFilter
import java.net.URLConnection

class ImagePager : AppCompatActivity() {
    lateinit var file : File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pager)

        val folderImage = File(filesDir, "image")
        val files = folderImage.listFiles(FilenameFilter { dir, name ->
            name.contains("image_")
        })?.reversed()

        val viewPager = findViewById<ViewPager2>(R.id.viewpager)

        val photoPagerAdapter = PhotoPagerAdapter(this, files!!)

        viewPager.adapter = photoPagerAdapter

        val bShareImage = findViewById<Button>(R.id.btnShare)
        bShareImage.setOnClickListener {
            Toast.makeText(bShareImage.context,"Sharing...",Toast.LENGTH_SHORT).show()
            file = files[viewPager.currentItem]
            var Uri = FileProvider.getUriForFile(
                this,"com.rockbass2560.mycamera",file
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = URLConnection.guessContentTypeFromName(file.name)
            intent.putExtra(Intent.EXTRA_STREAM,Uri)
            startActivity(Intent.createChooser(intent, "Sharing..."))
        }
    }

    private inner class PhotoPagerAdapter(fa: FragmentActivity, private val files: List<File>) :
        FragmentStateAdapter(fa) {
        val cacheFragments = mutableMapOf<Int, Fragment>()

        override fun getItemCount(): Int = files.size

        override fun createFragment(position: Int): Fragment {
            val imagePageFragment: Fragment
            if (!cacheFragments.containsKey(position)) {
                val file = files[position]
                imagePageFragment = ImagePageFragment(file)
                cacheFragments[position] = imagePageFragment
            } else {
                imagePageFragment = cacheFragments[position]!!
            }

            return imagePageFragment
        }

    }

}