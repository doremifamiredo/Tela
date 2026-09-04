sealed class NotFoundException(message: String): RuntimeException(message) {
    class PostNotFound(postId: Int): NotFoundException("No post with id $postId")
    class PostCommentNotFound(commentId: Int, postId: Int): NotFoundException("No comment $commentId in post $postId")
    class NullComment(postId: Int): NotFoundException("Comments is null in post $postId")
    class AuthorNotFound(authorId: Int): NotFoundException("No author with id $authorId")
    class NoteNotFound(noteId: Int): NotFoundException("No note with id $noteId")
    class NoteCommentNotFound(commentId: Int, noteId: Int): NotFoundException("No comment $commentId in note $noteId")
    class NoteDeleted(noteId: Int): NotFoundException("The note $noteId has already been deleted")
    class NoteCommentDeleted(noteId: Int, commentId: Int)
        : NotFoundException("Comment $commentId of note $noteId has already been deleted")
    class ChatNotFound(talker1Id: Int, talker2Id: Int): NotFoundException("Chat for $talker1Id and $talker2Id not found")
}

class WrongReasonException(val reason: Int) : RuntimeException("Reason $reason is not valid")