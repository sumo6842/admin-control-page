package com.duc.smallproject.modaldialog.controller;

import com.duc.smallproject.modaldialog.model.Categories;
import com.duc.smallproject.modaldialog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CategoriesRestController {
    CategoryService service;

    @PostMapping("/categories/check_unique")
    public String checkUnique(@Param("id") Integer id,
                              @Param("name") String name,
                              @Param("alias") String alias) {
        return service.checkUnique(id, name, alias);
    }

}
