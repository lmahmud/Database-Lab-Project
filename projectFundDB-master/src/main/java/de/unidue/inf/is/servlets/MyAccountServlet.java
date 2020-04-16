package de.unidue.inf.is.servlets;

import de.unidue.inf.is.utils.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("myaccount")
public class MyAccountServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    if (!SessionUtil.hasASession(req)) {
      error(req, resp);
      return;
    }

    final Optional<String> email = SessionUtil.getUserEmailFromSession(req);
    if (!email.isPresent()) {
      error(req, resp);
      return;
    }

    SessionUtil.setLoginLogoutInReq(req);
    resp.sendRedirect("/view_profile?email=" + email.get());
  }

  public void error(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setAttribute("msg", "You need to be logged in to continue");
    req.setAttribute("redirect_url", "/login");

    req.getRequestDispatcher("/info.ftl").forward(req, resp);
  }
}
