package de.florianbrand

import NumberSquareGrpcKt
import Numbersquare
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.Closeable

class NumberSquareClient(private val channel: ManagedChannel) : Closeable {
    private val stub = NumberSquareGrpcKt.NumberSquareCoroutineStub(channel)
    suspend fun generateNumberSquare(number: Int, insertLeadingZeros: Boolean): String {
        val request =
            Numbersquare.NumberRequest.newBuilder().setNumber(number).setLeadingZeros(insertLeadingZeros).build()
        return stub.generateNumberSquare(request).numberSquare
    }

    suspend fun solveNumberSquare(numberSquare: String): String {
        val request = Numbersquare.NumberSquareRequest.newBuilder().setEncryptedSquare(numberSquare).build()
        return stub.solveNumberSquare(request).decryptedSquare
    }

    override fun close() {
        channel.shutdown()
    }
}

suspend fun main() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 8980).usePlaintext().build()
    val client = NumberSquareClient(channel)
    val generatedNumberSquare = client.generateNumberSquare(5, false)
    println("Generated number square: $generatedNumberSquare")
    val solvedNumberSquare = client.solveNumberSquare(generatedNumberSquare)
    println("Solved number square: $solvedNumberSquare")
    client.close()
}