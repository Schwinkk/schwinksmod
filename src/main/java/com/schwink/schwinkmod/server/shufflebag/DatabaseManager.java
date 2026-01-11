package com.schwink.schwinkmod.server.shufflebag;

import com.schwink.schwinkmod.common.DataTypes;
import com.schwink.schwinkmod.common.DataTypes.ShuffleBagEntry;
import com.schwink.schwinkmod.common.DataTypes.PlayerBagData;
import net.minecraft.server.level.ServerPlayer;
import org.jline.utils.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseManager {

    public static final DatabaseManager INSTANCE = new DatabaseManager();

    public Map<UUID, PlayerBagData> currentPlayerBags = new ConcurrentHashMap<>();

    public void loadPlayerInCache(UUID uuid) {

        // TODO loading bagData from db
        PlayerBagData playerBagData;

        //currentPlayerBags.put(uuid, playerBagData);
    }

    public void clearPlayerFromCache(UUID uuid) {

        if (!currentPlayerBags.containsKey(uuid)) {
            Log.error("Player " + uuid + "is not found !");
            return;
        }

        if (currentPlayerBags.get(uuid).isDirty()) {
            //TODO save data to db
        }

        currentPlayerBags.remove(uuid);
    }

    public void saveAllDataToDb(){
        currentPlayerBags.forEach((uuid, playerBagData) -> {
            if (currentPlayerBags.get(uuid).isDirty()) {
                // TODO save data to db
            }
        });
    }

    public void changeBagData(UUID uuid, PlayerBagData playerBagData) {
        playerBagData.setDirty(true);
        currentPlayerBags.replace(uuid, playerBagData);
    }

    public void initDB(UUID uuid, PlayerBagData playerBagData) throws SQLException {
        Connection connect = DriverManager.getConnection("jbdc:sqlite:modDB.db");
        currentPlayerBags.put(uuid, playerBagData);
    }
}
