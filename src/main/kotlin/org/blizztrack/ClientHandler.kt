package org.blizztrack

import org.blizztrack.network.command.Command
import org.blizztrack.ribbit.Client
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler(private val client: Socket, private val region: String) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()

    fun run() {
        try {
            val text = reader.nextLine()
            val command = Command(text)

            val results = Client(region).get(command)

            results?.let { write(it) }

            client.close()
        } catch(ex: Exception) {
            println("failed to process client: " + ex.printStackTrace());
            ex.message?.let { write(it) }
            client.close()
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
}