package android.example.testtaskforvrg.adapter

import android.example.testtaskforvrg.R
import android.example.testtaskforvrg.databinding.ListItemBinding
import android.example.testtaskforvrg.retrofit.Entry
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class EntryAdapter : ListAdapter<Entry, EntryAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)

        fun bind(entry: Entry) = with(binding) {
            author.text = entry.subreddit
            time.text = entry.created_utc.toString()
            comments.text = entry.num_comments.toString()

        }
    }

    class Comparator : DiffUtil.ItemCallback<Entry>() {
        override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean {
            return oldItem.subreddit == newItem.subreddit &&
                    oldItem.created_utc == newItem.created_utc
        }

        override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}
