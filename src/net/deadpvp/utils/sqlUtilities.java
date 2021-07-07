package net.deadpvp.utils;

import net.deadpvp.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class sqlUtilities {

    public static boolean hasData(String table, String nameColumn, String playerSearched)  {
        try {
            Connection connection = Main.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM " + table + " WHERE " + nameColumn + "='" + playerSearched + "';");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                return true;
            }
            preparedStatement.close();
            resultSet.close();
            return false;
        }
        catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }

    }

    public static Object getData(String table, String nameColumn, String playerSearched, String columnSearched,
                            String dataTypeSearchedStringOrInt) throws SQLException {
        Connection connection = Main.getInstance().getConnection();
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM " + table + " WHERE " + nameColumn + "='" + playerSearched + "';");
        ResultSet resultSet =  preparedStatement.executeQuery();
        if (resultSet.next()) {
            if (dataTypeSearchedStringOrInt.equalsIgnoreCase("String")) {
                String data = resultSet.getString(columnSearched);
                preparedStatement.close();
                resultSet.close();
                return data;
            } else if (dataTypeSearchedStringOrInt.equalsIgnoreCase("Int")) {
                int data = resultSet.getInt(columnSearched);
                resultSet.close();
                preparedStatement.close();
                return data;
            }
        }
        return null;
    }

    public static void insertData(String table, String playerName, int mystics, int karma,
                              String columnsSeparatedByCommas) throws SQLException {
        Connection connection = Main.getInstance().getConnection();
        PreparedStatement insert =
                connection.prepareStatement(
                        "INSERT INTO "+ table + " (" + columnsSeparatedByCommas + ") VALUES ('" +playerName  + "', "+ mystics + ", " + karma + ")");
        insert.execute();
        insert.close();
    }

    public static void updateData (String table, String nameColumn, Object data, String player) throws SQLException {
        Connection connection = Main.getInstance().getConnection();
        PreparedStatement modifyStats =
                connection.prepareStatement("UPDATE "+ table + " SET " + nameColumn +"=" + data + " WHERE player='" + player + "';");
        modifyStats.execute();
        modifyStats.close();
    }

}
