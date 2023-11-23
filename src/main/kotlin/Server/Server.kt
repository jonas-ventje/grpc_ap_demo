package Server

import io.grpc.ServerBuilder
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration


fun helloServer() {
    //val helloService = HelloService()
    val pointService = PointService()
    val server = ServerBuilder
        .forPort(15001)
        .addService(pointService)
        .build()

    Runtime.getRuntime().addShutdownHook(Thread {
        server.shutdown()
        server.awaitTermination()
    })

    server.start()
    server.awaitTermination()
}

class MyJavaFXApplication : IObserver, Application(){
    lateinit var gc: GraphicsContext;
    override fun start(p0: Stage?) {
        Points.getInstance().add(this);
        p0?.setTitle("Drawing Operations Test")
        val root = Group()
        val canvas = Canvas(600.0, 600.0)
        gc = canvas.getGraphicsContext2D()
        drawShapes(gc)
        root.getChildren().add(canvas)
        p0?.setScene(Scene(root))
        p0?.show()
    }

    private fun drawShapes(gc: GraphicsContext) {
        gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height);
        gc.fill = Color.GREEN
        gc.stroke = Color.BLUE
        gc.lineWidth = 5.0
        Points.getInstance().pointList.forEach{point ->
            gc.fillOval(point.X.toDouble(), point.Y.toDouble(), 30.0, 30.0)
        }
    }

    override fun update() {
        gc?.let{drawShapes(it)}
    }
}


fun main() {
    val serverThread = Thread {
        helloServer()
    }

    val javafxThread = Thread {
        Application.launch(MyJavaFXApplication::class.java)
    }

    serverThread.start()
    javafxThread.start()
}