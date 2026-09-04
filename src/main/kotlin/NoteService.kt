import NotFoundException.*
import RealNoteService.notes

object RealNoteService : NoteService {
    var notes = mutableListOf<Note>()

    override fun resetNotes() {
        notes = mutableListOf()
    }

    override fun add(authorId: Int, text: String): Int {
        val noteId = notes.size
        notes.add(noteId, Note(authorId, text))
        return noteId
    }

    override fun createComment(noteId: Int, authorId: Int, text: String): Int {
        val note = notes[noteId]
        val commentId = note.comments.size
        note.comments.add(commentId, Comment(authorId, text))
        return commentId
    }

    override fun delete(noteId: Int): Boolean {
        notes[noteId].isDeleted = true
        return true
    }

    override fun deleteComment(noteId: Int, commentId: Int): Boolean {
        notes[noteId].comments[commentId].isDeleted = true
        return true
    }

    override fun edit(noteId: Int, text: String): Boolean {
        notes[noteId].text = text
        return true
    }

    override fun editComment(noteId: Int, commentId: Int, text: String): Boolean {
        notes[noteId].comments[commentId].text = text
        return true
    }

    override fun get(): MutableList<Note> {
        return notes
    }

    override fun getById(noteId: Int): Note {
        return notes[noteId]
    }

    override fun getComments(noteId: Int): MutableList<Comment> {
        return notes[noteId].comments
    }

    override fun restoreComment(noteId: Int, commentId: Int): Boolean {
        notes[noteId].comments[commentId].isDeleted = false
        return true
    }
}

interface NoteService {
    fun resetNotes()
    fun add(authorId: Int, text: String): Int
    fun createComment(noteId: Int, authorId: Int, text: String): Int
    fun delete(noteId: Int): Boolean
    fun deleteComment(noteId: Int, commentId: Int): Boolean
    fun edit(noteId: Int, text: String): Boolean
    fun editComment(noteId: Int, commentId: Int, text: String): Boolean
    fun get(): MutableList<Note>
    fun getById(noteId: Int): Note
    fun getComments(noteId: Int): MutableList<Comment>
    fun restoreComment(noteId: Int, commentId: Int): Boolean
}

class ValidatingNoteService(
    private val delegate: NoteService
) : NoteService {

    override fun resetNotes() {
        delegate.resetNotes()
    }

    override fun add(authorId: Int, text: String): Int {
        requireValidAuthor(authorId)
        return delegate.add(authorId, text)
    }

    override fun createComment(noteId: Int, authorId: Int, text: String): Int {
        requireValidAuthor(authorId)
        requireValidNote(noteId)
        return delegate.createComment(noteId, authorId, text)
    }

    override fun delete(noteId: Int): Boolean {
        requireValidNote(noteId)
        return delegate.delete(noteId)
    }

    override fun deleteComment(noteId: Int, commentId: Int): Boolean {
        requireValidComment(noteId, commentId)
        if (notes[noteId].comments[commentId].isDeleted) {
            throw NoteCommentDeleted(noteId, commentId)
        }
        return delegate.deleteComment(noteId, commentId)
    }

    override fun edit(noteId: Int, text: String): Boolean {
        requireValidNote(noteId)
        return delegate.edit(noteId, text)
    }

    override fun editComment(noteId: Int, commentId: Int, text: String): Boolean {
        requireValidComment(noteId, commentId)
        if (notes[noteId].comments[commentId].isDeleted) {
            throw NoteCommentDeleted(noteId, commentId)
        }
        return delegate.editComment(noteId, commentId, text)
    }

    override fun get(): MutableList<Note> {
        return delegate.get()
    }

    override fun getById(noteId: Int): Note {
        requireValidNote(noteId)
        return delegate.getById(noteId)
    }

    override fun getComments(noteId: Int): MutableList<Comment> {
        requireValidNote(noteId)
        return delegate.getComments(noteId)
    }

    override fun restoreComment(noteId: Int, commentId: Int): Boolean {
        requireValidComment(noteId, commentId)
        return delegate.restoreComment(noteId, commentId)
    }

    private fun requireValidNote(noteId: Int) {
        if (noteId >= notes.size) {
            throw NoteNotFound(noteId)
        }
        if (notes[noteId].isDeleted) {
            throw NoteDeleted(noteId)
        }
    }

    private fun requireValidAuthor(authorId: Int) {
        if (!WallService.checkAuthor(authorId)) {
            throw AuthorNotFound(authorId)
        }
    }

    private fun requireValidComment(noteId: Int, commentId: Int) {
        requireValidNote(noteId)
        if (commentId >= notes[noteId].comments.size) {
            throw NoteCommentNotFound(noteId, commentId)
        }
    }
}