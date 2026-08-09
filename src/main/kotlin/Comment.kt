import java.time.LocalDate

data class Comment(
    val authorId: Int,
    val text: String,
    val date: LocalDate,
    val likes: Int = 0
)