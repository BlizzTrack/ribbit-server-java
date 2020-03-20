package org.blizztrack

import java.net.InetAddress
import java.net.ServerSocket
import kotlin.concurrent.thread


fun main(args: Array<String>) {
    val server = ServerSocket(1119, 50,  InetAddress.getByName("127.0.0.1"))
    var region = "us"
    if(args.size == 1) {
        region = args[0]
    }

    println("Ribbit running on port ${server.localPort}")

    while (true) {
        val client = server.accept()
        println("Client connected: ${client.inetAddress.hostAddress}:${client.port}")

        // Run client in it's own thread.
        thread { ClientHandler(client, region).run() }
    }
}