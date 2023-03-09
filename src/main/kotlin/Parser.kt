import com.spire.pdf.PdfDocument
import com.spire.pdf.PdfPageBase
import kotlinx.serialization.Serializable
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class Parser() {

    fun ParseDir(dirPath: String) : HashMap<String, HashMap<String, Int>> {
        val map = HashMap<String, HashMap<String, Int>>()
        File(dirPath).walk(FileWalkDirection.TOP_DOWN).forEach {
            val ext = it.extension.lowercase(Locale.getDefault())
            val curMap = HashMap<String, Int>()
            if ( (ext == "pdf")){
                println(it)
//                try {

                    val content = parsePdf(it)
                    val lexer = Lexer(content)

                    for (token in lexer){
                        if (curMap.containsKey(token)){
                            curMap[token] = curMap[token]!! + 1
                        }else {
                            curMap[token] = 1
                        }
                    }
//                }
//                catch (e: Exception){
//                    println("Some pdf parsing error {e.localizedMessage}")
//                }
                map[it.path] = curMap
            }
        }
        return map
    }

    private fun parsePdf(file: File) : String {
        val doc = PdfDocument()
        doc.loadFromFile(file.path)

        val sb = StringBuilder()
        val page: PdfPageBase

        for(i in 0 until doc.pages.count){
            sb.append(doc.pages.get(i).extractText(false))
        }
        println(doc.pages.count)
        val res = sb.toString()

        doc.close()
        return res.slice(74 until res.length)
    }
}