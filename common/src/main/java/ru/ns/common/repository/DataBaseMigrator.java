package ru.ns.common.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseMigrator {
    private static final Logger logger = Logger.getLogger(DataBaseMigrator.class.getName());
    private static final int CURRENT_VERSION = 2;

    private final DataBaseConnector connector;
    private int schemaVersion;


    public DataBaseMigrator(DataBaseConnector connector) {
        this.connector = connector;
        logger.fine("Initializing database migrator with connector");
        detectSchemaVersion();
        logger.fine("Database migrator initialized with schema version: " + schemaVersion);
    }

    private void detectSchemaVersion() {
        logger.fine("Detecting database schema version");
        String checkTableSQL = "SELECT 1 FROM user_tables WHERE table_name = 'SCHEMA_VERSION'";

        try (Connection connection = connector.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(checkTableSQL)) {

            if (!rs.next()) {
                logger.info("Schema version table not found, creating schema_version table");
                stmt.execute("CREATE TABLE schema_version (version NUMBER(10) NOT NULL)");
                stmt.execute("INSERT INTO schema_version (version) VALUES (0)");
                logger.info("Table schema_version created. schemaVersion=0");
                this.schemaVersion = 0;
            }

            else {
                logger.fine("Schema version table found, retrieving current version");
                try (ResultSet rs2 = stmt.executeQuery("SELECT NVL(MAX(version), 0) AS ver FROM schema_version")) {
                    if (rs2.next()) {
                        this.schemaVersion = rs2.getInt("ver");
                        logger.info("Detected schema version: " + schemaVersion);
                    } else {
                        this.schemaVersion = 0;
                        logger.warning("Could not retrieve schema version, defaulting to 0");
                    }
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error detecting schema version", e);
        }
    }


    public void migrate() {
        logger.info("Starting database migration process. Current version: " + schemaVersion + ", Target version: " + CURRENT_VERSION);
        while (schemaVersion < CURRENT_VERSION) {
            logger.info("Migrating from version " + schemaVersion + " to version " + (schemaVersion + 1));
            try {
                switch (schemaVersion) {
                    case 0:
                        logger.info("Starting migration to version 1");
                        migrateTo1();
                        logger.info("Migration to version 1 completed");
                        break;
                    case 1:
                        logger.info("Starting migration to version 2");
                        migrateTo2();
                        logger.info("Migration to version 2 completed");
                        break;
                    default:
                        logger.warning("Unexpected schema version: " + schemaVersion);
                        break;
                }

                schemaVersion ++;
                logger.info("Updated schema version to " + schemaVersion);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error while migrating from version " + schemaVersion, e);
                throw new RuntimeException("Database migration failed", e);
            }
        }
        logger.info("Database migration completed. Final version: " + schemaVersion);
    }

    private void migrateTo1() throws SQLException {
        logger.fine("Executing migration to version 1");
        String createTableSQL = """
            CREATE TABLE hit_results (
                id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                x NUMBER(10, 8) NOT NULL,
                y NUMBER(10, 8) NOT NULL,
                r NUMBER(10, 8) NOT NULL,
                is_hit NUMBER(1) NOT NULL,
                max_miss_r NUMBER(10, 8) NOT NULL
            )
        """;

        try (Connection connection = connector.getConnection();
            Statement stmt = connection.createStatement();) {
            stmt.execute(createTableSQL);

            logger.info("Table hit_results created.");

            stmt.execute("INSERT INTO schema_version (version) VALUES (1)");
            logger.fine("Updated schema version to 1");
        }
    }

    private void migrateTo2() throws SQLException {
        logger.fine("Executing migration to version 2");
        String alterTableSQL = """
            ALTER TABLE hit_results ADD (deleted NUMBER(1) DEFAULT 0 NOT NULL)
        """;

        try (Connection connection = connector.getConnection();
            Statement stmt = connection.createStatement();) {
            stmt.execute(alterTableSQL);

            logger.info("Table hit_results updated with soft-delete column.");

            stmt.execute("INSERT INTO schema_version (version) VALUES (2)");
            logger.fine("Updated schema version to 2");
        }
    }
}
