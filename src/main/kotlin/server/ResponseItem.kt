package server

import kotlinx.serialization.Serializable

@Serializable
data class ResponseItem (val file: String, val relevancy: Double){

}