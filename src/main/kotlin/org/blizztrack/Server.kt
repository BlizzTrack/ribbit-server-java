package org.blizztrack

import mu.KotlinLogging
import org.blizztrack.workers.SummaryWorker
import java.net.InetAddress
import java.net.ServerSocket
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug")
    var region = "us"
    if (args.size == 1) region = args[0]

    val server = ServerSocket(1119, 50, InetAddress.getByName("127.0.0.1"))
    val summaryWorker = SummaryWorker(region)

    logger.info { "Ribbit running on port ${server.localPort}" }

    thread {
        summaryWorker.run()
    }

    while (true) {
        val client = server.accept()
        logger.info { "Client connected: ${client.inetAddress.hostAddress}:${client.port}" }

        // Run client in it's own thread.
        thread { ClientHandler(client, region).run() }
    }
}