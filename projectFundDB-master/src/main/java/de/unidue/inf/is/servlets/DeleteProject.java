package de.unidue.inf.is.servlets;

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
import java.util.Optional;

@WebServlet("delete_project")
public class DeleteProject extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    SessionUtil.setLoginLogoutInReq(req);

    final String idParam = req.getParameter("id");
    if (idParam == null) {
      resp.sendRedirect("/404.html");
      return;
    }

    try (Repo repo = new Repo()) {
      int id = Integer.parseInt(idParam.trim());

      final Optional<Project> proj = repo.findProjectById(id);
      if (!proj.isPresent()) {
        resp.sendRedirect("/404.html");
        return;
      }

      final Optional<String> sessionUserEmail = SessionUtil.getUserEmailFromSession(req);
      if (!sessionUserEmail.isPresent()
          || !proj.get().getCreator_email().equals(sessionUserEmail.get())) {
        error(req, resp, "You need to be logged in as the owner of the project");
        return;
      }

      repo.deleteProject(id);

      req.setAttribute("pageTitle", "Success");
      req.setAttribute("msg", "Project : '" + proj.get().getTitle() + "' deleted.");
      req.setAttribute("redirect_url", "/");
      req.getRequestDispatcher("/info.ftl").forward(req, resp);

    } catch (RepoException | NumberFormatException e) {
      error(req, resp, "Error occurred ! : " + e.getMessage());
    }
  }

  private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
      throws ServletException, IOException {
    req.setAttribute("msg", msg);
    req.setAttribute("redirect_url", "/");
    req.getRequestDispatcher("/info.ftl").forward(req, resp);
  }
}
