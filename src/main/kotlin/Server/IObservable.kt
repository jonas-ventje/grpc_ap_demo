package Server

interface IObservable {
    val observers: ArrayList<IObserver>

    fun add(observer: IObserver);

    fun remove(observer: IObserver);

    fun sendUpdateEvent();
}