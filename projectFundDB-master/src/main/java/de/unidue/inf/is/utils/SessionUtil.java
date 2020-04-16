package de.unidue.inf.is.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtil {
  public static boolean isSameUserAsSession(HttpServletRequest req, String email) {
    final HttpSession session = req.getSession(false);
    if (session == null) return false;

    final String semail = (String) session.getAttribute("user");
    if (semail == null) return false;

    return semail.equals(email);
  }

  public static Optional<String> getUserEmailFromSession(HttpServletRequest req) {
    final HttpSession session = req.getSession(false);
    if (session == null) return Optional.empty();

    return Optional.ofNullable((String) session.getAttribute("user"));
  }

  public static boolean hasASession(HttpServletRequest req) {
    final HttpSession session = req.getSession(false);
    return session != null;
  }

  public static void setLoginLogoutInReq(HttpServletRequest req) {
    String lg = (SessionUtil.hasASession(req)) ? "logout" : "login";
    req.setAttribute("lglo", lg);
  }
}
