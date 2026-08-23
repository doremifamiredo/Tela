import io.github.serpro69.kfaker.Faker
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import NotFoundException.*

class NoteServiceTest() {
    val noteService: NoteService = ValidatingNoteService(RealNoteService)
    val faker = Faker()
    var existentAuthor = 0
    var existentNote = 0
    val text = faker.lorem.words()

    @Before
    fun setUp() {
        noteService.resetNotes()
        WallService.resetAllFields()
        existentAuthor = WallService.addAuthor(faker.name.name())
        existentNote = noteService.add(existentAuthor, text)
    }

    @Test
    fun addSuccessfully() {
        assertEquals(1, noteService.add(existentAuthor, text))
    }

    @Test(expected = AuthorNotFound::class)
    fun nonExistentAuthorAddsNote() {
        noteService.add(existentAuthor + 1, text)
    }

    @Test
    fun successfullyCreateComment() {
        assertEquals(
            0, noteService.createComment(existentNote, existentAuthor, text)
        )
    }

    @Test(expected = AuthorNotFound::class)
    fun nonExistentAuthorAddsComment() {
        noteService.createComment(existentNote, existentAuthor + 1, text)
    }

    @Test(expected = NoteNotFound::class)
    fun addingCommentToNonExistentNote() {
        noteService.createComment(existentNote + 1, existentAuthor, text)
    }

    @Test(expected = NoteDeleted::class)
    fun addingCommentToDeletedNote() {
        noteService.delete(existentNote)
        noteService.createComment(existentNote, existentAuthor, text)
    }


    @Test
    fun successfullyDeleteNote() {
        assertTrue(noteService.delete(existentNote))
    }

    @Test(expected = NoteNotFound::class)
    fun deleteNonExistentNote() {
        noteService.delete(existentNote + 1)
    }

    @Test(expected = NoteDeleted::class)
    fun deleteAlreadyDeletedNote() {
        noteService.delete(existentNote)
        noteService.delete(existentNote)
    }

    @Test
    fun successfullyDeleteCommentToNote() {
        noteService.createComment(existentNote, existentAuthor, text)
        assertTrue(noteService.deleteComment(existentNote, existentNote))
    }

    @Test(expected = NoteDeleted::class)
    fun deleteCommentOfDeletedNote() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.delete(existentNote)
        noteService.deleteComment(existentNote, existentComment)
    }

    @Test(expected = NoteCommentNotFound::class)
    fun deleteNonExistentComment() {
        noteService.deleteComment(existentNote, faker.random.nextInt())
    }

    @Test(expected = NoteNotFound::class)
    fun deleteCommentOfNonExistentNote() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.deleteComment(existentNote + 1, existentComment)
    }

    @Test(expected = NoteCommentDeleted::class)
    fun deleteAlreadyDeletedComment() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.deleteComment(existentNote, existentComment)
        noteService.deleteComment(existentNote, existentComment)
    }

    @Test
    fun successfullyEditNoteReturnTrue() {
        assertTrue(noteService.edit(existentNote, faker.lorem.words()))
    }

    @Test
    fun successfullyEditNoteAssertText() {
        val newText = faker.lorem.words()
        noteService.edit(existentNote, newText)
        assertEquals(newText, noteService.getById(existentNote).text)
    }

    @Test(expected = NoteNotFound::class)
    fun editNonExistentNote() {
        noteService.edit(existentNote + 1, faker.lorem.words())
    }

    @Test(expected = NoteDeleted::class)
    fun editDeletedNote() {
        noteService.delete(existentNote)
        noteService.edit(existentNote, faker.lorem.words())
    }

    @Test
    fun successfullyEditNoteCommentReturnTrue() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        assertTrue(noteService.editComment(existentNote, existentComment, faker.lorem.words()))
    }

    @Test
    fun successfullyEditNoteCommentAssertText() {
        val newText = faker.lorem.words()
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.editComment(existentNote, existentComment, newText)
        assertEquals(newText, noteService.getById(existentNote).comments[existentComment].text)
    }

    @Test(expected = NoteNotFound::class)
    fun editNonExistentNoteComment() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.editComment(existentNote + 1, existentComment, faker.lorem.words())
    }

    @Test(expected = NoteCommentNotFound::class)
    fun editNonExistentComment() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.editComment(existentNote, existentComment + 1, faker.lorem.words())
    }

    @Test(expected = NoteDeleted::class)
    fun editCommentOfDeletedNote() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.delete(existentNote)
        noteService.editComment(existentNote, existentComment, faker.lorem.words())
    }

    @Test(expected = NoteCommentDeleted::class)
    fun editDeletedComment() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.deleteComment(existentNote, existentComment)
        noteService.editComment(existentNote, existentComment, faker.lorem.words())
    }

    @Test
    fun getNote() {
        assertEquals(text, noteService.get()[existentNote].text)
    }

    @Test
    fun getNoteById() {
        assertEquals(text, noteService.getById(existentNote).text)
    }

    @Test(expected = NoteNotFound::class)
    fun getNonExistentNoteById() {
        noteService.getById(existentNote + 1)
    }

    @Test(expected = NoteDeleted::class)
    fun getDeletedNote() {
        noteService.delete(existentNote)
        noteService.getById(existentNote)
    }

    @Test
    fun successfullyGetComments() {
        val newText = faker.lorem.words()
        val existentComment = noteService.createComment(existentNote, existentAuthor, newText)
        assertEquals(newText, noteService.getComments(existentNote)[existentComment].text)
    }

    @Test(expected = NoteNotFound::class)
    fun getNonExistentNoteComments() {
        noteService.getComments(existentNote + 1)
    }

    @Test(expected = NoteDeleted::class)
    fun getDeletedNoteComments() {
        noteService.delete(existentNote)
        noteService.getComments(existentNote)
    }

    @Test
    fun successfullyRestoreComment() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.deleteComment(existentNote, existentComment)
        assertTrue(noteService.restoreComment(existentNote, existentComment))
    }

    @Test(expected = NoteDeleted::class)
    fun restoreCommentOfDeletedNote() {
        noteService.delete(existentNote)
        noteService.restoreComment(existentNote, existentAuthor)
    }

    @Test(expected = NoteNotFound::class)
    fun restoreCommentOfNonExistentNote() {
        val existentComment = noteService.createComment(existentNote, existentAuthor, text)
        noteService.restoreComment(existentNote + 1, existentComment)
    }
}
