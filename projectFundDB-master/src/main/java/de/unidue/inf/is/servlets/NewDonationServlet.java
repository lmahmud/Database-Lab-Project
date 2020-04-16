package de.unidue.inf.is.servlets;

import de.unidue.inf.is.domain.Donation;
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

@WebServlet("new_project_fund")
public class NewDonationServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    SessionUtil.setLoginLogoutInReq(req);
    final Optional<String> sessionUserEmail = SessionUtil.getUserEmailFromSession(req);
    if (!sessionUserEmail.isPresent()) {
      error(req, resp, "You need to be logged in to donate");
      return;
    }

    req.getRequestDispatcher("/new_project_fund.ftl").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    final String idParam = req.getParameter("id");
    if (idParam == null) {
      resp.sendRedirect("/404.html");
      return;
    }

    final Optional<String> op_donor_email = SessionUtil.getUserEmailFromSession(req);
    if (!op_donor_email.isPresent()) {
      error(req, resp, "You need to be logged in.");
      return;
    }

    try (Repo repo = new Repo()) {
      int proj_id = Integer.parseInt(idParam.trim());

      String amountStr = req.getParameter("amount");
      String anonymous = req.getParameter("anonym");
      boolean isVisible = (anonymous == null || anonymous.isEmpty());

      double amount = Double.parseDouble(amountStr.trim());

      Donation donation = new Donation(op_donor_email.get(), proj_id, amount, isVisible);
      repo.addDonation(donation);

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
