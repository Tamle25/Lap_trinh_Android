package com.example.gmaillayout

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Dữ liệu mẫu
        val emailList = arrayListOf(
            Email("Edurila.com", "$19 Only (First 10 spots)", "Are you looking to Learn Web Designing...", "12:34 PM", Color.parseColor("#4285F4")),
            Email("Chris Abad", "Help make Campaign Monitor better", "Let us know your thoughts! No Images...", "11:22 AM", Color.parseColor("#DB4437")),
            Email("Tuto.com", "8h de formation gratuite", "Photoshop, SEO, Blender, CSS, WordPress...", "11:04 AM", Color.parseColor("#0F9D58")),
            Email("support", "Société Ovh : suivi de vos services", "SAS OVH - http://www.ovh.com 2 rue K...", "10:26 AM", Color.parseColor("#607D8B")),
            Email("Matt from Ionic", "The New Ionic Creator Is Here!", "Announcing the all-new Creator, build...", "10:00 AM", Color.parseColor("#F4B400")),
            Email("Github", "Security Alert", "We noticed a new login to your account...", "9:00 AM", Color.parseColor("#24292E")),
            Email("LinkedIn", "You appeared in 5 searches", "See who is looking for you this week...", "8:30 AM", Color.parseColor("#0077B5"))
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = EmailAdapter(emailList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}