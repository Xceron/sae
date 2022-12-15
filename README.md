# NumberSquare

Project designed to solve verbal arithmetics puzzles (called NumberSquares), given in a 3x3 grid,
using [Kotlin](https://kotlinlang.org/).

## Structure

The project consists of 3 parts:

1. The `NumberSquareGenerator` which takes an amount of digits as an input and generates (and encrypts) a new
   NumberSquare. Decrypted NumberSquares are represented as `Array<String>`, while solved/encrypted NumberSquares are
   represented as `IntArray`
2. The `NumberSquareDecryptor` takes an encrypted NumberSquare and solves it by using Bruteforce, i.e. trying out all
   Permutations using [QuickPerm](https://www.quickperm.org/quickperm.php).
3. `Main.kt` is used as a starting point to show the usage of the other classes and is capable of reading/writing the
   encrypted NumberSquares to files. Encrypted NumberSquares are represented by the letters A-J. The following file
   formats are implemented:
   - `.csv` Files: The NumberSquare is written in a single line with `,` as delimiter
   - `.xlsx` Files: The NumberSquare is written in a 3x3 grid in a single Excel sheet. The grid represents the
     NumberSquare, i.e. the right-most column and the lowest row are the respective sums of the inner square.

## gRPC

To execute any command using `.\gradlew` on Windows, use `.\gradlew.bat` instead.

Generate stubs with Gradle using `.\gradlew installDist`.

To run the server, execute `.\gradlew runServer`.

To run the client, execute `.\gradlew runClient`.