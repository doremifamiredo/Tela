import java.io.File

sealed class Attachment(type: String) {
    class Photo(val file: File, val id: Int): Attachment("photo")
    class Audio(val file: File, val id: Int, val title: String, val duration: Int): Attachment("audio")
    class Video(val file: File, val id: Int, val title: String, val duration: Int): Attachment("video")
    class Doc(val file: File, val id: Int, val ext: String, val size: Int): Attachment("doc")
    class Link(val url: String, val title: String): Attachment("link")
}

