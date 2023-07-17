package com.example.bootIML.interseptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HttpsConfig implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // String requestedPort = request.getServerPort() if you're not behind a proxy
        //String requestedPort = request.getHeader("X-Forwarded-Port"); // I'm behind a proxy on Heroku

        System.out.println("preHandle");
        //System.out.println(requestedPort);
        System.out.println(request + " " + request.getServerName() + " " + request.getRequestURI() + " " + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        if (response.getTrailerFields() != null)
            System.out.println(response + response.getTrailerFields().toString());
//        if (requestedPort != null && requestedPort.equals("80")) { // This will still allow requests on :8080
//            response.sendRedirect("https://" + request.getServerName() + request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
//            return false;
//        }
        return true;
    }

}

