package ru.eleventh.fakenews.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 3, message = "Too short title")
    private String title;

    @NotEmpty(message = "Text should not be empty")
    private String text;

    private String rubric;

    public News() { }

    public News(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRubric(String rubric) {
        this.rubric = rubric;
    }

    public String getRubric() {
        return rubric;
    }
}
