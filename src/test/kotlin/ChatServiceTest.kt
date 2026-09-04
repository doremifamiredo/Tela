import io.github.serpro69.kfaker.Faker
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChatServiceTest {
    val chatService: ChatService = ValidChatService(RealChatService)
    val faker = Faker()
    var existentTalker1 = 0
    var existentTalker2 = 0
    val text = faker.lorem.words()

    @Before
    fun setUp() {
        WallService.resetAllFields()
        existentTalker1 = WallService.addAuthor(faker.name.name())
        existentTalker2 = WallService.addAuthor(faker.name.name())
    }

    @Test(expected = NotFoundException.AuthorNotFound::class)
    fun initChatNonExistentAuthor() {
        chatService.initChat(existentTalker1 + faker.random.nextInt(), existentTalker2)
    }
}