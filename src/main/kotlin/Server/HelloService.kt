package Server
import HelloReply
import HelloRequest
import HelloServiceGrpcKt


class HelloService : HelloServiceGrpcKt.HelloServiceCoroutineImplBase() {
    override suspend fun hello(request: HelloRequest): HelloReply {
        println(request.name)
        Points.getInstance().addPoint( Point(X=200, Y=200) )
        return HelloReply.newBuilder()
            .setMessage("Hello, ${request.name}")
            .build()
    }
}