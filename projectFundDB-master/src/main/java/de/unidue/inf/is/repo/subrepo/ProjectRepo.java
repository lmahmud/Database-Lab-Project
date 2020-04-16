package de.unidue.inf.is.repo.subrepo;

import de.unidue.inf.is.domain.Project;
import de.unidue.inf.is.repo.RepoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repo to handle all comments related tasks.
 *
 * @author kt
 */
public class ProjectRepo {
  private Connection conn;
  private final String projectPreparedString =
      "SELECT p.kennung, p.titel, p.BESCHREIBUNG, p.STATUS, p.FINANZIERUNGSLIMIT, b.email cemail, b.name cname, "
          + "prep.KENNUNG preId, prep.TITEL preTitle, k.id kId, k.NAME kname, k.icon, dons.funded FROM projekt p\n"
          + "JOIN benutzer b ON p.ersteller = b.email\n"
          + "JOIN kategorie k ON p.kategorie = k.id\n"
          + "LEFT JOIN projekt prep ON p.VORGAENGER = prep.KENNUNG\n"
          + "LEFT JOIN (SELECT PROJEKT, SUM(SPENDENBETRAG) funded FROM SPENDEN GROUP BY PROJEKT) dons "
          + "      ON dons.PROJEKT = p.KENNUNG \n";

  public ProjectRepo(Connection conn) {
    this.conn = conn;
  }

  public void addProject(Project project) throws RepoException {
    String sql =
        "INSERT INTO projekt(titel, beschreibung, finanzierungslimit, ersteller, vorgaenger, kategorie) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    try (final PreparedStatement ps = conn.prepareStatement(sql)) {
      addUpdateHelper(project, ps);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  private void addUpdateHelper(Project project, PreparedStatement ps) throws SQLException {
    ps.setString(1, project.getTitle());
    ps.setString(2, project.getDescription());
    ps.setDouble(3, project.getFunding_limit());
    ps.setString(4, project.getCreator_email());
    if (project.getPredecessor_id() == null || project.getPredecessor_id() < 1) {
      ps.setNull(5, java.sql.Types.INTEGER);
    } else {
      ps.setInt(5, project.getPredecessor_id());
    }
    ps.setInt(6, project.getCategory_id());
  }

  public void updateProject(Project project) throws RepoException {
    String sql =
        "UPDATE PROJEKT SET TITEL=?, BESCHREIBUNG=?, FINANZIERUNGSLIMIT=?, ERSTELLER=?, VORGAENGER=?, KATEGORIE=? "
            + "WHERE KENNUNG=?";
    try (final PreparedStatement ps = conn.prepareStatement(sql)) {
      addUpdateHelper(project, ps);
      ps.setInt(7, project.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public void deleteProject(int project_id) throws RepoException {
    String sql = "DELETE FROM PROJEKT WHERE KENNUNG = ?";
    try (final PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, project_id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  private List<Project> findProjectList(PreparedStatement ps) throws SQLException {
    List<Project> projects = new ArrayList<>();
    try (final ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        projects.add(extractProjectFromResultSet(rs));
      }
    }
    return projects;
  }

  Project extractProjectFromResultSet(ResultSet rs) throws SQLException {
    return new Project(
        rs.getInt("Kennung"),
        rs.getString("Titel"),
        rs.getString("Beschreibung"),
        rs.getString("Status"),
        rs.getDouble("Finanzierungslimit"),
        rs.getString("cEmail"),
        rs.getString("cName"),
        rs.getInt("preId"),
        rs.getString("preTitle"),
        rs.getInt("kId"),
        rs.getString("kName"),
        rs.getString("Icon"),
        rs.getDouble("funded"));
  }

  public List<Project> findAll() throws RepoException {
    List<Project> projects = new ArrayList<>();
    try (final PreparedStatement ps = conn.prepareStatement(projectPreparedString)) {
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          projects.add(extractProjectFromResultSet(rs));
        }
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }
    return projects;
  }

  public List<Project> searchProjects(String searchStr) throws RepoException {
    List<Project> projects = new ArrayList<>();
    String sql = projectPreparedString + " WHERE p.titel LIKE ?";
    try (final PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, "%" + searchStr.trim() + "%");
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          projects.add(extractProjectFromResultSet(rs));
        }
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }
    return projects;
  }

  public List<Project> findByCreatorEmail(String email) throws RepoException {
    String psString = projectPreparedString + "WHERE b.EMAIL = ?";
    return getProjectsfromEmail(email, psString);
  }

  public List<Project> findByDonorEmail(String email) throws RepoException {
    String psString =
        projectPreparedString
            + "JOIN SPENDEN ds on p.KENNUNG = ds.PROJEKT\n"
            + "WHERE ds.SPENDER = ?";
    return getProjectsfromEmail(email, psString);
  }

  public Optional<Project> findById(int id) throws RepoException {
    String psString = projectPreparedString + "WHERE p.KENNUNG = ?";
    try (final PreparedStatement ps = conn.prepareStatement(psString)) {
      ps.setInt(1, id);
      final List<Project> projects = findProjectList(ps);
      if (!projects.isEmpty()) {
        return Optional.ofNullable(projects.get(0));
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }

    return Optional.empty();
  }

  private List<Project> getProjectsfromEmail(String email, String prepareString)
      throws RepoException {
    final List<Project> projects;

    try (PreparedStatement ps = conn.prepareStatement(prepareString)) {
      ps.setString(1, email);
      projects = findProjectList(ps);
    } catch (SQLException e) {
      throw new RepoException(e);
    }

    return projects;
  }
}
