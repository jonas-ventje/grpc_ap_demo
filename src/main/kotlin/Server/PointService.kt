package Server

import EmptyDTO
import PointDTO
import Server.Models.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach


class PointService: PointServiceGrpcKt.PointServiceCoroutineImplBase() {
    override suspend fun addPoint(request: PointDTO): EmptyDTO {
        println("test")
        Points.getInstance().addPoint(Point(X=request.x, Y=request.y))
        return EmptyDTO.getDefaultInstance()
    }

    override suspend fun addPointStream(requests: Flow<PointDTO>): EmptyDTO {
        var clickCounter = 0;
        requests
            .onEach { clickCounter++ }
            .debounce (300)
            .collect{
                point ->
                    println(clickCounter)
                    Points.getInstance().addPoint(Point(X=point.x, Y=point.y, amount = clickCounter))
                    clickCounter = 0
            }
        return EmptyDTO.getDefaultInstance();
    }
}