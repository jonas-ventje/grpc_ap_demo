package Server
import HelloReply
import HelloRequest
import HelloServiceGrpcKt
import Server.Models.Point


class HelloService : HelloServiceGrpcKt.HelloServiceCoroutineImplBase() {
    override suspend fun hello(request: HelloRequest): HelloReply {
        println(request.name)
        Points.getInstance().addPoint( Point(X=200.0, Y=200.0) )
        return HelloReply.newBuilder()
            .setMessage("Hello, ${request.name}")
            .build()
    }
}