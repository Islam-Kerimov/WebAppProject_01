package com.kerimovikh.filter;

import com.kerimovikh.connection.ConnectionUtils;
import com.kerimovikh.utils.MyUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

@WebFilter(filterName = "jdbcFilter", urlPatterns = "/*")
public class JdbcFilter implements Filter {

    private boolean needJdbc(HttpServletRequest request) {

        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();

        String urlPattern = pathInfo != null
                ? servletPath + "/*"
                : servletPath;

        Map<String, ? extends ServletRegistration> servletRegistration = request.getServletContext().getServletRegistrations();
        for (ServletRegistration registration : servletRegistration.values()) {
            if (registration.getMappings().contains(urlPattern)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (this.needJdbc(request)) {

            Connection connection = null;

            try {

                connection = ConnectionUtils.getConnection();
                connection.setAutoCommit(false);

                MyUtils.storeConnection(servletRequest, connection);

                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception e) {
                e.printStackTrace();
                ConnectionUtils.rollbackQuietly(connection);
                throw new RuntimeException(e);
            } finally {
                ConnectionUtils.closeQuietly(connection);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
