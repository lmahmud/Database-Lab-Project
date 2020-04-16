package de.unidue.inf.is.servlets;

import de.unidue.inf.is.domain.User;
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

@WebServlet("register")
public class NewUser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtil.setLoginLogoutInReq(req);
        Optional<String> userEmail = SessionUtil.getUserEmailFromSession(req);
        if (userEmail.isPresent()) {
            error(req, resp, "You are already logged in");
            return;
        }

        req.getRequestDispatcher("/register.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionUtil.setLoginLogoutInReq(req);
        Optional<String> userEmail = SessionUtil.getUserEmailFromSession(req);
        if (userEmail.isPresent()) {
            error(req, resp, "You are already logged in");
            return;
        }

        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String password = req.getParameter("pass");


        try (Repo repo = new Repo()) {
            double balance = Double.parseDouble(req.getParameter("balance"));

            Optional<User> userByEmail = repo.findUserByEmail(email);
            if (userByEmail.isPresent()) {
                error(req, resp, "User is already registerd");
                return;
            }

            User newUser = new User(email, name, description, balance, password);
            repo.addUser(newUser);

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

