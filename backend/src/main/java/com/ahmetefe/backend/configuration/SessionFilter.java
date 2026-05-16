package com.ahmetefe.backend.configuration;

import com.ahmetefe.backend.utils.AppConstants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class SessionFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String urlPath = request.getRequestURI();
        String[] freeUrls = {"/user","/kvkk"};

        boolean isAuth = true;

        for(String url : freeUrls)
        {
            if(urlPath.startsWith(url))
            {
                isAuth = false;
                break;
            }
        }

        // 🔹 CLIENT BİLGİLERİ
        String ipAddress = getClientIP(request);
        String userAgent = request.getHeader("User-Agent");
        String method = request.getMethod();
        String query = request.getQueryString();
        String referer = request.getHeader("Referer");

        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        HttpSession session = request.getSession(false);
        Object userId = (session != null) ? session.getAttribute(AppConstants.USER_SESSION_INFO) : null;

        // ✅ INFO LOG
        logger.info("""
                ====== REQUEST LOG ======
                Time      : {}
                IP        : {}
                Method    : {}
                URL       : {}
                Query     : {}
                Referer   : {}
                UserAgent : {}
                Session   : {}
                User      : {}
                ==========================
                """,
                time,
                ipAddress,
                method,
                urlPath,
                query,
                referer,
                userAgent,
                (session != null ? session.getId() : "No Session"),
                (userId != null ? userId : "Anonymous")
        );

        if(isAuth)
        {
            if(userId == null)
            {
                logger.warn("Unauthorized access -> IP: {}, URL: {}", ipAddress, urlPath);

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                String jsonResponse = """
                        {
                          "success": false,
                          "message": "Unauthorized access. Please log in."
                        }
                        """;

                response.getWriter().write(jsonResponse);
                return;
            }
        }
        filterChain.doFilter(request,response);

    }

    private String getClientIP(HttpServletRequest request)
    {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if(xfHeader == null || xfHeader.isEmpty())
        {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
