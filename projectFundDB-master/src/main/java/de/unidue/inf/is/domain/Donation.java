package de.unidue.inf.is.domain;

public class Donation {
  private String donor_email;
  private String donor_name;
  private int project_id;
  private double amount;
  private boolean visibility;

  public Donation(String donor_email, int project_id, double amount, boolean visibility) {
    this.donor_email = donor_email;
    this.project_id = project_id;
    this.amount = amount;
    this.visibility = visibility;
  }

  public Donation(
      String donor_email, String donor_name, int project_id, double amount, boolean visibility) {
    this.donor_email = donor_email;
    this.donor_name = donor_name;
    this.project_id = project_id;
    this.amount = amount;
    this.visibility = visibility;
  }

  public String getDonor_email() {
    return donor_email;
  }

  public int getProject_id() {
    return project_id;
  }

  public double getAmount() {
    return amount;
  }

  public boolean getVisibility() {
    return visibility;
  }

  public String getDonor_name() {
    return donor_name;
  }
}
