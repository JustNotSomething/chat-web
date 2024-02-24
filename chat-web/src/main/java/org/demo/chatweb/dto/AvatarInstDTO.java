package org.demo.chatweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.io.ByteArrayResource;

public class AvatarInstDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("avatar")
    private byte[] avatarData;
    public AvatarInstDTO(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(byte[] avatarData) {
        this.avatarData = avatarData;
    }
}
