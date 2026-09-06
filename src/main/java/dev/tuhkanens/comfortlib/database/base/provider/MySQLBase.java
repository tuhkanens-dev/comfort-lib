package dev.tuhkanens.comfortlib.database.base.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.tuhkanens.comfortlib.ComfortAPI;
import dev.tuhkanens.comfortlib.database.base.DatabaseBase;
import org.jdbi.v3.core.Jdbi;
import org.spongepowered.configurate.ConfigurationNode;

public class MySQLBase extends DatabaseBase {

    private HikariDataSource dataSource;

    @Override
    protected Jdbi createConnection() {
        ConfigurationNode node = ComfortAPI.CONFIG.getNode().node("database");

        String host = node.node("host").getString("localhost");
        String port = node.node("port").getString("3306");
        String database = node.node("database").getString("comfort");
        String username = node.node("username").getString("root");
        String password = node.node("password").getString("");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&characterEncoding=utf8");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30_000);
        config.setIdleTimeout(600_000);
        config.setMaxLifetime(1_800_000);

        dataSource = new HikariDataSource(config);
        return Jdbi.create(dataSource);
    }

    @Override
    public void closeConnection() {
        if (dataSource == null) return;
        dataSource.close();
    }
}
