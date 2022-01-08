package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import java.io.IOException
import java.util.concurrent.TimeUnit

open class CliCommand(private val fullCommand: String, private val executeDetached: Boolean = false) {
    fun execute(): CliCommandResult {
        return try {
            val runtime = Runtime.getRuntime()
            val proc = runtime.exec(fullCommand)

            if (!executeDetached) {
                proc.waitFor(60, TimeUnit.MINUTES)

                val output = proc.inputStream.bufferedReader().readText()
                val error = proc.errorStream.bufferedReader().readText()
                val exitCode = proc.exitValue()

                CliCommandResult(fullCommand, exitCode, output, exitCode == 0, error)
            } else {
                CliCommandResult(fullCommand, 0, "", true)
            }
        } catch(e: IOException) {
            e.printStackTrace()

            CliCommandResult(fullCommand, -1, e.toString(), false)
        }
    }
}