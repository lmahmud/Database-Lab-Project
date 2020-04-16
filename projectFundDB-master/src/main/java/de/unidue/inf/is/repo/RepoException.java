package de.unidue.inf.is.repo;

public class RepoException extends Exception {
  public RepoException(Exception e) {
    super(e);
  }

  public RepoException(String message) {
    super(message);
  }
}
