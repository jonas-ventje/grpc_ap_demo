import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun simple(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..10) {
        delay(if(i%3==0) 400 else 1000)
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    println("Calling simple function...")
    val flow = simple()
    println("Calling collect...")
    var clickCount = 0
    flow
        .onEach { clickCount++; }
        .debounce(500)
        //.debounce(500)
        .collect {
            value ->
            //value -> println(value)
            println(clickCount)
            clickCount = 0
        }
}

