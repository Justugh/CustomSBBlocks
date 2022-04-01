package net.justugh.customsbblocks.database;

import net.justugh.customsbblocks.database.statement.BlockHikariStatements;
import net.justugh.japi.database.hikari.HikariAPI;
import net.justugh.japi.database.hikari.data.HikariTable;
import net.justugh.japi.util.location.SafeLocation;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlockDatabase extends HikariTable<BlockData> {

    public BlockDatabase(HikariAPI hikariAPI) {
        super(hikariAPI, new BlockHikariStatements());
    }

    @Override
    public BlockData loadObject(ResultSet resultSet) throws SQLException {
        SafeLocation location = SafeLocation.fromString(resultSet.getString("location"));
        String key = resultSet.getString("blockKey");
        return new BlockData(location.toString(), key);
    }

    public BlockData getBlockData(Location location) {
        return getData().stream().filter(data -> data.getSafeLocation().matches(location)).findFirst().orElse(null);
    }

}
