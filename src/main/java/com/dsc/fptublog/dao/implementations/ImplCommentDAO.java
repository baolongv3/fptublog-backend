package com.dsc.fptublog.dao.implementations;

import com.dsc.fptublog.dao.interfaces.ICommentDAO;
import com.dsc.fptublog.database.ConnectionWrapper;
import com.dsc.fptublog.entity.CommentEntity;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImplCommentDAO implements ICommentDAO {

    @Inject
    private ConnectionWrapper connectionWrapper;

    @Override
    public CommentEntity insertNewComment(String blogId, String authorId, String content, long postedDatetime, String statusId, String replyTo) throws SQLException {
        Connection connection = connectionWrapper.getConnection();
        CommentEntity comment = null;
        if (blogId == null || authorId == null) {
            return null;
        }
        if (connection != null) {
            return null;
        }
        String sql = "INSERT INTO comment (blog_id, author_id, content, posted_datetime, posted_datetime, reply_to "
                + "OUTPUT inserted.id "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, blogId);
            stm.setString(2, authorId);
            stm.setNString(3, content);
            stm.setLong(4, postedDatetime);
            stm.setString(5, statusId);
            stm.setString(6, replyTo);

            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String id = resultSet.getString(1);
                comment = CommentEntity.builder()
                        .id(id)
                        .blogId(blogId)
                        .authorId(authorId)
                        .content(content)
                        .postedDatetime(postedDatetime)
                        .statusId(statusId)
                        .replyTo(replyTo)
                        .build();

            }
        }
        return comment;
    }

    @Override
    public List<CommentEntity> getAllCommentsByBlogId(String blogId) throws SQLException {
        Connection connection = connectionWrapper.getConnection();
        if (connection == null) {
            return null;
        }
        List<CommentEntity> commentsList = null;

        String sql = "SELECT id, author_id, content, posted_datetime, status_id, reply_to "
                + "FROM comment "
                + "WHERE blog_id = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, blogId);
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                if (commentsList == null) {
                    commentsList = new ArrayList<>();
                }
                String id = resultSet.getString(1);
                String authorId = resultSet.getString(2);
                String content = resultSet.getNString(3);
                Long postedDatetime = resultSet.getLong(4);
                String statusId = resultSet.getString(5);
                String replyTo = resultSet.getString(6);

                commentsList.add(CommentEntity.builder()
                        .id(id)
                        .blogId(blogId)
                        .authorId(authorId)
                        .content(content)
                        .postedDatetime(postedDatetime)
                        .statusId(statusId)
                        .replyTo(replyTo)
                        .build());
            }
        }
        return commentsList;
    }
}
