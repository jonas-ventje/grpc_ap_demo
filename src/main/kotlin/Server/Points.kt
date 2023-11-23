package Server

import Server.Models.Point

class Points : IObservable {
    val pointList: ArrayList<Point> = arrayListOf<Point>(Point(X=10.0, Y=10.0))

    fun addPoint(point: Point){
        synchronized(this){
            pointList.add(point)
            sendUpdateEvent()
        }
    }



    override val observers: ArrayList<IObserver> =  ArrayList()

    override fun add(observer: IObserver) {
        observers.add(observer)
    }

    override fun remove(observer: IObserver) {
        observers.remove(observer)
    }

    override fun sendUpdateEvent() {
        observers.forEach { it.update() }
    }

    companion object{
        @Volatile
        private var instance: Points? = null

        fun getInstance(): Points {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Points()
                    }
                }
            }
            return instance!!
        }
    }
}