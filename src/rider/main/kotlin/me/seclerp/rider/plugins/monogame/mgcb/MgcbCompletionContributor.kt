package me.seclerp.rider.plugins.monogame.mgcb

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbTypes

class MgcbCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(MgcbTypes.OPTION_KEY), OptionCompletionProvider())
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), KeywordCompletionProvider())
    }

    abstract class MgcbCompletionProvider(private val values: Array<String>)
        : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(params: CompletionParameters, ctx: ProcessingContext, resultSet: CompletionResultSet) {
            values.forEach {
                resultSet.addElement(LookupElementBuilder.create(it))
            }
        }

        companion object {
            private const val specialMarker = "IntellijIdeaRulezzz"

//            fun getPrefix(params: CompletionParameters): String {
//                val text = params.position.text
//                println(text)
//
//                if (text == specialMarker) {
//                    return specialMarker
//                }
//
//                if (!text.endsWith(specialMarker)) {
//                    return ""
//                }
//
//                return text.substring(0, text.length - specialMarker.length)
//            }
        }
    }

//    class KeywordCompletionProvider : MgcbCompletionProvider(Keywords, ::handleInsert) {
//        companion object {
//            private fun handleInsert(ctx: InsertionContext, item: LookupElement): Unit {
//                ctx.commitDocument()
//            }
//
//            val Keywords = arrayOf(
////                "\$set",
//                "\$if",
////                "\$endif"
//            )
//        }
//    }

    class OptionCompletionProvider : MgcbCompletionProvider(OptionTypes) {
        override fun addCompletions(
            params: CompletionParameters,
            ctx: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            val prefixMatcher = resultSet.prefixMatcher.cloneWithPrefix("/${resultSet.prefixMatcher.prefix}")
            super.addCompletions(params, ctx, resultSet.withPrefixMatcher(prefixMatcher))
        }

        companion object {
            val OptionTypes = arrayOf(
                "/outputDir",
                "/intermediateDir",
                "/rebuild",
                "/clean",
                "/incremental",
                "/reference",
                "/platform",
                "/profile",
                "/config",
                "/compress",
                "/importer",
                "/processor",
                "/processorParam",
                "/build",
                "/launchdebugger"
            )
        }
    }
//
//    class MgcbPrefixMatcher : PrefixMatcher() {
//        override fun prefixMatches(p0: String): Boolean {
//            TODO("Not yet implemented")
//        }
//
//        override fun cloneWithPrefix(p0: String): PrefixMatcher {
//            TODO("Not yet implemented")
//        }
//    }
}