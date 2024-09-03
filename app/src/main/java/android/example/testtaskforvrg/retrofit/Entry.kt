package android.example.testtaskforvrg.retrofit

data class Entry(
    val id: String,
    val subreddit: String,
    val created_utc: Int,
    val thumbnail: String,
    val num_comments: Int
)
