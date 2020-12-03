package com.sourcedcode.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/simple-post",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST
        })
public class SimplePostServlet extends SlingAllMethodsServlet {
    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
        resp.setStatus(200);
        resp.getWriter().write("Simple Post Test");
    }
}
