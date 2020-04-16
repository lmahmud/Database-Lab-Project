package de.unidue.inf.is.repo.subrepo;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.repo.RepoException;
import de.unidue.inf.is.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Repo to handle all comments related tasks.
 *
 * @author kt
 */
public class UserRepo {
  private Connection conn;

  public UserRepo(Connection conn) {
    this.conn = conn;
  }

  public void addUser(User user) throws RepoException {
    String usrSql = "INSERT INTO benutzer (email, name, beschreibung) VALUES (?, ?, ?)";
    String accSql = "INSERT INTO konto (inhaber, guthaben, geheimzahl) VALUES (?, ?, ?)";

    try (PreparedStatement usrPs = conn.prepareStatement(usrSql)) {
      usrPs.setString(1, user.getEmail());
      usrPs.setString(2, user.getName());
      usrPs.setString(3, user.getDescription());
      usrPs.executeUpdate();

      try (PreparedStatement accPs = conn.prepareStatement(accSql)) {
        accPs.setString(1, user.getEmail());
        accPs.setDouble(2, user.getAccount_balance());
        accPs.setString(3, user.getSecret_code());
        accPs.executeUpdate();
      }

    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public void updateUser(User user) throws RepoException {
    String usrSql = "UPDATE benutzer SET name = ?, beschreibung = ? WHERE email = ?";
    String accSql = "UPDATE konto SET guthaben = ?, geheimzahl = ? WHERE inhaber = ?";

    try (PreparedStatement usrPs = conn.prepareStatement(usrSql)) {
      usrPs.setString(1, user.getName());
      usrPs.setString(2, user.getDescription());
      usrPs.setString(3, user.getEmail());
      usrPs.executeUpdate();

      try (PreparedStatement accPs = conn.prepareStatement(accSql)) {
        accPs.setDouble(1, user.getAccount_balance());
        accPs.setString(2, user.getSecret_code());
        accPs.setString(3, user.getEmail());
        accPs.executeUpdate();
      }

    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public void updateUserBalanceOffset(String user_email, double offset) throws RepoException {
    String sql = "UPDATE konto SET guthaben = guthaben + ? WHERE inhaber = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setDouble(1, offset);
      ps.setString(2, user_email);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public void deleteUser(String user_email) throws RepoException {}

  public Optional<User> findByEmail(String email) throws RepoException {
    try (PreparedStatement ps =
        conn.prepareStatement(
            "SELECT * FROM benutzer b JOIN konto k ON b.email=k.inhaber WHERE b.email=?")) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(extractUser(rs));
        }
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }

    return Optional.empty();
  }

  private User extractUser(ResultSet rs) throws SQLException {
    return new User(
        rs.getString("email"),
        rs.getString("name"),
        rs.getString("beschreibung"),
        rs.getDouble("guthaben"),
        rs.getString("geheimzahl"));
  }

  public void close() throws RepoException {
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

}
