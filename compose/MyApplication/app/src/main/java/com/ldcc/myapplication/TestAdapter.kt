package com.ldcc.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ldcc.myapplication.databinding.ItemTestBinding

class TestAdapter : ListAdapter<Int, TestAdapter.TestViewHolder>(TestDiffUtil()){

    inner class TestViewHolder(private val binding: ItemTestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.pos = pos
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val bind = ItemTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TestViewHolder(bind)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(position)
    }
}

class TestDiffUtil : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

}