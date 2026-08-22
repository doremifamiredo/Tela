import java.time.LocalDate

object WallService {
    private var posts = emptyArray<Post>()
    var nextId = 1
    private var authors = emptyMap<Int, String>()
    private var reports = arrayListOf<Report>()

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
        val post = findPostById(postId) ?: throw PostNotFoundException("No post with id $postId")
        if (post.comments == null) {
            post.comments = arrayListOf(comment)
        } else {
            post.comments!!.add(comment)
        }
        return comment
    }

    fun findPostById(postId: Int): Post? {
        for (post in posts) {
            if (post.postId == postId) {
                return post
            }
        }
        return null
    }

    fun getLastPostId(): Int {
        return posts.last().postId
    }

    fun reportComment(postId: Int, commentId: Int, reason: Int): Boolean {
        if (reason !in 0..8) {
            return false
        }
        val post = findPostById(postId) ?: return false
        val comments = post.comments ?: return false
        for (comm in comments) {
            if (comm.commentId == commentId) {
                reports.add(Report.ReportComment(commentId, postId, reason))
                return true
            }
        }
        return false
    }
}