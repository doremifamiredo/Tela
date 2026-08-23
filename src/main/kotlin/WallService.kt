import NotFoundException.*
import Report.ReportComment
import java.time.LocalDate

object WallService {
    private var posts = mutableListOf<Post>()
    var nextId = 1
    var authors = mutableListOf<String>()
    private var reports = mutableListOf<Report>()

    fun addPost(authorId: Int, text: String): Post {
        val id = nextId
        posts += Post(id, authorId, LocalDate.now(), text)
        nextId++
        return posts.last()
    }

    fun updatePost(postId: Int, newText: String): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.postId == postId) {
                posts[index].text = newText
                posts[index].isChanged = true
                return true
            }
        }
        return false
    }

    fun likeById(id: Int) {
        for ((index, post) in posts.withIndex()) {
            if (post.postId == id) {
                posts[index] = post.copy(likes = post.likes + 1)
            }
        }
    }

    fun createComment(postId: Int, comment: Comment): Comment {
        val post = findPostById(postId) ?: throw PostNotFound(postId)
        if (post.comments == null) {
            post.comments = arrayListOf(comment)
        } else {
            post.comments!!.add(comment)
        }
        return comment
    }

    fun findPostById(postId: Int): Post {
        if (postId >= posts.size) throw PostNotFound(postId)
        return posts[postId]
    }

    fun getLastPostId(): Int {
        return posts.size - 1
    }

    fun reportComment(postId: Int, commentId: Int, reason: Int): Boolean {
        if (reason !in 0..8) {
            throw WrongReasonException(reason)
        }
        val post = findPostById(postId)
        post.comments ?: throw NullComment(postId)
        if (commentId >= post.comments!!.size) throw PostCommentNotFound(commentId, postId)
        reports.add(ReportComment(commentId, postId, reason))
        return true
    }

    fun resetAllFields() {
        posts = mutableListOf()
        nextId = 1
        authors = mutableListOf()
        reports = mutableListOf()
    }

    fun addAuthor(name: String) : Int {
        val authorId = authors.size
        authors.add(authorId, name)
        return authorId
    }

    fun checkAuthor(authorId: Int) : Boolean {
        return authorId < authors.size
    }
}