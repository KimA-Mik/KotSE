package server

import index.IndexData
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import index.Lexer
import index.Parser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.OutputStream
import java.net.InetSocketAddress
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

class SimpleServer (val index: IndexData, private val port: Int) {
    fun run() {
        HttpServer.create(InetSocketAddress(port), 0).apply {
            println("Server runs at: 127.0.0.1:${address.port}")
            createContext("/") { http ->
                when (http.requestMethod) {
                    "GET" -> {
                        sendPlainHtmlFile(http, "/html/index.html")
                        http.close()
                    }
                    "POST" -> {
                        val query = getQuery(http)
                        val res = executeQuery(http, query)
                        println(res)
                    }
                }
            }
            start()
        }
    }

    private fun executeQuery(http: HttpExchange, query: String): List<ResponseItem> {
//        for (token in Lexer(query))
//            println(token)
        val resultMap = HashMap<String, Double>()
        val queryList = Lexer(query).asSequence().toList()
        println(queryList)
        for (document in index.tfIndex.keys){
            var relevancy = 0.0
            for (token in queryList){
                val tf = computeTf(document, token)
                val idf = computeIdf(document, token)
                relevancy += tf * idf
            }
            resultMap[document] = relevancy
        }

        val sorted = resultMap.toList()
            .sortedByDescending { (key, value) -> value }.take(10)
        println(sorted)
        return listOf()
    }

    private fun computeTf(document: String, token: String): Double {
        val termCount = if (index.tfIndex[document]!!.containsKey(token))
            index.tfIndex[document]!![token]!!.toDouble()
        else
            0.0
        
        val totalTerms = index.tfIndex[document]!![Parser.TOTAL_TERMS_KEY]!!.toFloat()
        return termCount / totalTerms
    }

    private fun computeIdf(document: String, token: String): Double {
        val totalDocs = index.tfIndex.size.toDouble()
        val docsCount = if (index.idfIndex.containsKey(token))
            index.idfIndex[token]!!.toDouble() + 1.0
        else
            1.0

        return log(totalDocs / docsCount, 10.0)
    }

    private fun getQuery(http: HttpExchange): String {
        http.requestBody.reader().use {
            val body = it.readText()
            val map : HashMap<String, String> = Json.decodeFromString(body)
            return if (map.containsKey("query"))
                map["query"]!!
            else
                String()
        }
    }

    private fun sendPlainHtmlFile(http: HttpExchange, file: String) {
        http.responseHeaders.add("Content-type", "text/html")
        http.sendResponseHeaders(200, 0)
        val os: OutputStream = http.responseBody
        val fileContent = SimpleServer::class.java.getResource(file).readText()
        os.write(fileContent.encodeToByteArray())
        os.flush()
    }

    private fun sendJson(http: HttpExchange, json: String) {
        http.responseHeaders.add("Content-type", "text/json")
        http.sendResponseHeaders(200, 0)
        val os: OutputStream = http.responseBody
        os.write(json.encodeToByteArray())
        os.flush()
    }
}