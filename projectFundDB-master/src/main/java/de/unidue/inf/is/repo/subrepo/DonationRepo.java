package de.unidue.inf.is.repo.subrepo;

import de.unidue.inf.is.domain.Donation;
import de.unidue.inf.is.repo.RepoException;
import de.unidue.inf.is.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repo to handle all comments related tasks.
 *
 * @author kt
 */
public class DonationRepo {
  private final String prepareString =
      "SELECT s.SPENDER, b.NAME, p.KENNUNG, s.SPENDENBETRAG, s.SICHTBARKEIT FROM SPENDEN s\n"
          + "JOIN BENUTZER b ON b.EMAIL = s.SPENDER\n"
          + "JOIN PROJEKT P on s.PROJEKT = P.KENNUNG\n";
  private Connection conn;

  public DonationRepo() throws RepoException {
    try {
      this.conn = DBUtil.getConnection();
      this.conn.setAutoCommit(false);
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public DonationRepo(Connection conn) {
    this.conn = conn;
  }

  public void addDonation(Donation donation) throws RepoException {
    String sql =
        "INSERT INTO spenden (spender, projekt, spendenbetrag, sichtbarkeit) "
            + "VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, donation.getDonor_email());
      ps.setInt(2, donation.getProject_id());
      ps.setDouble(3, donation.getAmount());
      ps.setString(4, (donation.getVisibility()) ? "oeffentlich" : "privat");
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public List<Donation> findByProjectId(int id) throws RepoException {
    List<Donation> dons = new ArrayList<>();
    final String psString = prepareString + "WHERE P.KENNUNG = ?";

    try (final PreparedStatement ps = conn.prepareStatement(psString)) {
      ps.setInt(1, id);
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          dons.add(extractDonation(rs));
        }
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }
    return dons;
  }

  public List<Donation> findByDonorEmail(String email) throws RepoException {
    List<Donation> dons = new ArrayList<>();
    final String psString = prepareString + "WHERE b.EMAIL = ?";

    try (final PreparedStatement ps = conn.prepareStatement(psString)) {
      ps.setString(1, email);
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          dons.add(extractDonation(rs));
        }
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }
    return dons;
  }

  private Donation extractDonation(ResultSet rs) throws SQLException {
    return new Donation(
        rs.getString("spender"),
        rs.getString("name"),
        rs.getInt("kennung"),
        rs.getDouble("spendenbetrag"),
        rs.getString("sichtbarkeit").equals("oeffentlich"));
  }
}
