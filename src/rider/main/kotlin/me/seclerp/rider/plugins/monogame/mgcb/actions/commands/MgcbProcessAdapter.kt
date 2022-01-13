package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.util.Key

class MgcbProcessAdapter(private val commandString: String, private val listener: (CliCommandResult) -> Unit) : ProcessAdapter() {
    private val outputBuilder = StringBuilder()
    private val errorBuilder = StringBuilder()

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        val builder =
            when (outputType) {
                ProcessOutputTypes.STDERR -> errorBuilder
                else -> outputBuilder
            }

        builder.append(event.text)
    }

    override fun processTerminated(event: ProcessEvent) {
        val cliCommandResult = CliCommandResult(
            commandString,
            event.exitCode,
            outputBuilder.toString(),
            event.exitCode == 0,
            errorBuilder.toString()
        )

        listener(cliCommandResult)
    }
}