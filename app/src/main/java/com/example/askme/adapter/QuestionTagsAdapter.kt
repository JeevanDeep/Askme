package com.example.askme.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.askme.R
import kotlinx.android.synthetic.main.tags_row_item.view.*

class QuestionTagsAdapter(private val listOfTags: List<String>) :
    RecyclerView.Adapter<QuestionTagsAdapter.QuestionTagsViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): QuestionTagsViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.tags_row_item, p0, false)
        return QuestionTagsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listOfTags.size
    }

    override fun onBindViewHolder(p0: QuestionTagsViewHolder, p1: Int) {
        p0.tag.text = listOfTags[p1]
    }

    inner class QuestionTagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag = itemView.tagText
    }
}