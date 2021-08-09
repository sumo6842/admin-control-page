package com.duc.smallproject.modaldialog.service;

import com.duc.smallproject.modaldialog.model.Categories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {
    private final CategoryService service;

    @Autowired
    CategoryServiceTest(CategoryService service) {
        this.service = service;
    }

    @Test
    public void testGetListCategories() {
        var page = service.listCategories("id", "desc", 0, "");
        page.getContent().forEach(System.out::println);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
    }
    @Test
    public void testFindPageable() {
        var page = service
                .listCategories("id", "asc", 1, "ar");
        page.getContent().forEach(System.out::println);
    }

    @Test
    void listAll() {
        var categories = service.listAll();
        categories.forEach(System.out::println);
    }
}