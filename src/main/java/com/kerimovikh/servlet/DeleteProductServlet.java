package com.kerimovikh.servlet;

import com.kerimovikh.utils.DbUtils;
import com.kerimovikh.utils.MyUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/deleteProduct")
public class DeleteProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = MyUtils.getStoredConnection(req);
        String code = req.getParameter("code");
        String errorString = null;

        try {
            DbUtils.deleteProduct(connection, code);
        } catch (SQLException e) {
            e.printStackTrace();
            errorString = e.getMessage();
        }

        if (errorString != null) {
            req.setAttribute("errorString", errorString);

            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/views/deleteProductErrorView.jsp");
            dispatcher.forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/productList");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
