import io.github.serpro69.kfaker.Faker
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WallServiceTest {
    val faker = Faker()

    @Before
    fun initPost() {
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
}