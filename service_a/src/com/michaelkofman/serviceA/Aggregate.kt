package com.michaelkofman.serviceA

import com.michaelkofman.dummy.DummyEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate
import java.io.Serializable


class DummyCommand(@TargetAggregateIdentifier val name: String): Serializable

@Aggregate
internal class DummyCollector: Serializable {
    @AggregateIdentifier
    private lateinit var name: String

    constructor()

    @CommandHandler
    constructor(command: DummyCommand) {
        name = command.name

        apply(DummyEvent(id = command.name, value = SimpleRandomSentences.randomSentence()))
    }
}

object SimpleRandomSentences {
    private val nouns = arrayOf("farmer", "rooster", "judge", "man", "maiden", "cow", "dog", "cat", "cheese")

    private val verbs = arrayOf("kept", "waked", "married", "milked", "tossed", "chased", "lay in")

    private val modifiers = arrayOf("that crowed in the morn", "sowing his corn", "all shaven and shorn", "all forlorn", "with the crumpled horn")

    internal fun randomSentence(): String {
        var sentence = ""
        sentence += randomSimpleSentence()

        /* Optionally, "and" followed by another simple sentence.*/

        if (Math.random() > 0.75) { // 25% of sentences continue with another clause.
            sentence += " and "
            sentence += randomSimpleSentence()
        }
        return sentence
    }

    internal fun randomSimpleSentence(): String {
        var sentence = "this is "
        if (Math.random() > 0.15) { // 85% of sentences have a noun phrase.
            sentence += randomNounPhrase()
        }
        sentence += "the house that Jack built"
        return sentence
    }

    internal fun randomNounPhrase(): String {

        val n = (Math.random() * nouns.size).toInt()
        var sentence = "the " + nouns[n]

        /* Optionally, a modifier. */

        if (Math.random() > 0.75) { // 25% chance of having a modifier.
            val m = (Math.random() * modifiers.size).toInt()
            sentence += " " + modifiers[m]
        }

        /* "that", followed by a random verb */

        val v = (Math.random() * verbs.size).toInt()
        sentence += " that " + verbs[v] + " "

        /* Another random noun phrase */

        if (Math.random() > 0.5) {  // 50% chance of having another noun phrase.
            sentence += randomNounPhrase()
        }
        return sentence
    }

}