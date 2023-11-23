package Server

import EmptyDTO
import PointDTO
import Server.Models.Point


class PointService: PointServiceGrpcKt.PointServiceCoroutineImplBase() {
    override suspend fun addPoint(request: PointDTO): EmptyDTO {
        println("test")
        Points.getInstance().addPoint(Point(X=request.x, Y=request.y))
        return EmptyDTO.getDefaultInstance()
    }
}