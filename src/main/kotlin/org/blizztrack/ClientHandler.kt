package org.blizztrack

import mu.KotlinLogging
import org.blizztrack.Factories.SummaryFactory
import org.blizztrack.network.command.Command
import org.blizztrack.ribbit.Client
import org.blizztrack.ribbit.getSeqn
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler(private val client: Socket, private val region: String) {
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private val logger = KotlinLogging.logger {}

    fun run() {
        try {
            val text = reader.nextLine()
            val command = Command(text)
            if(command.toString().contains("summary")) {
                write(SummaryFactory.raw)
                client.close()
                return
            }

            val prodSeqn = SummaryFactory.get(command.product, command.file)
            var contents: String? = null
            if(prodSeqn != null) {
                contents = VersionsFactory.get(command.product, command.file, prodSeqn)
            } else {
                client.close()
                return
            }

            if(contents == null) {
                val results = Client(region).get(command)
                contents = results
                val seqn = contents?.let { getSeqn(it) }

                if (seqn != null) {
                    if (contents != null) {
                        VersionsFactory.add(command.product, command.file, seqn, contents)
                    }
                }
            }

            if (contents != null) {
                write(contents)
            }

            client.close()
        } catch(ex: Exception) {
            logger.error(ex) { "failed to process client: $ex" }
            ex.message?.let { write(it) }
            client.close()
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
}