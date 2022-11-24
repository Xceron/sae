class NumberSquareDecryptor(private var encryptedSquare: Array<String>?) {
    /**
     * Checks whether the given input array is a solved Square.
     *
     * A square is solved, iff all apply:
     * A + B = C
     * +   +   +
     * D + E = F
     * =   =   =
     * G + H = I
     *
     * @param square square to check solution from
     * @return
     */
    private fun isValidSolution(square: IntArray): Boolean {
        return (square[0] + square[1] == square[2])
                && (square[3] + square[4] == square[5])
                && (square[0] + square[3] == square[6])
                && (square[1] + square[4] == square[7])
                && (square[2] + square[5] == square[8])
                && (square[6] + square[7] == square[8])
    }

    /**
     * Function which solves [encryptedSquare]
     *
     * @return Solved Square (if possible) else null
     */
    fun decrypt(): IntArray? {
        if (encryptedSquare == null) {
            return null
        }
        val letters = charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J')
        var currentSolution: IntArray
        val permutations = generatePermutation(letters.toTypedArray()).iterator()
        while (permutations.hasNext()) {
            val permutation = permutations.next()
            // Apply a permutation of letters to its numberSquare in digits
            currentSolution = encryptedSquare!!.copyOf().map { string ->
                val intString = string.map { permutation.indexOf(it) }.joinToString(separator = "")
                intString.toInt()
            }.toIntArray()
            if (isValidSolution(currentSolution)) return currentSolution
        }
        return null
    }


    /**
     * Generator function which generates all permutations of a given array using QuickPerm.
     * See https://www.quickperm.org/quickperm.php
     *
     * @param T generic Type
     * @param inputArray input to generate permutations for
     */
    private fun <T> generatePermutation(inputArray: Array<T>) = sequence {
        yield(inputArray)
        val n = inputArray.size
        var i = 1
        val p = IntArray(inputArray.size) { 0 }
        while (i < n) {
            if (p[i] < i) {
                val j = if ((i % 2) == 0) 0 else p[i]
                // swap
                inputArray[i] = inputArray[j].also { inputArray[j] = inputArray[i] }
                yield(inputArray)
                p[i]++
                i = 1
            } else {
                p[i] = 0
                i++
            }
        }
    }
}