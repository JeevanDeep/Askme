package com.example.askme.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.askme.R
import com.example.askme.model.QuestionListModel
import com.example.askme.utils.BindingAdapters
import kotlinx.android.synthetic.main.question_item_row.view.*

class QuestionListAdapter(
    private val list: List<QuestionListModel>,
    private val questionClickListener: QuestionClickListener
) :
    RecyclerView.Adapter<QuestionListAdapter.QuestionListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): QuestionListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.question_item_row, parent, false)
        return QuestionListViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: QuestionListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class QuestionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val answeredBy = itemView.answerByName
        val answer = itemView.answerTextView
        val questionTitle = itemView.questionTextView
        val questionRoot = itemView.questionRoot
        val answerDate = itemView.answerDate

        fun bind(questionListModel: QuestionListModel) {

            if (questionListModel.answers.isEmpty())
                answer.text = "No answers yet"
            else {
                answer.text = questionListModel.answers[0].content
                answeredBy.text = questionListModel.answers[0].author
                BindingAdapters.postedDate(answerDate, questionListModel.answers[0].date_posted)
            }
            questionTitle.text = questionListModel.title

            questionRoot.setOnClickListener {
                questionClickListener.onQuestionClicked(
                    questionTitle,
                    questionListModel
                )
            }
        }

    }

    interface QuestionClickListener {
        fun onQuestionClicked(questionTextView: TextView, questionListModel: QuestionListModel)
    }
}