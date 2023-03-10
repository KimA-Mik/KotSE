package server

import IndexData
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.io.OutputStream
import java.net.InetSocketAddress

class SimpleServer (val index: IndexData, private val port: Int) {
    fun run() {
        HttpServer.create(InetSocketAddress(port), 0).apply {
            println("Server runs at: 127.0.0.1:${address.port}")
            createContext("/") { http ->
                when (http.requestMethod) {
                    "GET" -> {
                        send_plain_html_file(http, "/html/index.html")
                        http.close()
                    }
                }
            }
            start()
        }
    }

    private fun send_plain_html_file(http: HttpExchange, file: String) {
        http.responseHeaders.add("Content-type", "text/html")
        http.sendResponseHeaders(200, 0)
        val os: OutputStream = http.responseBody
        val fileContent = SimpleServer::class.java.getResource(file).readText()
        os.write(fileContent.encodeToByteArray())
        os.flush()
    }
}