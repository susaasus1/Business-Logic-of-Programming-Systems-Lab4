package com.example.main_service.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class UpdateRecipeRequest {

    @NotNull(message = "Укажите блюдо!")
    @Size(min = 1, max = 255, message = "Укажите название блюда! От 1 до 255 символов")
    private String dishName;

    @NotNull(message = "Укажите описание рецепта!")
    @Size(min = 1, max = 4096, message = "Укажите описание рецепта! От 1 до 4096 символов")
    private String description;
    @NotNull(message = "Укажите блюдо!")
    @Min(value = 1, message = "Количество порций не может быть меньше 1!")
    @Max(value = 100, message = "Предел порций - 100")
    private Integer countPortion;

    @NotNull(message = "Укажите национальную кухню!")
    @Size(min = 1, max = 64, message = "Укажите национальную кухню! От 1 до 64 символов")
    private String nationalCuisineName;

    @NotNull(message = "Укажите вкусы!")
    private List<String> tastesNames;

    @NotNull(message = "Укажите ингредиенты!")
    private List<String> ingredientsNames;


    public UpdateRecipeRequest(String dishName, String description,
                               Integer countPortion,
                               String nationalCuisineName, List<String> tastesNames,
                               List<String> ingredientsNames) {

        this.dishName = dishName;
        this.description = description;
        this.countPortion = countPortion;
        this.nationalCuisineName = nationalCuisineName;
        this.tastesNames = tastesNames;
        this.ingredientsNames = ingredientsNames;
    }
}
