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

@WebServlet("new_project")
public class NewProject extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    SessionUtil.setLoginLogoutInReq(req);

    try (Repo repo = new Repo()) {

      final Optional<String> sessionUserEmail = SessionUtil.getUserEmailFromSession(req);
      if (!sessionUserEmail.isPresent()) {
        error(req, resp, "You need to be logged in to create a project");
        return;
      }

      final List<Category> categoryList = repo.getCategories();
      List<Project> userProjects = repo.findProjectsByCreatorEmail(sessionUserEmail.get());

      req.setAttribute("categories", categoryList);
      req.setAttribute("preds", userProjects);
      req.setAttribute("title", "New Project");

      req.getRequestDispatcher("/new_project.ftl").forward(req, resp);
    } catch (RepoException | NumberFormatException e) {
      error(req, resp, "Error occurred ! : " + e.getMessage());
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

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
    int category_id;
    Integer predecessor_id;
    try {
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

    try (final Repo repo = new Repo()) {
      repo.addProject(project);
    } catch (RepoException e) {
      error(req, resp, e.getMessage());
      return;
    }

    resp.sendRedirect("/");
  }

  private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
      throws ServletException, IOException {
    req.setAttribute("msg", msg);
    req.setAttribute("redirect_url", "/");
    req.getRequestDispatcher("/info.ftl").forward(req, resp);
  }
}
