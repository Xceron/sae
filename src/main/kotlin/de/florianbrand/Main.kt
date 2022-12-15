package de.florianbrand

import io.github.evanrupert.excelkt.workbook
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


/**
 * Writes the (encrypted) NumberSquare to the given OutputStream
 *
 * @param numberSquare  numberSquare to write
 */
private fun OutputStream.writeCsv(numberSquare: Array<String>) {
    val writer = bufferedWriter()
    val csvString = if (numberSquare.isArrayOf<String>()) {
        numberSquare.joinToString { "\"$it\"" }
    } else {
        numberSquare.joinToString(separator = ", ")
    }
    writer.write(csvString)
    writer.flush()
}

/**
 * Writes the NumberSquare to a file
 *
 * @param file file to write into
 */
fun writeCsv(file: File, numberSquare: Array<String>) {
    FileOutputStream(file).apply { writeCsv(numberSquare) }
}

/**
 * Writes the given (encrypted) NumberSquare into an Excel file
 *
 * @param file
 * @param numberSquare
 */
fun writeExcel(file: File, numberSquare: Array<String>) {
    workbook {
        sheet("numberSquare") {
            for (i in 0..2) {
                row {
                    for (j in 0..2) {
                        val index = i * 3 + j
                        cell(numberSquare[index])
                    }
                }
            }
        }
    }.write(file.toString())
}

/**
 * Reads the given Excel File
 *
 * @param file  Excel file with encrypted NumberSquare
 * @return  NumberSquare
 */
fun readExcel(file: File): Array<String> {
    val workbook = WorkbookFactory.create(file)
    val sheet = workbook.getSheet("numberSquare")
    val out = Array(9) { "" }
    val dataFormatter = DataFormatter()
    var i = 0
    for (row in sheet) {
        for (cell in row) {
            out[i] = dataFormatter.formatCellValue(cell)
            i++
        }
    }
    return out
}

/**
 * Reads the given CSV file
 *
 * @param file  CSV file with encrypted NumberSquare
 * @return  NumberSquare
 */
fun readCSV(file: File): Array<String> {
    val input = file.useLines { it.firstOrNull() } ?: throw Exception("Input is empty")
    return input
        .split(", ")
        .map { it.removeSurrounding("\"") }
        .toTypedArray()
}


fun main() {
    val encryptedNumberSquare = NumberSquareGenerator(3).getEncryptedSquare()
    println("Generated, encrypted square: ${encryptedNumberSquare.contentToString()}")
    val csvFile = File("encrypted.csv")
    val excelFile = File("ecrypted.xlsx")
    writeCsv(csvFile, encryptedNumberSquare)
    writeExcel(excelFile, encryptedNumberSquare)

    val csvSolver = NumberSquareDecryptor(readCSV(csvFile))
    println("Solution from CSV: ${csvSolver.decrypt().contentToString()}")
    val excelSolver = NumberSquareDecryptor(readExcel(excelFile))
    println("Solution from Excel: ${excelSolver.decrypt().contentToString()}")
}