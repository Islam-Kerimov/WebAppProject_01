package com.kerimovikh.filter;

import com.kerimovikh.beans.UserAccount;
import com.kerimovikh.utils.DbUtils;
import com.kerimovikh.utils.MyUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;

@WebFilter(filterName = "cookieFilter", urlPatterns = "/*")
public class CookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        UserAccount userInSession = MyUtils.getLoginedUser(session);
        if (userInSession != null) {
            session.setAttribute("COOKIE_CHECKED", "CHECKED");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Connection connection = MyUtils.getStoredConnection(servletRequest);

        String checked = (String) session.getAttribute("COOKIE_CHECKED");
        if (checked == null && connection != null) {
            String userName = MyUtils.getUserNameInCookie(request);
            UserAccount user = DbUtils.findUser(connection, userName);
            MyUtils.storeLoginedUser(session, user);
            session.setAttribute("COOKIE_CHECKED", "CHECKED");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
