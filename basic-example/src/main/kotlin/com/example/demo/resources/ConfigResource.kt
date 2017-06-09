package com.example.demo.resources


import com.example.demo.configuration.ConfigFacebook
import com.example.demo.configuration.ConfigGoogle
import com.example.demo.configuration.ConfigPaypal
import com.example.demo.logging.logger

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/config")
@Api("/config")
class ConfigResource @Inject constructor(
        private val configPaypal: ConfigPaypal,
        private val configGoogle: ConfigGoogle,
    private val configFacebook: ConfigFacebook
) {
    companion object {
        private val LOGGER by logger()
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/describe")
    @ApiOperation(
            value = "",
            notes = "",
            response = Map::class
    )
    fun describe(): Response {
        LOGGER.info("/describe $this")

        val responseData = mapOf<String, Any?>(
                "configPaypal" to configPaypal,
                "configGoogle" to configGoogle,
            "configFacebook" to configFacebook
        )

        return Response.ok(responseData).build()
    }
}