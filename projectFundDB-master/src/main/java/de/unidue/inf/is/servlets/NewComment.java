package de.unidue.inf.is.servlets;

import de.unidue.inf.is.domain.Comment;
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

@WebServlet("new_comment")
public class NewComment extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    final String idParam = req.getParameter("id");
    if (idParam == null) {
      resp.sendRedirect("/404.html");
      return;
    }

    final Optional<String> op_commenter_email = SessionUtil.getUserEmailFromSession(req);
    if (!op_commenter_email.isPresent()) {
      error(req, resp, "You need to be logged in.");
      return;
    }
    req.getRequestDispatcher("/new_comment.ftl").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    final String idParam = req.getParameter("id");
    if (idParam == null) {
      resp.sendRedirect("/404.html");
      return;
    }

    final Optional<String> op_commenter_email = SessionUtil.getUserEmailFromSession(req);
    if (!op_commenter_email.isPresent()) {
      error(req, resp, "You need to be logged in.");
      return;
    }

    try (Repo repo = new Repo()) {
      int proj_id = Integer.parseInt(idParam.trim());

      final Optional<Project> proj = repo.findProjectById(proj_id);
      if (!proj.isPresent()) {
        resp.sendRedirect("/404.html");
        return;
      }

      String text = req.getParameter("text");
      String anonymous = req.getParameter("anonym");
      boolean isVisible = (anonymous == null || anonymous.isEmpty());

      Comment comment = new Comment(op_commenter_email.get(), proj_id, text, isVisible);
      repo.addComment(comment);

      resp.sendRedirect("/view_project?id=" + idParam);

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
