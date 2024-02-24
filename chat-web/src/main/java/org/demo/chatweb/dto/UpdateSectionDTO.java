package org.demo.chatweb.dto;

import java.util.List;

public class UpdateSectionDTO {
    private String prevTitle;
    private String newTitle;
    private List<String> usersToDelete;
    private List<String> usersToAdd;

    public UpdateSectionDTO(){}

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public List<String> getUsersToDelete() {
        return usersToDelete;
    }

    public void setUsersToDelete(List<String> usersToDelete) {
        this.usersToDelete = usersToDelete;
    }

    public List<String> getUsersToAdd() {
        return usersToAdd;
    }

    public void setUsersToAdd(List<String> usersToAdd) {
        this.usersToAdd = usersToAdd;
    }

    public String getPrevTitle() {
        return prevTitle;
    }

    public void setPrevTitle(String prevTitle) {
        this.prevTitle = prevTitle;
    }

    @Override
    public String toString() {
        return "UpdateSectionDTO{" +
                "prevTitle='" + prevTitle + '\'' +
                ", newTitle='" + newTitle + '\'' +
                ", usersToDelete=" + usersToDelete +
                ", usersToAdd=" + usersToAdd +
                '}';
    }
}
