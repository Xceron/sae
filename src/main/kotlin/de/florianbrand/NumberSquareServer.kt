package de.florianbrand

import NumberSquareGrpcKt
import Numbersquare
import io.grpc.Server
import io.grpc.ServerBuilder

class NumberSquareServer(
    private val port: Int,
    val server: Server = ServerBuilder.forPort(port).addService(NumberSquareService()).build()
) {

    // start grpc server
    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(Thread {
            println("*** shutting down gRPC server since JVM is shutting down")
            this@NumberSquareServer.stop()
            println("*** server shut down")
        })
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }


    internal class NumberSquareService : NumberSquareGrpcKt.NumberSquareCoroutineImplBase() {
        override suspend fun generateNumberSquare(request: Numbersquare.NumberRequest): Numbersquare.GeneratedNumberSquareReply {
            val number = request.number
            // set default to false
            val insertLeadingZeros = if (request.hasLeadingZeros()) request.leadingZeros else false
            val numberSquare = NumberSquareGenerator(number).getEncryptedSquare(insertLeadingZeros)
            return Numbersquare.GeneratedNumberSquareReply.newBuilder()
                .setNumberSquare(numberSquare.joinToString(separator = ", ")).build()
        }

        override suspend fun solveNumberSquare(request: Numbersquare.NumberSquareRequest): Numbersquare.SolvedNumberSquareReply {
            val numberSquareArray = request.encryptedSquare
                .replace("\\s".toRegex(), "")
                .split(",")
                .toTypedArray()
            val solution = NumberSquareDecryptor(numberSquareArray).decrypt()
            val returnString = solution?.joinToString(separator = ", ") ?: "No solution found"
            return Numbersquare.SolvedNumberSquareReply.newBuilder().setDecryptedSquare(returnString).build()
        }
    }
}

fun main() {
    val server = NumberSquareServer(8980)
    server.start()
    server.server.awaitTermination()
}