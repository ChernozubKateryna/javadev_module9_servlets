package com.goit.filters;

import com.goit.servlets.TimeZoneQueryParameter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    private final TimeZoneQueryParameter queryParameter = new TimeZoneQueryParameter();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            ZoneId.of(queryParameter.parseTimeZone(req));

            chain.doFilter(req, res);
        } catch (DateTimeException | IllegalArgumentException ex) {
            res.setStatus(400);

            res.setContentType("application/json");
            res.getWriter().write("Invalid timezone!");
            res.getWriter().close();
        }
    }
}
