package de.unidue.inf.is.repo.subrepo;

import de.unidue.inf.is.domain.Comment;
import de.unidue.inf.is.repo.RepoException;
import de.unidue.inf.is.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repo to handle all comments related tasks.
 *
 * @author kt
 */
public class CommentRepo {
  private final String prepareString =
      "SELECT s.BENUTZER, b.NAME, projekt, k.TEXT, k.DATUM, k.SICHTBARKEIT, k.ID FROM SCHREIBT s\n"
          + "JOIN KOMMENTAR K on s.KOMMENTAR = K.ID\n"
          + "JOIN BENUTZER B on s.BENUTZER = B.EMAIL\n";
  private Connection conn;

  public CommentRepo() throws RepoException {
    try {
      this.conn = DBUtil.getConnection();
      this.conn.setAutoCommit(false);
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public CommentRepo(Connection conn) {
    this.conn = conn;
  }

  public void addComment(Comment comment) throws RepoException {
    String comSql = "INSERT INTO kommentar (text, sichtbarkeit) VALUES (?, ?)";
    String writeSql = "INSERT INTO schreibt (benutzer, projekt, kommentar) VALUES (?, ?, ?)";
    int comId = -1;

    try (PreparedStatement ps = conn.prepareStatement(comSql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, comment.getText());
      ps.setString(2, (comment.getVisibility()) ? "oeffentlich" : "privat");
      ps.executeUpdate();
      try (ResultSet comIdRs = ps.getGeneratedKeys()) {
        if (comIdRs.next()) {
          comId = comIdRs.getInt(1);
        }
      }

      if (comId < 1) throw new RepoException("Couldn't create comment");

      try (PreparedStatement writePs = conn.prepareStatement(writeSql)) {
        writePs.setString(1, comment.getCommenter_email());
        writePs.setInt(2, comment.getProject_id());
        writePs.setInt(3, comId);
        writePs.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public List<Comment> findByProjectId(int id) throws RepoException {
    List<Comment> coms = new ArrayList<>();
    final String psString = prepareString + "WHERE PROJEKT = ?";

    try (final PreparedStatement ps = conn.prepareStatement(psString)) {
      ps.setInt(1, id);
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          coms.add(extractComment(rs));
        }
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }
    return coms;
  }

  private Comment extractComment(ResultSet rs) throws SQLException {
    return new Comment(
        rs.getString("benutzer"),
        rs.getString("name"),
        rs.getInt("projekt"),
        rs.getString("text"),
        rs.getTimestamp("datum").toLocalDateTime(),
        rs.getString("sichtbarkeit").equals("oeffentlich"),
        rs.getInt("id"));
  }

  public List<Comment> findByUserEmail(String email) throws RepoException {
    List<Comment> coms = new ArrayList<>();
    final String psString = prepareString + "WHERE s.BENUTZER = ?";

    try (final PreparedStatement ps = conn.prepareStatement(psString)) {
      ps.setString(1, email);
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          coms.add(extractComment(rs));
        }
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }
    return coms;
  }
}
