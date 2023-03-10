import Index.Parser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.Exception

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        usage()
        return
    }

    if (args[0] == "index"){
        if (args.size != 3){
            println("Please provide dirt to index and filename to save cache")
        }
        val dir = args[1]
        val cacheFile = args[2]
        index(dir, cacheFile)
    }else if(args[0] == "serve"){
        if (args.size == 2)
            serve(args[1])
        else
            println("Provide index file")
    }else{
        usage()
    }
}

private fun usage(){
    println("Usage:\nindex <directory to index> <file to save>")
}

private fun index(dir: String, cacheFile: String) {
    try {
        val parser = Parser()
        val data = parser.ParseDir(dir)
        val jsonStr = Json.encodeToString(data)
        File(cacheFile).bufferedWriter().use { out ->
            out.write(jsonStr)
        }
    } catch (e: Exception) {
        println(e.localizedMessage)
    }
}

private fun serve(file: String){
    lateinit var indexData: IndexData
    File(file).bufferedReader().use {it ->
        indexData = Json.decodeFromString(it.readText())
    }
    println(indexData)
}