package com.github.zjor.spring.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoggingService {

    void logRequest(HttpServletRequest req, Object body);

    void logResponse(HttpServletRequest req, HttpServletResponse res, Object body);
}
