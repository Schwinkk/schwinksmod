package com.schwink.schwinkmod.server.shufflebag;

import com.schwink.schwinkmod.common.Config;
import com.schwink.schwinkmod.common.DataTypes;
import com.schwink.schwinkmod.common.DataTypes.ShuffleBagEntry;
import com.schwink.schwinkmod.common.DataTypes.PlayerBagData;
import net.minecraft.server.level.ServerPlayer;
import org.jline.utils.Log;

import java.io.File;
import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseManager {

    public static final DatabaseManager INSTANCE = new DatabaseManager();

    public Map<UUID, PlayerBagData> currentPlayerBags = new ConcurrentHashMap<>();

    private Connection connection;

    public void loadPlayerInCache(UUID uuid) {

        if (connection == null) {
            Log.error("Database connection is null during loadPlayerInCache");
            return;
        }

        PlayerBagData playerData = currentPlayerBags.computeIfAbsent(uuid, k -> new PlayerBagData());

        String sql = "SELECT bag_id, seed, opened_count FROM player_shufflebags WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, uuid.toString());

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    DataTypes.BagInfo bag = new DataTypes.BagInfo();
                    bag.setSeed(rs.getLong("seed"));
                    bag.setCount(rs.getInt("opened_count"));
                    playerData.addToBags(rs.getString("bag_id"),bag);
                }

                playerData.setDirty(false);

                currentPlayerBags.put(uuid, playerData);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearPlayerFromCache(UUID uuid) throws SQLException {

        if (!currentPlayerBags.containsKey(uuid)) {
            Log.error("Player " + uuid.toString() + "is not found !");
            return;
        }

        if (currentPlayerBags.get(uuid).isDirty()) {
            try {
                connection.setAutoCommit(false);

                String sql = """
                INSERT INTO player_shufflebags(uuid, bag_id, seed, opened_count)
                VALUES(?, ?, ?, ?)
                ON CONFLICT(uuid, bag_id)
                DO UPDATE SET
                    seed = excluded.seed,
                    opened_count = excluded.opened_count;
                """;

                try (PreparedStatement pstmt = connection.prepareStatement(sql)){
                    pstmt.setString(1,uuid.toString());
                    for (var entry : currentPlayerBags.get(uuid).getBags().entrySet()){
                        pstmt.setString(2, entry.getKey());
                        pstmt.setLong(3, entry.getValue().getSeed());
                        pstmt.setInt(4, entry.getValue().getCount());

                        pstmt.addBatch();
                    }

                    pstmt.executeBatch();
            }
                connection.commit();

            } catch (SQLException e) {
                Log.error("Failed to save data for player " + uuid, e);

                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Log.error("Failed to rollback transaction", ex);
                }
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    Log.error("Failed to reset auto-commit", e);
                }
            }
        }

        currentPlayerBags.remove(uuid);
    }

    public void saveAllDataToDb(){
        currentPlayerBags.forEach((uuid, playerBagData) -> {
            try {
                clearPlayerFromCache(uuid);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeBagData(UUID uuid, PlayerBagData playerBagData) {
        playerBagData.setDirty(true);
        currentPlayerBags.replace(uuid, playerBagData);
    }

    public void initDB(File worldDir) throws SQLException {

        if (!worldDir.exists()){
            boolean created = worldDir.mkdirs();
            if (!created){
                Log.warn("CANT MAKE FILE");
            }
        } else
        {
            Log.warn("EXITTT");
        }

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + worldDir.getAbsolutePath() + "/modDB.db";
            connection = DriverManager.getConnection(url);

            Log.info("Connected to database successfully!");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("NO CONNECTION");
        }

        String sqlCode = """
            CREATE TABLE IF NOT EXISTS player_shufflebags (
                uuid         TEXT NOT NULL,
                bag_id       TEXT NOT NULL,
                seed         INTEGER      NOT NULL,
                opened_count INTEGER     DEFAULT 0,
            
                PRIMARY KEY (uuid, bag_id)
            );
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCode);
        }
    }
}
