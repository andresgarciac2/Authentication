package co.com.uniandes.arquitectura.controller;

import java.io.IOException;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.json.DecodeException;
import io.vertx.rxjava.ext.web.RoutingContext;

/**
 * @author andresgarcias4n
 * Implementa acciones comunes en los controladores
 *
 */
public interface Controller {

	
    public static final ObjectMapper mapper = new ObjectMapper();
	
	 /**
     * Extrae el cuerpo de una petición como un JSON
     *
     * @param ctx   Contexto del request
     * @param clazz Clase del objeto destino
     * @param <T>   Tipo de la clase del objeto destino
     * @return Una instancia creada del objeto destino
     */
    default <T> T extractBodyAsJson(RoutingContext ctx, Class<T> clazz) {
        try {
            String bodyAsString = ctx.getBodyAsString();
            return mapper.readValue(bodyAsString, clazz);
        } catch (DecodeException e) {
            throw new DecodeException(e.getMessage());
        } catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * Responde una petición http sin contenido (statusCode 204)
     *
     * @param ctx Contexto del request
     */
    default void respondNoContent(RoutingContext ctx) {
        ctx.response()
                .setStatusCode(403)
                .end();
    }

    /**
     * Responde una petición http con un JSON (statusCode 200)
     *
     * @param ctx  Contexto del request
     * @param body Objeto a convertir en JSON
     */
    default void respondWithJson(RoutingContext ctx, Object body) {
    	respondWithJson(ctx, 200, body);
    }

    /**
     * Responde una petición http con un JSON
     *
     * @param ctx        Contexto del request
     * @param statusCode Status code de la resuesta
     * @param body       Objeto a convertir en JSON
     */
    default void respondWithJson(RoutingContext ctx, int statusCode, Object body) {
        String encoding = "UTF-8";
        try {
			ctx.response()
			        .setStatusCode(statusCode)
			        .putHeader("Content-Type", "application/json;charset=" + encoding)
			        .end(mapper.writeValueAsString(body), encoding);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
	
}
