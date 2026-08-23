sealed class NotFoundException(message: String) : RuntimeException(message) {
    class PostNotFound(postId: Int) : NotFoundException("No post with id $postId")
    class CommentNotFound(commentId: Int, postId: Int) :
        NotFoundException("No comment with id $commentId in post $postId")
    class NullComment(postId: Int) : NotFoundException("Comments is null in post $postId")
}

class WrongReasonException(val reason: Int) : RuntimeException("Reason $reason is not valid")