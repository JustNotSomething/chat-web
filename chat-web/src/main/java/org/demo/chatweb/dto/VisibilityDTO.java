package org.demo.chatweb.dto;

public class VisibilityDTO {
    private Boolean isVisible;
    public VisibilityDTO(){}

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    @Override
    public String toString() {
        return "VisibilityDTO{" +
                "isVisible=" + isVisible +
                '}';
    }
}
