package de.unidue.inf.is.servlets;

import de.unidue.inf.is.domain.DonationProject;
import de.unidue.inf.is.domain.Project;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.repo.Repo;
import de.unidue.inf.is.repo.RepoException;
import de.unidue.inf.is.utils.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("view_profile")
public class ViewProfileServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    SessionUtil.setLoginLogoutInReq(req);

    final String emailParam = req.getParameter("email");
    if (emailParam == null) {
      error(req, resp);
      return;
    }

    try (Repo repo = new Repo()) {
      final Optional<User> user = repo.findUserByEmail(emailParam);
      if (!user.isPresent()) {
        error(req, resp);
        return;
      }

      List<DonationProject> donProjs = repo.findDonationProjectsByDonorEmail(emailParam);
      List<Project> createdProjects = repo.findProjectsByCreatorEmail(emailParam);

      req.setAttribute("user", user.get());
      req.setAttribute("cpjs", createdProjects);
      req.setAttribute("donpjs", donProjs);

      req.setAttribute("own_profile", SessionUtil.isSameUserAsSession(req, emailParam));
      req.getRequestDispatcher("/view_profile.ftl").forward(req, resp);
    } catch (RepoException e) {
      error(req, resp);
    }
  }

  private void error(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.sendRedirect("/404.html");
  }
}
