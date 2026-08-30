import io.github.serpro69.kfaker.Faker
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WallServiceTest {
    val faker = Faker()

    @Before
    fun initPost() {
        WallService.resetAllFields()
        WallService.addPost(1, faker.lorem.words())
    }

    @Test
    fun addingPostIncreasesFollowingId() {
        val expectedId = WallService.nextId + 1
        WallService.addPost(1, faker.lorem.words())
        assertEquals(expectedId, WallService.nextId)
    }

    @Test
    fun updatingExistingPost() {
        val expectedId = WallService.nextId
        WallService.addPost(1, faker.lorem.words())
        assertTrue(WallService.updatePost(expectedId, faker.lorem.words()))
    }

    @Test
    fun updatingNonExistingPost() {
        val expectedId = WallService.nextId + 1
        WallService.addPost(1, faker.lorem.words())
        assertFalse(WallService.updatePost(expectedId, faker.lorem.words()))
    }

    @Test
    fun successfullyAddingComment() {
        val text = faker.lorem.words()
        val actual = WallService.createComment(1, Comment(1, text))
        assertEquals(Comment(1, text), actual)
    }

    @Test(expected = NotFoundException.PostNotFound::class)
    fun shouldThrow() {
        val nonExistentId = WallService.getLastPostId() + 1
        WallService.createComment(nonExistentId, Comment(1, faker.lorem.words()))
    }

    @Test
    fun successfullyReportComment() {
        val existentId = WallService.getLastPostId()
        WallService.createComment(existentId, Comment(1, faker.lorem.words()))
        assertTrue(WallService.reportComment(0, 0, 1))
    }

    @Test(expected = WrongReasonException::class)
    fun reportWrong() {
        val existentId = WallService.getLastPostId()
        WallService.createComment(existentId, Comment(1, faker.lorem.words()))
        WallService.reportComment(existentId, 1, 9)
    }

    @Test(expected = NotFoundException.PostNotFound::class)
    fun reportNonExistingPost() {
        val existentId = WallService.getLastPostId()
        WallService.createComment(existentId, Comment(1, faker.lorem.words()))
        WallService.reportComment(existentId + 1, 1, 1)
    }

    @Test(expected = NotFoundException.PostCommentNotFound::class)
    fun reportWrongComment() {
        val postId = WallService.getLastPostId()
        val commentId = 1
        WallService.createComment(postId, Comment(commentId, faker.lorem.words()))
        WallService.reportComment(postId, commentId + 1, 1)
    }

    @Test(expected = NotFoundException.NullComment::class)
    fun reportNonExistingComment() {
        val postId = WallService.getLastPostId()
        WallService.reportComment(postId, 1, 1)
    }
}