package edu.java.scrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LiquibaseMigrationsTest extends IntegrationEnvironment {
    @Test
    @SneakyThrows
    void liquibaseMigrationsShouldCreateCorrectChatTable() {
        Connection connection = POSTGRES.createConnection("");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from chat;");
        ResultSet resultSet = preparedStatement.executeQuery();
        assertThat(resultSet.findColumn("id")).isEqualTo(1);
        assertThat(resultSet.findColumn("created_at")).isEqualTo(2);
    }

    @Test
    @SneakyThrows
    void liquibaseMigrationsShouldCreateCorrectLinkTable() {
        Connection connection = POSTGRES.createConnection("");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from link;");
        ResultSet resultSet = preparedStatement.executeQuery();
        assertThat(resultSet.findColumn("id")).isEqualTo(1);
        assertThat(resultSet.findColumn("url")).isEqualTo(2);
        assertThat(resultSet.findColumn("last_check_at")).isEqualTo(3);
        assertThat(resultSet.findColumn("updated_at")).isEqualTo(4);
    }

    @Test
    @SneakyThrows
    void liquibaseMigrationsShouldCreateCorrectChatLinkTable() {
        Connection connection = POSTGRES.createConnection("");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from subscription;");
        ResultSet resultSet = preparedStatement.executeQuery();
        assertThat(resultSet.findColumn("link_id")).isEqualTo(1);
        assertThat(resultSet.findColumn("chat_id")).isEqualTo(2);
        assertThat(resultSet.findColumn("created_at")).isEqualTo(3);
    }
}
