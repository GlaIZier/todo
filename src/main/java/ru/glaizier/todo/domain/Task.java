package ru.glaizier.todo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
// Todo migrate to lombok
public class Task {

    private int id;

    private String todo;

    @JsonProperty("_link")
    private String link;

    public Task() {
    }

    public Task(int id, String todo, String link) {
        this.id = id;
        this.todo = todo;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
