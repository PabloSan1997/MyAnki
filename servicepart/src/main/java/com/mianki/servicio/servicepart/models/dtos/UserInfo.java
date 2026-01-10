package com.mianki.servicio.servicepart.models.dtos;

import com.mianki.servicio.servicepart.models.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    private String username;
    private String nickname;
    public UserInfo(UserEntity user){
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
