package com.java.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.exception.APIException;
import com.java.response.APIResponse;
import com.java.util.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.java.constant.Constants.UNAUTHORIZED;

@Component
public class EntryPointAuthenticationConfig implements AuthenticationEntryPoint {

    private final Translator translator;

    public EntryPointAuthenticationConfig(Translator translator) {
        this.translator = translator;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        String message = this.translator.getMessage(UNAUTHORIZED);
        APIResponse<Object> res = APIResponse.failed(HttpStatus.UNAUTHORIZED.value(), message);

        OutputStream outputStream = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, res);
        outputStream.flush();
    }
}
