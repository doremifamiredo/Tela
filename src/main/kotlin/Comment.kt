import java.time.LocalDate

data class Comment(
    val authorId: Int,
    val text: String,
    val date: LocalDate,
    var comments: List<Comment>? = null,
    val likes: Int = 0
)