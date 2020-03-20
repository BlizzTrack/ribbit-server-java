package org.blizztrack.ribbit

import mu.KotlinLogging
import org.blizztrack.network.command.Command
import org.slf4j.LoggerFactory
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset
import java.sql.Time
import java.util.*

// Blizzard closes the connection after they send data thus this cannot be a singleton
class Client(region: String) {
    private val server = "${region}.version.battle.net"
    private val port = 1119
    private val connection: Socket = Socket(server, port)
    private val reader: Scanner = Scanner(connection.getInputStream())
    private val writer: OutputStream = connection.getOutputStream()

    private val logger = KotlinLogging.logger {}

    fun get(command: Command): String? {
        try {
            write(command.toString())

            var ret = ""
            while (reader.hasNext()) {
                ret += reader.nextLine() + "\r\n"
            }
            // Always close just in case
            connection.close()

            logger.debug { "Handled ribbit $command" }
            return ret.trimEnd()
        } catch (ex: Exception) {
            logger.error(ex) { "$command failed with $ex"}
            return null
        }
    }

    private fun write(message: String) {
        writer.write((message + "\r\n").toByteArray(Charset.defaultCharset()))
    }
}