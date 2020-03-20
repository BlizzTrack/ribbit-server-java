package org.blizztrack.Factories

object SummaryFactory {
    private var cleaned: String = ""
    var seqn: String = ""
        private set(value) {
            field = value
        }
    var raw: String = ""
        private set(value) {
            field = value
        }
    // Key => code_file
    // Value => seqn
    private var summaryList: HashMap<String, String> = HashMap<String, String>()

    private fun createSummaryList() {
        summaryList.clear()

        val lines = cleaned.trimEnd().lines()
        var newLines = mutableListOf(*lines.toTypedArray())
        newLines.removeAt(0 )// drop item as it's just "header"
        /*
        wow|159810|cdn
        wow|161026|bgdl
        wow|166146|
         */
        var id = 0
        newLines.forEach {
            val line = it
            if (!line.startsWith("##")) {
                val items = line.trimEnd().split("|")

                var key = items[0]
                val seqn = items[1]
                if(items[2] == "") {
                    key += "_versions"
                } else {
                    key += "_" + items[2]
                }

                summaryList[key] = seqn
            }
        }

    }

    fun update(cleaned: String, seqn: String, raw: String): Boolean {
        if(seqn == this.seqn) {
            return false
        }

        this.cleaned = cleaned
        this.seqn = seqn
        this.raw = raw

        createSummaryList()
        VersionsFactory.add("summary", "summary", this.seqn, this.raw)

        return true
    }

    fun get(code: String, file: String): String? {
        val key = "${code}_$file".toLowerCase()
        if(summaryList.containsKey(key)) {
            return summaryList[key]
        }

        return null
    }
}