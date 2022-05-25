package com.kerimovikh.servlet;

import com.kerimovikh.beans.UserAccount;
import com.kerimovikh.utils.DbUtils;
import com.kerimovikh.utils.MyUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        String rememberMeStr = req.getParameter("rememberMe");
        boolean remember = "Y".equals(rememberMeStr);

        UserAccount user = null;
        boolean hasError = false;
        String errorString = null;

        if (userName == null || password == null || userName.length() == 0 || password.length() == 0) {
            hasError = true;
            errorString = "Required username and password";
        } else {
            try (Connection connection = MyUtils.getStoredConnection(req)) {

                user = DbUtils.findUser(connection, userName, password);

                if (user == null) {
                    hasError = true;
                    errorString = "User Name or password invalid";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                hasError = true;
                errorString = e.getMessage();
            }
        }

        if (hasError) {
            user = new UserAccount();
            user.setUserName(userName);
            user.setPassword(password);

            req.setAttribute("errorString", errorString);
            req.setAttribute("user", user);

            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");

            dispatcher.forward(req, resp);
        } else {

            HttpSession session = req.getSession();
            MyUtils.storeLoginedUser(session, user);

            if (remember) {
                MyUtils.storeUserCookie(resp, user);
            } else {
                MyUtils.deleteUserCookie(resp);
            }

            resp.sendRedirect(req.getContextPath() + "/userInfo");
        }
    }
}
