package de.unidue.inf.is.domain;

import java.time.LocalDateTime;

public class Comment {
  private String commenter_email;
  private String commenter_name;
  private int project_id;
  private String text;
  private LocalDateTime dateTime;
  private boolean visibility;
  private int comment_id;

  public Comment(String commenter_email, int project_id, String text, boolean visibility) {
    this.commenter_email = commenter_email;
    this.project_id = project_id;
    this.text = text;
    this.visibility = visibility;

    this.comment_id = -1;
    this.commenter_name = "";
  }

  public Comment(
      String commenter_email,
      String commenter_name,
      int project_id,
      String text,
      LocalDateTime dateTime,
      boolean visibility,
      int comment_id) {
    this.commenter_email = commenter_email;
    this.project_id = project_id;
    this.text = text;
    this.dateTime = dateTime;
    this.visibility = visibility;
    this.comment_id = comment_id;
    this.commenter_name = commenter_name;
  }

  public String getCommenter_email() {
    return commenter_email;
  }

  public int getProject_id() {
    return project_id;
  }

  public String getText() {
    return text;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public boolean getVisibility() {
    return visibility;
  }

  public int getComment_id() {
    return comment_id;
  }

  public String getCommenter_name() {
    return commenter_name;
  }
}
