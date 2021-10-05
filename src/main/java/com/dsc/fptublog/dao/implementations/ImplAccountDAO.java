package com.dsc.fptublog.dao.implementations;

import com.dsc.fptublog.dao.interfaces.IAccountDAO;
import com.dsc.fptublog.database.ConnectionWrapper;
import com.dsc.fptublog.entity.AccountEntity;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.jvnet.hk2.annotations.Service;

import java.sql.*;

import javax.inject.Inject;

@Service
@RequestScoped
public class ImplAccountDAO implements IAccountDAO {

    @Inject
    private ConnectionWrapper connectionWrapper;

    @Override
    public AccountEntity getByEmail(String email) throws SQLException {
        Connection connection = connectionWrapper.getConnection();
        if (connection == null) {
            return null;
        }

        AccountEntity result = null;

        String sql = "SELECT id, alternative_email, firstname, lastname, avatar_url, description, status_id " +
                "FROM account " +
                "WHERE email = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);

            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String id = resultSet.getString(1);
                String alternativeEmail = resultSet.getString(2);
                String firstName = resultSet.getNString(3);
                String lastName = resultSet.getNString(4);
                String avatarUrl = resultSet.getString(5);
                String description = resultSet.getNString(6);
                String statusId = resultSet.getString(7);

                result = AccountEntity.builder()
                        .id(id)
                        .email(email)
                        .alternativeEmail(alternativeEmail)
                        .firstName(firstName)
                        .lastName(lastName)
                        .avatarUrl(avatarUrl)
                        .description(description)
                        .statusId(statusId)
                        .build();
            }
        }

        return result;
    }

    @Override
    public AccountEntity createForNewEmail(String email, String name, String avatarUrl, String statusId)
            throws SQLException {
        if (email == null || name == null) {
            return null;
        }

        Connection connection = connectionWrapper.getConnection();
        if (connection == null) {
            return null;
        }

        AccountEntity result = null;

        String sql = "INSERT INTO account (email, alternative_email, firstname, lastname, avatar_url, status_id) " +
                "OUTPUT inserted.id " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            stm.setString(2, email); // because alternative_email is unique => can't be NULL is 2 different rows
            stm.setNString(3, name);
            stm.setNString(4, "");
            stm.setString(5, avatarUrl);
            stm.setString(6, statusId);

            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String id = resultSet.getString(1);

                result  = AccountEntity.builder()
                        .id(id)
                        .email(email)
                        .alternativeEmail(email)
                        .firstName(name)
                        .lastName("")
                        .avatarUrl(avatarUrl)
                        .statusId(statusId)
                        .build();
            }
        }

        return result;
    }

    @Override
    public AccountEntity getById(String id) throws SQLException {
        Connection connection = connectionWrapper.getConnection();
        if (connection == null) {
            return null;
        }

        AccountEntity result = null;

        String sql = "SELECT email, alternative_email, firstname, lastname, avatar_url, description, status_id " +
                "FROM account " +
                "WHERE id = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, id);

            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString(1);
                String alternativeEmail = resultSet.getString(2);
                String firstName = resultSet.getNString(3);
                String lastName = resultSet.getNString(4);
                String avatarUrl = resultSet.getString(5);
                String description = resultSet.getNString(6);
                String statusId = resultSet.getString(7);

                result = AccountEntity.builder()
                        .id(id)
                        .email(email)
                        .alternativeEmail(alternativeEmail)
                        .firstName(firstName)
                        .lastName(lastName)
                        .avatarUrl(avatarUrl)
                        .description(description)
                        .statusId(statusId)
                        .build();
            }
        }

        return result;
    }
}
