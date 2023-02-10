package com.goit.servlets;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private final TimeZoneQueryParameter queryParameter = new TimeZoneQueryParameter();
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        JavaxServletWebApplication app = JavaxServletWebApplication.buildApplication(getServletContext());
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(app);
        engine = new TemplateEngine();

        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ZoneId zoneId = ZoneId.of(queryParameter.parseTimeZone(req));
        Clock clock = Clock.system(zoneId);

        String currentTime = LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + zoneId;
        resp.setContentType("text/html; charset=utf-8");

        resp.addCookie(new Cookie("lastTimezone", zoneId.toString()));

        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("time", currentTime)
        );

        engine.process("time", simpleContext, resp.getWriter());

        resp.getWriter().close();
    }
}
