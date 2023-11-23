package Client

import EmptyDTO
import PointDTO
import PointServiceGrpc
import PointServiceGrpcKt
import Server.IObserver
import Server.MyJavaFXApplication
import Server.Points
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Closeable
import java.util.concurrent.TimeUnit

class ClientJavaFX : Application(){
    lateinit var gc: GraphicsContext
    lateinit var channel: ManagedChannel
    lateinit var client: PointClient

    override fun start(p0: Stage?) {
        channel = ManagedChannelBuilder.forAddress("localhost", 15001)
            .usePlaintext()
            .build()

        client = PointClient(channel)


        p0?.setTitle("Drawing Operations Test")
        val root = Group()
        val canvas = Canvas(600.0, 600.0)
        gc = canvas.getGraphicsContext2D()
        root.getChildren().add(canvas)
        p0?.setScene(Scene(root))
        p0?.show()

        canvas.setOnMouseClicked { event: MouseEvent ->
            GlobalScope.launch {
                client.addPoint()
            }
        }
    }
}

/*fun asyncHelloClient() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 15001)
        .usePlaintext()
        .build()

    PointServiceGrpc.newStub(channel).addPoint(
        PointDTO.newBuilder()
            .setX(50.0)
            .setY(50.0)
            .build(),
        object : StreamObserver<EmptyDTO> {
            override fun onNext(response: EmptyDTO?) {
                println("response")
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

}*/

class PointClient(private val channel: ManagedChannel) : Closeable {
    private val stub: PointServiceGrpcKt.PointServiceCoroutineStub =
        PointServiceGrpcKt.PointServiceCoroutineStub(channel)

    suspend fun addPoint() {
        val request = PointDTO.newBuilder().setX(40.0).setY(40.0).build()
        val response = stub.addPoint(request)
        //println("Received: ${response.message}")
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun main(args: Array<String>) {
    Application.launch(ClientJavaFX::class.java)
}