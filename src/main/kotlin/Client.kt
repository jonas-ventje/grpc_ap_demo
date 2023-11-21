import HelloServiceGrpc.HelloServiceStub
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.io.Closeable
import java.util.concurrent.TimeUnit

fun asyncHelloClient() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 15001)
        .usePlaintext()
        .build()


    HelloServiceGrpc.newStub(channel).hello(
        HelloRequest.newBuilder().setName("test").build(), object : StreamObserver<HelloReply> {
            override fun onNext(response: HelloReply?) {
                println("response")
                println(response?.message)
            }

            override fun onError(throwable: Throwable?) {
                println("error")
                throwable?.printStackTrace()
            }

            override fun onCompleted() {
                println("Completed!")
            }
        }
    )

    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)

}

fun main(args: Array<String>) {
    asyncHelloClient()
}

/*
class HelloWorldClient(private val channel: ManagedChannel) : Closeable {
    private val stub: HelloServiceStub = HelloServiceStub.newStub(channel)

    suspend fun greet(name: String) {
        val request = HelloRequest { this.name = name }
        val response = stub.hello(request)
        println("Received: ${response.message}")
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun Server.main(args: Array<String>) {
    val port = 15001

    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()

    val client = HelloWorldClient(channel)

    val user = args.singleOrNull() ?: "world"
    client.greet(user)
}*/
