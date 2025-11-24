package com.example.gmaillayout

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmailAdapter(private val emails: List<Email>) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    class EmailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAvatar: TextView = itemView.findViewById(R.id.tvAvatar)
        val tvSender: TextView = itemView.findViewById(R.id.tvSender)
        val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_email, parent, false)
        return EmailViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        val email = emails[position]
        holder.tvSender.text = email.senderName
        holder.tvSubject.text = email.subject
        holder.tvContent.text = email.content
        holder.tvTime.text = email.time

        // Lấy chữ cái đầu
        if (email.senderName.isNotEmpty()) {
            holder.tvAvatar.text = email.senderName.substring(0, 1).uppercase()
        }

        // Đổi màu nền
        val background = holder.tvAvatar.background as GradientDrawable
        background.setColor(email.color)
    }

    override fun getItemCount() = emails.size
}