package com.kerimovikh.servlet;

import com.kerimovikh.beans.Product;
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

@WebServlet(urlPatterns = "/createProduct")
public class CreateProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = MyUtils.getStoredConnection(req);

        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");
        float price = Float.parseFloat(priceStr);
        Product product = new Product(code, name, price);

        String errorString = null;

        String regex = "\\w+";

        if (code == null || !code.matches(regex)) {
            errorString = "Product Code invalid!";
        }

        if (errorString == null) {
            try {
                DbUtils.insertProduct(connection, product);
            } catch (SQLException e) {
                e.printStackTrace();
                errorString = e.getMessage();
            }
        }

        req.setAttribute("errorString", errorString);
        req.setAttribute("product", product);

        if (errorString != null) {
            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
            dispatcher.forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/productList");
        }
    }
}
