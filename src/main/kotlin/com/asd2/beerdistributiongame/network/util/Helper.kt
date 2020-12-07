package com.asd2.beerdistributiongame.network.util

import com.asd2.beerdistributiongame.network.NetworkResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

const val CONTENT_TYPE = "Content-Type"
const val MEDIA_TYPE = "application/json"
const val LOCALHOST = "127.0.0.1"
const val APP_PORT = 8080

const val HOST_CANNOT_BE_REACHED = "Host cannot be reached"
const val CLIENT_CANNOT_BE_REACHED = "Client cannot be reached"

const val CONNECTION_TIMEOUT : Long = 1000
const val SOCKET_TIMEOUT : Long = 10000

const val OTHER_CONNECTION_TIMEOUT : Long = 500
const val OTHER_SOCKET_TIMEOUT : Long = 5000

/**
 * Returns the host URL for the given endpoint path
 *
 * @param endpointPath: The end point path of the to connect computer
 * @return valid ip, port and endpoint
 */
internal fun hostUrl(endpointPath: String) = "http://${NetworkContext.hostIp}:${NetworkContext.HOST_PORT}/host/$endpointPath"

/**
 * Returns the client URL for the given endpoint path
 *
 * @param ipAddress The IP of the client
 * @param endpointPath The path of the endpoint
 * @return The client URL
 */
internal fun clientUrl(ipAddress: String, endpointPath: String) = "http://$ipAddress:$APP_PORT/client/$endpointPath"

/**
 * Converts any object to a JSON string
 *
 * @param theObject Any object
 * @return JSON string
 */
internal fun toJson(theObject: Any): String = jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(theObject)


/**
 * Creates a NetworkResponse
 *
 * @param response HttpResponse<JsonNode>
 * @return NetworkResponse
 */
internal fun getNetworkResponse(response: HttpResponse<JsonNode>): NetworkResponse {
    return NetworkResponse(response.status == HttpStatus.OK.value(), "${response.status}: ${response.statusText}")
}

/**
 * Creates a NetworkResponse that returns a false and host cannot be reached message.
 *
 * @return NetworkResponse
 */
internal fun getHostCannotBeReachedNetworkResponse() = NetworkResponse(false, HOST_CANNOT_BE_REACHED)

/**
 * Creates a NetworkResponse that returns a false and client cannot be reached message.
 *
 * @return NetworkResponse
 */
internal fun getClientCannotBeReachedNetworkResponse() = NetworkResponse(false, CLIENT_CANNOT_BE_REACHED)

/**
 * Writes the Host Endpoint call to the log
 *
 * @param endpoint The endpoint
 * @param requestBody The requestBody, default is null
 */
internal fun logEndpointCall(clazz: String ,endpoint: String, requestBody: Any? = null) {
    val logger = LoggerFactory.getLogger(clazz)
    if (requestBody != null)
        logger.info("Endpoint: $endpoint called with request body $requestBody")
    else
        logger.info("Endpoint: $endpoint called")
}

/**
 * @return current milliseconds
 */
fun getCurrentMilliseconds(): Long = System.currentTimeMillis()