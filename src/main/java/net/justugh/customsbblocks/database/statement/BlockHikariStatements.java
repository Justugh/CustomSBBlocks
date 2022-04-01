package net.justugh.customsbblocks.database.statement;

import net.justugh.japi.database.hikari.data.HikariTableStatements;

public class BlockHikariStatements extends HikariTableStatements {

    @Override
    public String getTableCreateStatement() {
        return "CREATE TABLE IF NOT EXISTS sbblockdata(location VARCHAR(255) NOT NULL, blockKey VARCHAR(255) NOT NULL, PRIMARY KEY(location));";
    }

    @Override
    public String getDataCreateStatement() {
        return "INSERT INTO sbblockdata(location, blockKey) VALUES(?, ?);";
    }

    @Override
    public String getDataStatement() {
        return "SELECT * FROM sbblockdata;";
    }

    @Override
    public String getSaveStatement() {
        return "INSERT INTO sbblockdata(location, blockKey) VALUES(?, ?) ON DUPLICATE KEY UPDATE blockKey=VALUES(blockKey);";
    }

    @Override
    public String getDeleteStatement() {
        return "DELETE FROM sbblockdata WHERE location=?;";
    }

}
