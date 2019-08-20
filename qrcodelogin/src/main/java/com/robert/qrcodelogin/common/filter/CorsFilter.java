package com.robert.qrcodelogin.common.filter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
@Component
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse rsp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        String originHeader = req.getHeader("Origin");
        String excludedPages = "*";
        String[] origins = {};
        if (excludedPages.length() > 0) {
            origins = excludedPages.split(",");
        }
        if (Arrays.asList(origins).contains(originHeader) || Arrays.asList(origins).contains("*")){
            rsp.setHeader("Access-Control-Allow-Origin", originHeader);
            rsp.setHeader("Access-Control-Allow-Methods","GET, POST, HEAD, PUT, DELETE,OPTIONS");
            rsp.setHeader("Access-Control-Max-Age","3600");
            rsp.setHeader("Access-Control-Allow-Headers","Origin, Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, Accept, token");
            rsp.setHeader("Access-Control-Allow-Credentials", "true");
        }
        if("OPTIONS".equals(((HttpServletRequest) req).getMethod())){
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
