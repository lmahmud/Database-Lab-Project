package de.unidue.inf.is.repo;

import de.unidue.inf.is.domain.*;
import de.unidue.inf.is.repo.subrepo.CommentRepo;
import de.unidue.inf.is.repo.subrepo.DonationRepo;
import de.unidue.inf.is.repo.subrepo.ProjectRepo;
import de.unidue.inf.is.repo.subrepo.UserRepo;
import de.unidue.inf.is.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Central Repo to handle all database related tasks.
 *
 * @author kt
 */
public class Repo implements AutoCloseable {
  private Connection conn;

  public Repo() throws RepoException {
    try {
      this.conn = DBUtil.getConnection();
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  // projects
  public void addProject(Project project) throws RepoException {
    if (project.getFunding_limit() < 100)
      throw new RepoException("Funding limit must be greater than 100");
    new ProjectRepo(conn).addProject(project);
  }

  public void updateProject(Project project) throws RepoException {
    new ProjectRepo(conn).updateProject(project);
  }

  public List<Project> searchProjects(String searchStr) throws RepoException {
    return new ProjectRepo(conn).searchProjects(searchStr);
  }

  public void deleteProject(int project_id) throws RepoException {
    try {
      List<Donation> donations = new DonationRepo(conn).findByProjectId(project_id);

      boolean prevAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);
      // Return donations back
      for (Donation d : donations) {
        new UserRepo(conn).updateUserBalanceOffset(d.getDonor_email(), d.getAmount());
      }

      new ProjectRepo(conn).deleteProject(project_id);
      conn.commit();
      conn.setAutoCommit(prevAutoCommit);
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  public List<Project> findAllProjects() throws RepoException {
    return new ProjectRepo(conn).findAll();
  }

  public Optional<Project> findProjectById(int id) throws RepoException {
    return new ProjectRepo(conn).findById(id);
  }

  public List<Project> findProjectsByCreatorEmail(String email) throws RepoException {
    return new ProjectRepo(conn).findByCreatorEmail(email);
  }

  public List<Project> findProjectsByDonorEmail(String email) throws RepoException {
    return new ProjectRepo(conn).findByDonorEmail(email);
  }

  public List<DonationProject> findDonationProjectsByDonorEmail(String email) throws RepoException {
    final List<Project> donatedProjects = findProjectsByDonorEmail(email);
    final List<Donation> donations = findDonationsByDonorEmail(email);

    Map<Integer, Project> dpMap = new HashMap<>();
    List<DonationProject> donProjs = new ArrayList<>();
    donatedProjects.forEach(dpj -> dpMap.put(dpj.getId(), dpj));
    donations.forEach(
        don -> donProjs.add(new DonationProject(don, dpMap.get(don.getProject_id()))));

    return donProjs;
  }

  // users
  public Optional<User> findUserByEmail(String email) throws RepoException {
    return new UserRepo(conn).findByEmail(email);
  }

  public void addUser(User user) throws RepoException {
    new UserRepo(conn).addUser(user);
  }

  public void deleteUser(String user_email) throws RepoException {
    new UserRepo(conn).deleteUser(user_email);
  }

  // donations
  public List<Donation> findDonationsByProjectId(int id) throws RepoException {
    return new DonationRepo(conn).findByProjectId(id);
  }

  public List<Donation> findDonationsByDonorEmail(String email) throws RepoException {
    return new DonationRepo(conn).findByDonorEmail(email);
  }

  public void addDonation(Donation donation) throws RepoException {
    Optional<Project> projectById = findProjectById(donation.getProject_id());
    Project pj = projectById.orElseThrow(() -> new RepoException("project doesn't exist"));
    if (pj.getStatus().equals("geschlossen")) throw new RepoException("project is closed");

    User user =
        findUserByEmail(donation.getDonor_email())
            .orElseThrow(() -> new RepoException("User doesn't exists"));

    List<Donation> dons = findDonationsByProjectId(pj.getId());
    for (Donation d : dons) {
      if (d.getDonor_email().equals(donation.getDonor_email())) {
        throw new RepoException("User has already donated");
      }
    }

    if (donation.getAmount() <= 0)
      throw new RepoException("Donation amount must be greater than 0");

    if (user.getAccount_balance() < donation.getAmount())
      throw new RepoException("User doesn't have sufficient funds");

    try {
      boolean prevAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      new DonationRepo(conn).addDonation(donation);
      new UserRepo(conn).updateUserBalanceOffset(user.getEmail(), -1 * donation.getAmount());

      conn.commit();
      conn.setAutoCommit(prevAutoCommit);
    } catch (SQLException e) {
      throw new RepoException(e);
    }
  }

  // comments
  public List<Comment> findCommentsByProjectId(int id) throws RepoException {
    return new CommentRepo(conn).findByProjectId(id);
  }

  public void addComment(Comment comment) throws RepoException {
    new CommentRepo(conn).addComment(comment);
  }

  // others
  public List<Category> getCategories() throws RepoException {
    List<Category> result = new ArrayList<>();
    final String prepareString = "SELECT ID,NAME FROM KATEGORIE ORDER BY ID";

    try (final PreparedStatement ps = conn.prepareStatement(prepareString)) {
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          result.add(new Category(rs.getInt("Id"), rs.getString("Name")));
        }
      }
    } catch (SQLException e) {
      throw new RepoException(e);
    }

    return result;
  }

  @Override
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
