package org.demo.chatweb.dto;

import java.util.List;

public class SectionDTO {
    private String title;
    private List<String> users;

    public SectionDTO(){}
    public SectionDTO(String title, List<String> users) {
        this.title = title;
        this.users = users;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "SectionDTO{" +
                "title='" + title + '\'' +
                ", users=" + users +
                '}';
    }
}
