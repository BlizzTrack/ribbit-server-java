import java.io.File
import java.nio.file.Paths

object VersionsFactory {
    fun get(code: String, file: String, seqn: String) : String? {
        val path = Paths.get("files", file, "${code}_${seqn}.bmime");
        val localFile = File(path.toString())
        createFolder(file)

        if(!localFile.exists()) {
            return null
        }

        val lines = readLines(localFile)

        return lines.joinToString(separator = "\r\n")
    }

    fun add(code: String, file: String, seqn: String, raw: String) {
        createFolder(file)
        val path = Paths.get("files", file, "${code}_${seqn}.bmime");
        val localFile = File(path.toString())

        localFile.writeText(raw)
    }

    private fun readLines(file: File): List<String>
            = file.useLines { it.toList() }

    private fun createFolder(file: String) {
        val path = Paths.get("files", file)
        val localFile = File(path.toString())
        localFile.mkdirs()
    }
}