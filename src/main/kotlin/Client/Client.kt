package Client

import PointDTO
import PointServiceGrpcKt
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.stage.Stage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.Closeable
import java.util.concurrent.TimeUnit


class ClientJavaFX : Application(){
    lateinit var gc: GraphicsContext
    lateinit var channel: ManagedChannel
    lateinit var client: PointClient
    lateinit var canvas: Canvas;

    override fun start(p0: Stage?) {
        channel = ManagedChannelBuilder.forAddress("localhost", 15001)
            .usePlaintext()
            .build()

        client = PointClient(channel)


        p0?.setTitle("Drawing Operations Test")
        val root = Group()
        canvas = Canvas(600.0, 600.0)
        gc = canvas.getGraphicsContext2D()
        root.getChildren().add(canvas)
        p0?.setScene(Scene(root))
        p0?.show()

        val mouseMovements = MutableSharedFlow<PointDTO>()
        canvas.setOnMouseClicked { event ->
            val x = event.x
            val y = event.y
            println("test")
            GlobalScope.launch {
                mouseMovements.emit(PointDTO.newBuilder().setX(x).setY(y).build())
            }
        }
        GlobalScope.launch {
            client.addStream(mouseMovements)
        }
    }
}



class PointClient(private val channel: ManagedChannel) : Closeable {
    private val stub: PointServiceGrpcKt.PointServiceCoroutineStub =
        PointServiceGrpcKt.PointServiceCoroutineStub(channel)

    suspend fun addPoint(point: PointDTO) {
        val response = stub.addPoint(point)
    }

    suspend fun addStream(flow :Flow<PointDTO>){
        stub.addPointStream(flow);
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun main(args: Array<String>) {
    Application.launch(ClientJavaFX::class.java)
}