package org.blizztrack.network.command

class Command {
    var mode = ""
        get() = field
        private set(value) {
            field = value
        }
    var product: String = ""
        get() = field
        private set(value) {
            field = value
        }
    var file: String = ""
        public get() = field
        private set(value) {
            field = value
        }

    constructor(mode: String, product: String, file: String) {
        this.mode = mode
        this.product = product
        this.file = file
    }

    constructor(vararg command: String) {
        when (command.size) {
            2 -> {
                mode = command[1]
            }
            4 -> {
                mode = command[1]
                product = command[2]
                file = command[3]
            }
            else -> {
                throw Exception("invalid command size")
            }
        }
    }

    constructor(command: String) : this(*command.split("/").toTypedArray()) {
    }

    override fun toString(): String {
        if(file == "" && product == "") {
            return "v1/${mode}";
        }

        return "v1/${mode}/${product}/${file}"
    }
}