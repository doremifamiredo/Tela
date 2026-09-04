import NotFoundException.*

object RealChatService : ChatService {
    private var chats = mutableListOf<Chat>()

    override fun initChat(talker1Id: Int, talker2Id: Int): Chat {
        val tryFound = foundChat(talker1Id, talker2Id)
        if (tryFound != null) return tryFound
        else {
            val newChat = Chat(talker1Id, talker2Id)
            chats.add(newChat)
            return newChat
        }
    }

    fun foundChat(talker1Id: Int, talker2Id: Int): Chat? {
        return chats.find(fun(chat: Chat) = chat.talker1Id == talker1Id && chat.talker2Id == talker2Id)
    }

    fun writeMessage() {

    }
}

interface ChatService {
    fun initChat(talker1Id: Int, talker2Id: Int): Chat
}

class ValidChatService(
    private val delegate: ChatService
) : ChatService {

    override fun initChat(talker1Id: Int, talker2Id: Int): Chat {
        val talkers = requireTalkers(talker1Id, talker2Id)
        return delegate.initChat(talkers[0], talkers[1])
    }

    fun Int.requireAuthorExists() {
        if (!WallService.checkAuthor(this)) {
            throw AuthorNotFound(this)
        }
    }

    private fun requireTalkers(talker1Id: Int, talker2Id: Int): List<Int> {
        talker1Id.requireAuthorExists()
        talker2Id.requireAuthorExists()
        var t1 = talker1Id
        var t2 = talker2Id
        if (talker2Id < talker1Id) {
            t1 = talker2Id
            t2 = talker1Id
        }
        return listOf(t1, t2)
    }
}
