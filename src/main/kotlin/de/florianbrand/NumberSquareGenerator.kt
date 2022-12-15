package de.florianbrand

import kotlin.math.pow
import kotlin.random.Random

class NumberSquareGenerator(private val numberOfDigits: Int) {
    private val numberSquare: IntArray = generateSquare()

    /**
     * Generates a new NumberSquare
     *
     * @return NumberSquare
     */
    private fun generateSquare(): IntArray {
        // maxSummand denote the upper bound of the inner square, therefore the max number of digits
        // are 1 less than numberOfDigits so that the sums are still within the bound of numberOfDigits
        val maxSummand = 10.0.pow(numberOfDigits - 1).toInt() - 1
        val summands = IntArray(4) { Random.nextInt(0, maxSummand) }
        val out = intArrayOf(
            summands[0], summands[1], summands[0] + summands[1],
            summands[2], summands[3], summands[2] + summands[3],
            summands[0] + summands[2], summands[1] + summands[3], 0
        )
        out[8] = out[2] + out[5]
        println("Generated square: ${out.contentToString()}")
        return out
    }

    /**
     * Converts the numbers from NumberSquare into encoded characters for each digit
     *
     * @param fillZeros whether to add leading zeros
     * @return encrypted array
     */
    fun getEncryptedSquare(fillZeros: Boolean = true): Array<String> {
        val letters = charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J')
        letters.shuffle()
        // Encrypt whole array
        return numberSquare.map { number ->
            // Change single number (111) to String ("AAA")
            val numberString = number.toString().map { letters[it.digitToInt()] }.joinToString(separator = "")
            // Add leading zeros
            val out: String = if (fillZeros) {
                numberString.padStart(numberOfDigits, letters[0])
            } else {
                numberString
            }
            out
        }.toTypedArray()
    }
}