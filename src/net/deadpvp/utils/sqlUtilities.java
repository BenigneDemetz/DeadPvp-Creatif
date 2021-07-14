package net.deadpvp.utils;

import net.deadpvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;

public class sqlUtilities {

    /*
    * TODO: opti classe
    * */

    public static String host, database, username, password;
    public static int port;

    public static void mysqlSetup(){
        host = "localhost";
        port = 3306;
        database = "minecraftrebased";
        username = "root";
        password = "";

        try {

            synchronized (Main.getInstance().getServer()) {
                if (Main.getInstance().getConnection() != null && !Main.getInstance().getConnection().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                Main.getInstance().setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":"
                        + port + "/" + database, username, password));

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
            }
        }
        catch(SQLException e){
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "MYSQL NOT CONNECTED #2");
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "MYSQL NOT CONNECTED #3");
        }
    }

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
