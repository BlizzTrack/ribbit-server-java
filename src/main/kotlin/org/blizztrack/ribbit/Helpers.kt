package org.blizztrack.ribbit

import org.apache.commons.mail.util.MimeMessageParser
import java.io.ByteArrayInputStream
import java.util.*
import javax.mail.Session
import javax.mail.internet.MimeMessage


fun messageBody(rawMessage: String): String {
    val body = ByteArrayInputStream(rawMessage.toByteArray())
    val session = Session.getDefaultInstance(Properties())
    val parser = MimeMessageParser(MimeMessage(session, body))
    parser.parse()

    return parser.plainContent
}

fun getFileSeqn(rawMessage: String): String? {
    val pattern = Regex("""\#\#\s?seqn\s?=\s?([0-9]*)""")
    val found = pattern.find(rawMessage)!!
    val (id) = found.destructured

    return id
}

fun getSeqn(rawMessage: String): String {
    val body = messageBody(rawMessage)

    val seqn = getFileSeqn(body)

    if(seqn != null) {
        return seqn
    }

    return ""
}