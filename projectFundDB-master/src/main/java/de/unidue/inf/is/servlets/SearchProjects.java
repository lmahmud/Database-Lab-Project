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
import java.util.List;

@WebServlet("/search_projects")
public class SearchProjects extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    SessionUtil.setLoginLogoutInReq(req);
    req.getRequestDispatcher("/search_projects.ftl").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String searchStr = req.getParameter("title");

    try (Repo repo = new Repo()) {
      List<Project> pjs = repo.searchProjects(searchStr);
      req.setAttribute("projects", pjs);
      req.setAttribute("searchStr", searchStr);
      req.getRequestDispatcher("/search_results.ftl").forward(req, resp);

    } catch (RepoException e) {
      req.setAttribute("msg", "Error: " + e.getMessage());
      req.setAttribute("redirect_url", "/");
      req.getRequestDispatcher("/info.ftl").forward(req, resp);
    }
  }
}
