import java.io.File
import java.time.LocalDate

data class Post(
    val postId: Int,
    val authorId: Int,
    val createdAt: LocalDate,
    var text: String,
    var attachment: List<Attachment>? = null,
    var comments: MutableList<Comment> = mutableListOf(),
    var likes: Int = 0,
    var isChanged: Boolean = false,
    var isPinned: Boolean = false
)

data class Comment(
    val authorId: Int,
    var text: String,
    val date: LocalDate = LocalDate.now(),
    val likes: Int = 0,
    var isDeleted: Boolean = false
)

sealed class Attachment(type: String) {
    class Photo(val file: File, val attachTd: Int): Attachment("photo")
    class Audio(val file: File, val attachTd: Int, val title: String, val duration: Int): Attachment("audio")
    class Video(val file: File, val attachTd: Int, val title: String, val duration: Int): Attachment("video")
    class Doc(val file: File, val attachTd: Int, val ext: String, val size: Int): Attachment("doc")
    class Link(val url: String, val title: String): Attachment("link")
}

sealed class Report(postId: Int, reason: Int) {
    class ReportComment(val commentId: Int, val postId: Int, val reason: Int): Report(postId, reason)
    class ReportAttachment(val attachId: Int, val postId: Int, val reason: Int): Report(postId, reason)
}

data class Note(
    val authorId: Int,
    var text: String,
    val createdAt: LocalDate = LocalDate.now(),
    val comments: MutableList<Comment> = mutableListOf(),
    var isDeleted: Boolean = false
)