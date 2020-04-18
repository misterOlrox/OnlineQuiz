package com.olrox.quiz.dto;

import com.olrox.quiz.entity.QuizQuestionTheme;
import lombok.Data;

@Data
public class ThemeDto {
    private long id;
    private String themeName;

    private ThemeDto(long id, String themeName) {
        this.id = id;
        this.themeName = themeName;
    }

    public static ThemeDto from(QuizQuestionTheme theme) {
        return new ThemeDto(theme.getId(), theme.getThemeName());
    }
}
