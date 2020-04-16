package de.unidue.inf.is.domain;

public class DonationProject {
  private Donation donation;
  private Project project;

  public DonationProject(Donation donation, Project project) {
    this.donation = donation;
    this.project = project;
  }

  public Donation getDonation() {
    return donation;
  }

  public Project getProject() {
    return project;
  }
}
