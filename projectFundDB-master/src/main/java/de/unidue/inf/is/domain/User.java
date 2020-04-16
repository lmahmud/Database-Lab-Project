package de.unidue.inf.is.domain;

public class User {
  private String email;
  private String name;
  private String description;
  private double account_balance;
  private String secret_code;

  public User(
      String email, String name, String description, double account_balance, String secret_code) {
    this.email = email;
    this.name = name;
    this.description = description;
    this.account_balance = account_balance;
    this.secret_code = secret_code;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public double getAccount_balance() {
    return account_balance;
  }

  public void setAccount_balance(double account_balance) {
    this.account_balance = account_balance;
  }

  public String getSecret_code() {
    return secret_code;
  }
}
