import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import java.text.Normalizer

/**
 * A Kotlin port of JoshuaDoes's swear filter library
 */
class KFYourself(
    vararg uhohwords: String
) {
    private val badWords: MutableSet<String> = uhohwords.toMutableSet()
    private val lock = ReentrantReadWriteLock()

    private val multiCharLeet = mapOf(
        "vv" to "w",
        "uu" to "w",
        "//" to "w",
        "><" to "x",
        "1<" to "k",
        "|<" to "k",
        "()" to "o",
        "[]" to "o",
        "ph" to "f"
    )

    private val leetChars = mapOf(
        "4" to "a",
        "@" to "a",
        "8" to "b",
        "(" to "c",
        "<" to "c",
        "[" to "c",
        "3" to "e",
        "€" to "e",
        "6" to "g",
        "9" to "g",
        "#" to "h",
        "j" to "i",
        "0" to "o",
        "5" to "s",
        "$" to "s",
        "7" to "t",
        "+" to "t",
        "v" to "u",
        "2" to "z"
    )

    private val ambiguousLeetMap = mapOf(
        "!" to listOf("i", "l"),
        "|" to listOf("i", "l"),
        "1" to listOf("i", "l"),
        "]" to listOf("i", "l"),
        "}" to listOf("i", "l")
    )

    private val specialCharacters = setOf(
        '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';',
        '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~',
        '±', '÷', '×', '≠', '≈', '∑', '∏', '€', '£', '¥', '₹', '©', '®', '™', '§', '¶'
    )

    /**
     * Checks the input message for any bad words
     * @return List of tripped words found in the message
     */
    fun check(msg: String): List<String> {
        lock.read {
            if (badWords.isEmpty()) return emptyList()

            var message = msg.lowercase()
            message = normalizeLeetSpeak(message)
            message = normalizeString(message)

            val trippedWords = mutableListOf<String>()

            badWords.forEach { swear ->
                if (swear.isBlank()) return@forEach

                // Count special characters
                swear.count { it in specialCharacters }

                // Build regex pattern for the swear word with special characters in between
                val spacedPattern = swear.toCharArray().joinToString("[^a-zA-Z0-9]*")

                // Check for direct match
                if (message.contains(swear)) {
                    trippedWords.add(swear)
                    return@forEach
                }

                // Check for special character bypass
                if (Regex(spacedPattern).containsMatchIn(message)) {
                    trippedWords.add(swear)
                }
            }

            return trippedWords
        }
    }

    private fun normalizeLeetSpeak(message: String): String {
        var normalized = message.lowercase()

        // Handle multi-character replacements
        multiCharLeet.forEach { (leet, normal) ->
            normalized = normalized.replace(leet, normal)
        }

        // Handle single character replacements
        leetChars.forEach { (leet, normal) ->
            normalized = normalized.replace(leet, normal)
        }

        // Handle ambiguous characters
        ambiguousLeetMap.forEach { (leet, possibilities) ->
            if (normalized.contains(leet)) {
                possibilities.forEach { replacement ->
                    normalized = normalized.replace(leet, replacement)
                }
            }
        }

        return normalized
    }

    private fun normalizeString(input: String): String {
        // Normalize the string to remove accents
        return Normalizer.normalize(input, Normalizer.Form.NFD)
            .replace(Regex("[^\\p{ASCII}]"), "")
    }

    /**
     * Adds bad words to the filter
     */
    fun add(vararg badWords: String) {
        lock.write {
            this.badWords.addAll(badWords)
        }
    }

    /**
     * Removes bad words from the filter
     */
    fun delete(vararg badWords: String) {
        lock.write {
            this.badWords.removeAll(badWords.toSet())
        }
    }

    /**
     * Returns the list of active bad words
     */
    fun words(): List<String> {
        lock.read {
            return badWords.toList()
        }
    }
}
