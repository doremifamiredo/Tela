import java.time.LocalDate

data class Post(
    val id: Int,
    val authorId: Int,
    val createdAt: LocalDate,
    var text: String,
    var comments: List<Comment> = emptyList(),
    var likes: Int = 0,
    var isChanged: Boolean = false,
    var isPinned: Boolean = false
)