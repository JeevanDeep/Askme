package com.example.askme.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.askme.R
import com.example.askme.databinding.AnswerItemRowBinding
import com.example.askme.model.AnswerModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.answer_item_row.view.*

class AnswerListAdapter(private val answerList: MutableList<AnswerModel>, private val listener: AnswerListener) :
    RecyclerView.Adapter<AnswerListAdapter.AnswerListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerListViewHolder {
        val binding = DataBindingUtil.inflate<AnswerItemRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.answer_item_row,
            parent,
            false
        )
        return AnswerListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    override fun onBindViewHolder(holder: AnswerListViewHolder, position: Int) {
        holder.bind(answerList[position])
    }

    fun removeAnswer(position: Int) {
        answerList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addAnswer(answerModel: AnswerModel?) {
        answerList.add(0, answerModel!!)
        notifyItemInserted(0)
    }

    inner class AnswerListViewHolder(private val binding: AnswerItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val menu = binding.root.optionsImage
        val popup = PopupMenu(menu.context, menu)
        fun bind(answerModel: AnswerModel) {
            binding.isCurrentUser = FirebaseAuth.getInstance().currentUser?.uid == answerModel.author_id
            binding.model = answerModel
            binding.notifyChange()

            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
            menu.setOnClickListener {
                popup.show()
            }

            popup.setOnMenuItemClickListener {
                listener.onDeleteClicked(answerList, answerModel, adapterPosition)
                return@setOnMenuItemClickListener true
            }
        }

    }

    interface AnswerListener {
        fun onDeleteClicked(answerList: MutableList<AnswerModel>, answerModel: AnswerModel, position: Int)
    }
}