import java.time.LocalDate

object WallService {
    private var posts = emptyArray<Post>()
    var nextId = 1
    private var authors = emptyMap<Int, String>()

    fun addPost(authorId: Int, text: String): Post {
        val id = nextId
        posts += Post(id, authorId, LocalDate.now(), text)
        nextId++
        return posts.last()
    }

    fun updatePost(postId: Int, newText: String): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == postId) {
                posts[index].text = newText
                return true
            }
        }
        return false
    }

    fun likeById(id: Int) {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                posts[index] = post.copy(likes = post.likes + 1)
            }
        }
    }
}