package de.unidue.inf.is.servlets;

import de.unidue.inf.is.domain.Comment;
import de.unidue.inf.is.domain.Donation;
import de.unidue.inf.is.domain.Project;
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

@WebServlet("view_project")
public class ViewProjectServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    SessionUtil.setLoginLogoutInReq(req);

    final String idParam = req.getParameter("id");
    if (idParam == null) {
      error(req, resp);
      return;
    }

    try (Repo repo = new Repo()) {
      int id = Integer.parseInt(idParam.trim());

      final Optional<Project> proj = repo.findProjectById(id);
      if (!proj.isPresent()) {
        error(req, resp);
        return;
      }

      final List<Donation> donations = repo.findDonationsByProjectId(id);
      final List<Comment> comments = repo.findCommentsByProjectId(id);

      req.setAttribute("pj", proj.get());
      req.setAttribute("donations", donations);
      req.setAttribute("comments", comments);

      req.setAttribute(
          "own_project", SessionUtil.isSameUserAsSession(req, proj.get().getCreator_email()));
      req.getRequestDispatcher("/view_project.ftl").forward(req, resp);

    } catch (RepoException e) {
      error(req, resp);
    }
  }

  private void error(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.sendRedirect("/404.html");
  }
}
