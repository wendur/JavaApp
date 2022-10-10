package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Currencies;
import model.Items;
import model.Player;
import model.Progresses;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLDatabaseConnection {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/players_db";
    static final String USER = "postgres";
    static final String PASS = "1488";

    public void connect () throws IOException, SQLException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonData = Files.readAllBytes(Paths.get("players.json"));

        Player[] players = mapper.readValue(jsonData, Player[].class);

        clearDataBase();

        setToDataBase(players);

        List<Player> playersFromDB = getPlayersFromDataBase();

        writeToFile(playersFromDB);

        clearDataBase();
    }

    public void setToDataBase(Player[] players) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            Statement statement = connection.createStatement();

            for (Player pl : players) {
                statement.executeUpdate(String.format("INSERT INTO players (id, nickname) VALUES (%d, '%s');", pl.getPlayerId(), pl.getNickname()));
                for (Progresses prog : pl.getProgresses()) {
                    statement.executeUpdate(String.format("INSERT INTO progresses (id, fk_progresses_players, resource_id, score, max_score) VALUES (%d, %d, %d, %d, %d);", prog.getId(), prog.getPlayerId(), prog.getResourceId(), prog.getScore(), prog.getMaxScore()));
                }
                for (Currencies cur : pl.getCurrencies()) {
                    statement.executeUpdate(String.format("INSERT INTO currencies (id, fk_currencies_players, resource_id, name, count) VALUES (%d, %d, %d, '%s', %d);", cur.getId(), cur.getPlayerId(), cur.getResourceId(), cur.getName(), cur.getCount()));
                }
                for (Items item : pl.getItems()) {
                    statement.executeUpdate(String.format("INSERT INTO items (id, fk_items_players, resource_id, count, level) VALUES (%d, %d, %d, %d, %d);", item.getId(), item.getPlayerId(), item.getResourceId(), item.getCount(), item.getLevel()));
                }
            }
        }
    }

    public static List<Player> getPlayersFromDataBase() throws SQLException {
        List<Player> players = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players");
            while(resultSet.next()) {
                Player pl = new Player();
                int id = resultSet.getInt("id");
                pl.setPlayerId(id);
                pl.setNickname(resultSet.getString("nickname"));

                ResultSet progResult = statement.executeQuery(String.format("SELECT * FROM progresses WHERE fk_progresses_players=%d", id));
                while(progResult.next()) {
                    pl.addProgress(new Progresses(progResult.getInt("id"),
                            progResult.getInt("fk_progresses_players"),
                            progResult.getInt("resource_id"),
                            progResult.getInt("score"),
                            progResult.getInt("max_score")));
                }

                ResultSet curResult = statement.executeQuery(String.format("SELECT * FROM currencies WHERE fk_currencies_players=%d", id));
                while(curResult.next()) {
                    pl.addCurrency(new Currencies(curResult.getInt("id"),
                            curResult.getInt("fk_currencies_players"),
                            curResult.getInt("resource_id"),
                            curResult.getString("name"),
                            curResult.getInt("count")));
                }

                ResultSet itemResult = statement.executeQuery(String.format("SELECT * FROM items WHERE fk_items_players=%d", id));
                while(itemResult.next()) {
                    pl.addItem(new Items(itemResult.getInt("id"),
                            itemResult.getInt("fk_items_players"),
                            itemResult.getInt("resource_id"),
                            itemResult.getInt("count"),
                            itemResult.getInt("level")));
                }

                players.add(pl);
                //resultSet = statement.executeQuery("SELECT * FROM players");
            }
        }
        //System.out.println(players.size());
        return players;
    }

    public void clearDataBase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            Statement statement = connection.createStatement();

            statement.executeUpdate("TRUNCATE players, progresses, currencies, items;");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(List<Player> players) {
        try(FileWriter writer = new FileWriter("players.txt", false))
        {
            writer.write("Player 222:" + "\n" + players.get(222).toString() + "\n");
            writer.write("Player 555:" + "\n" + players.get(555).toString() + "\n");
            writer.write("Player 888:" + "\n" + players.get(888).toString() + "\n");

            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
