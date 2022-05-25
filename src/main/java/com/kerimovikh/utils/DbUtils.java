package com.kerimovikh.utils;

import com.kerimovikh.beans.Product;
import com.kerimovikh.beans.UserAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbUtils {

    private static final String SELECT_USER_WITH_USERNAME_AND_PASSWORD_SQL = "SELECT user_name, password, gender FROM user_account WHERE user_name = ? AND password = ?";
    private static final String SELECT_USER_WITH_USERNAME_SQL = "SELECT user_name, password, gender FROM user_account WHERE user_name = ?";
    private static final String SELECT_LIST_OF_PRODUCTS_SQL = "SELECT code, name, price FROM product";
    private static final String SELECT_PRODUCT_WITH_CODE_SQL = "SELECT code, name, price FROM product WHERE code = ?";
    private static final String UPDATE_PRODUCT_SQL = "UPDATE product SET name = ?, price = ? WHERE code = ?";
    private static final String INSERT_PRODUCT_SQL = "INSERT INTO product(code, name, price) VALUES (?, ?, ?)";
    private static final String DELETE_PRODUCT_SQL = "DELETE FROM product WHERE code = ?";

    public static UserAccount findUser(Connection connection, String userName, String password) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_WITH_USERNAME_AND_PASSWORD_SQL)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String gender = resultSet.getString("gender");
                UserAccount user = new UserAccount();
                user.setUserName(userName);
                user.setPassword(password);
                user.setGender(gender);
                return user;
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserAccount findUser(Connection connection, String userName) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_WITH_USERNAME_SQL)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String password = resultSet.getString("password");
                String gender = resultSet.getString("gender");
                UserAccount user = new UserAccount();
                user.setUserName(userName);
                user.setPassword(password);
                user.setGender(gender);
                return user;
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Product> queryProduct(Connection connection) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LIST_OF_PRODUCTS_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Product> products = new ArrayList<>();

            while (resultSet.next()) {
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                float price = resultSet.getFloat("price");
                products.add(new Product(code, name, price));
            }

            return products;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static Product findProduct(Connection connection, String code) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_WITH_CODE_SQL)) {

            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("code");
                float price = resultSet.getFloat("price");
                return new Product(code, name, price);
            }

            return null;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static void updateProduct(Connection connection, Product product) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setFloat(2, product.getPrice());
            preparedStatement.setString(3, product.getCode());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static void insertProduct(Connection connection, Product product) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {

            preparedStatement.setString(1, product.getCode());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setFloat(3, product.getPrice());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static void deleteProduct(Connection connection, String code) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT_SQL)) {

            preparedStatement.setString(1, code);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
