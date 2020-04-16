package de.unidue.inf.is.servlets;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.repo.Repo;
import de.unidue.inf.is.repo.RepoException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet({"/login", "/Login"})
public class LoginServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher("/login.ftl").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String username = req.getParameter("user");
    String pass = req.getParameter("pwd");

    try (Repo repo = new Repo()) {
      final Optional<User> usr = repo.findUserByEmail(username.trim());
      if (usr.isPresent() && usr.get().getSecret_code().equals(pass)) {
        HttpSession session = req.getSession();
        session.setAttribute("user", username);
        resp.sendRedirect("/");
      } else {
        error(req, resp, "username or password is incorrect");
      }
    } catch (RepoException e) {
      error(req, resp, e.getMessage());
    }
  }

  private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
      throws ServletException, IOException {
    req.setAttribute("msg", msg);
    req.setAttribute("redirect_url", "/login");
    req.getRequestDispatcher("/info.ftl").forward(req, resp);
  }
}
