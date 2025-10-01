package org.deng.littledengserver.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddBabyVo {
    private String token;
    private BabyInfo babyInfo;

    @Data
    public static class BabyInfo {
        private Long id;
        private String childName;
        private String childGender;
        private String childNickname;
        private LocalDateTime childBirthday;
        private String avatarOriginal;
        private String avatarCropped;
        private String childContent;
    }
}
