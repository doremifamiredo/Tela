import java.time.LocalDate

data class Post(
    val id: Int,
    val authorId: Int,
    val createdAt: LocalDate,
    var text: String,
    var attachment: List<Attachment>? = null,
    var comments: List<Comment>? = null,
    var likes: Int = 0,
    var isChanged: Boolean = false,
    var isPinned: Boolean = false
)