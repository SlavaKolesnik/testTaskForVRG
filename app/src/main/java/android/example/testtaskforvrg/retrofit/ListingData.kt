package android.example.testtaskforvrg.retrofit

data class ListingData(
    val children: List<Children>,
    val after: String?,
    val dist: Int?
)