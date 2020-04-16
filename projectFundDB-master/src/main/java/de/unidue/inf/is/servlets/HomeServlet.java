package de.unidue.inf.is.servlets;

import de.unidue.inf.is.repo.Repo;
import de.unidue.inf.is.repo.RepoException;
import de.unidue.inf.is.utils.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "")
public final class HomeServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try (Repo repo = new Repo()) {
      req.setAttribute("projects", repo.findAllProjects());
      SessionUtil.setLoginLogoutInReq(req);
      req.getRequestDispatcher("/index.ftl").forward(req, resp);

    } catch (RepoException e) {
      req.setAttribute("msg", "Retrieving items from database failed: " + e.getMessage());
      req.setAttribute("redirect_url", "/404.html");
      req.getRequestDispatcher("/info.ftl").forward(req, resp);
    }
  }
}
