package org.edgegamers.picklez.Storage;

import org.mineacademy.fo.settings.YamlConfig;

public class SQLConfig extends YamlConfig {

    public SQLConfig() {
        loadConfiguration("sql.yml", "database.yml");
    }

    public boolean isEnabled() {
        return getBoolean("mySQL.enabled");
    }

    public String getHost() {
        return getString("mySQL.host");
    }

    public int getPort() {
        return getInteger("mySQL.port");
    }

    public String getDatabase() {
        return getString("mySQL.databaseName");
    }

    public String getUser() {
        return getString("mySQL.username");
    }

    public String getPassword() {
        return getString("mySQL.password");
    }

    public String getTable() {
        return getString("mySQL.table");
    }
}
