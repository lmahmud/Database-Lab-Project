package de.unidue.inf.is.domain;

import java.util.Objects;

public class Project {
  private int id;
  private String title;
  private String description;
  private String status;
  private double funding_limit;
  private String creator_email;
  private String creator_name;
  private Integer predecessor_id;
  private String predecessor_title;
  private int category_id;
  private String category_name;
  private String icon;
  private double currently_funded;

  // constructor for new projects no in the database
  public Project(
      String title,
      String description,
      double funding_limit,
      String creator_email,
      Integer predecessor_id,
      int category_id) {
    this.title = title;
    this.description = description;
    this.funding_limit = funding_limit;
    this.creator_email = creator_email;
    this.predecessor_id = predecessor_id;
    this.category_id = category_id;

    // defaults for created objects
    this.id = -1;
    this.creator_name = "";
    this.icon = "";
    this.currently_funded = 0;
    this.predecessor_title = "";
    this.category_name = "";
  }

  // constructor used by the repo to create object
  public Project(
      int id,
      String title,
      String description,
      String status,
      double funding_limit,
      String creator_email,
      String creator_name,
      Integer predecessor_id,
      String predecessor_title,
      int category_id,
      String category_name,
      String icon,
      double currently_funded) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.funding_limit = funding_limit;
    this.creator_email = creator_email;
    this.creator_name = creator_name;
    this.predecessor_id = predecessor_id;
    this.predecessor_title = predecessor_title;
    this.category_id = category_id;
    this.category_name = category_name;
    this.icon = icon;
    this.currently_funded = currently_funded;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getStatus() {
    return status;
  }

  public double getFunding_limit() {
    return funding_limit;
  }

  public String getCreator_email() {
    return creator_email;
  }

  public String getCreator_name() {
    return creator_name;
  }

  public Integer getPredecessor_id() {
    return predecessor_id;
  }

  public int getCategory_id() {
    return category_id;
  }

  public String getIcon() {
    return icon;
  }

  public double getCurrently_funded() {
    return currently_funded;
  }

  public String getPredecessor_title() {
    return predecessor_title;
  }

  public String getCategory_name() {
    return category_name;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Project otherProject = (Project) o;

    if (id != otherProject.id) return false;
    return Objects.equals(creator_email, otherProject.creator_email);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (creator_email != null ? creator_email.hashCode() : 0);
    return result;
  }
}
