package me.krsmll.libs.common.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WebClientExceptionHandler {
    public static void handleWebClientError(Throwable e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
