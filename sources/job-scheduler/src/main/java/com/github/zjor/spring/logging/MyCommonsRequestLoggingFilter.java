package com.github.zjor.spring.logging;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class MyCommonsRequestLoggingFilter extends CommonsRequestLoggingFilter {

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
    }
}
