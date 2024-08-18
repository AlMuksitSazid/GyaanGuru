package com.gyaanguru.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gyaanguru.Domain.AppInfo
import com.gyaanguru.R

class AppListAdapter(private val context: Context, private val appList: List<AppInfo>) :
    RecyclerView.Adapter<AppListAdapter.AppViewHolder>() {

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        val appDescriptionTextView: TextView = itemView.findViewById(R.id.appDescriptionTextView)
        val appIconImageView: ImageView = itemView.findViewById(R.id.appIconImageView)
        val downloadButton: Button = itemView.findViewById(R.id.downloadButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val appInfo = appList[position]
        holder.appNameTextView.text = appInfo.name
        holder.appDescriptionTextView.text = appInfo.description
        holder.appIconImageView.setImageResource(appInfo.iconResId)

        holder.downloadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appInfo.downloadLink))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }
}