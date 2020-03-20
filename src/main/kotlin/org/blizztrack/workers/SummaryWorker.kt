package org.blizztrack.workers

import mu.KotlinLogging
import org.blizztrack.Factories.SummaryFactory
import org.blizztrack.network.command.Command
import org.blizztrack.ribbit.Client
import org.blizztrack.ribbit.getFileSeqn
import org.blizztrack.ribbit.messageBody
import javax.mail.Message

class SummaryWorker(private val region: String) {
    private val command = Command("v1", "summary")
    private var running = false
    private val logger = KotlinLogging.logger { }

    fun run() {
        running = true;

        while (running) {
            try {
                val summary = Client(region).get(command)

                val summaryBody = summary?.let { messageBody(it) }
                val seqn = summaryBody?.let { getFileSeqn(it) }
                if (seqn != null) {
                    if(SummaryFactory.update(summaryBody, seqn, summary)) {
                        logger.info { "Summary updated to seqn: $seqn" }
                    }
                }


            } catch (ex: Exception) {
                logger.error(ex) { "failed to get summary: $ex" }
            }

            Thread.sleep(5 * 1000)
        }
    }
}