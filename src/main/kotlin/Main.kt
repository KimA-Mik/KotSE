import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.Exception

fun main(args: Array<String>) {
    println("Hello World!")

    if (args.size < 1) {
        println("Provide directory path")
    }
    val parser = Parser()
    try {
        val data = parser.ParseDir(args[0])
        val jsonStr = Json.encodeToString(data)
        File("data.json").bufferedWriter().use { out ->
            out.write(jsonStr)
        }
    }
    catch (e :Exception){
        println(e.localizedMessage)
    }
}