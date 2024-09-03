package android.example.testtaskforvrg.adapter

import android.example.testtaskforvrg.R
import android.example.testtaskforvrg.constance.Constance
import android.example.testtaskforvrg.databinding.ListItemBinding
import android.example.testtaskforvrg.retrofit.Entry
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.time.Instant
import java.time.Duration

class EntryAdapter(
    private val onItemClick: (Entry) -> Unit
) : ListAdapter<Entry, EntryAdapter.Holder>(Comparator()) {

    class Holder(view: View, private val onItemClick: (Entry) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(entry: Entry) = with(binding) {
            author.text = entry.subreddit

            val createdTime = Instant.ofEpochSecond(entry.created_utc.toLong())
            val currentTime = Instant.now()
            val duration = Duration.between(createdTime, currentTime)
            val hoursAgo = duration.toHours()

            time.text = hoursAgo.toString() + Constance.TIME_AGO
            comments.text = Constance.COMMENTS + entry.num_comments.toString()

            Picasso.get()
                .load(entry.thumbnail)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(thumbnail)

            itemView.setOnClickListener {
                onItemClick(entry)
            }
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent,
            false)
        return Holder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}