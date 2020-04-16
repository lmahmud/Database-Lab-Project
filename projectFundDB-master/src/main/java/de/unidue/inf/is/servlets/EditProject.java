package de.unidue.inf.is.servlets;

import de.unidue.inf.is.domain.Category;
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

@WebServlet("edit_project")
public class EditProject extends HttpServlet {
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

      final List<Category> catgos = repo.getCategories();
      List<Project> userProjects = repo.findProjectsByCreatorEmail(sessionUserEmail.get());
      userProjects.remove(proj.get());

      req.setAttribute("categories", catgos);
      req.setAttribute("pj", proj.get());
      req.setAttribute("preds", userProjects);
      req.setAttribute("title", "Edit Project");

      req.getRequestDispatcher("/new_project.ftl").forward(req, resp);
    } catch (RepoException | NumberFormatException e) {
      error(req, resp, "Error occurred ! : " + e.getMessage());
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    final String idParam = req.getParameter("id");
    if (idParam == null) {
      resp.sendRedirect("/404.html");
      return;
    }

    final Optional<String> op_creator_email = SessionUtil.getUserEmailFromSession(req);
    if (!op_creator_email.isPresent()) {
      error(req, resp, "You need to be logged in as the owner of the project");
      return;
    }

    String title = req.getParameter("title");
    String flimitstr = req.getParameter("flimit");
    String category = req.getParameter("category");
    String predecessor = req.getParameter("predecessor");
    String description = req.getParameter("description");
    final String category_id_str = category.split("-")[0];
    final String predecessor_id_str = predecessor.split("-")[0];

    double funding_limit;
    int category_id, project_id;
    Integer predecessor_id;
    try {
      project_id = Integer.parseInt(idParam);
      funding_limit = Double.parseDouble(flimitstr);
      category_id = Integer.parseInt(category_id_str);
      predecessor_id = Integer.parseInt(predecessor_id_str);
    } catch (NumberFormatException | NullPointerException e) {
      error(req, resp, "Numbers must be valid number");
      return;
    }

    if (predecessor_id == 0) predecessor_id = null;

    Project project =
        new Project(
            title, description, funding_limit, op_creator_email.get(), predecessor_id, category_id);
    project.setId(project_id);

    try (final Repo repo = new Repo()) {
      final Optional<Project> proj = repo.findProjectById(project_id);
      if (!proj.isPresent()) {
        resp.sendRedirect("/404.html");
        return;
      }

      if (funding_limit < proj.get().getFunding_limit()) {
        error(req, resp, "Funding limit must be greater or equal to the current");
        return;
      }

      repo.updateProject(project);

    } catch (RepoException e) {
      error(req, resp, e.getMessage());
      return;
    }

    resp.sendRedirect("/view_project?id=" + project.getId());
  }

  private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
      throws ServletException, IOException {
    req.setAttribute("msg", msg);
    req.setAttribute("redirect_url", "/");
    req.getRequestDispatcher("/info.ftl").forward(req, resp);
  }
}
