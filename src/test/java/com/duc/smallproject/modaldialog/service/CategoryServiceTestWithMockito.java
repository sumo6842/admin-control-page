package com.duc.smallproject.modaldialog.service;

import com.duc.smallproject.modaldialog.model.Categories;
import com.duc.smallproject.modaldialog.repo.CategoryRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CategoryServiceTestWithMockito {
    CategoryRepository repo = Mockito.mock(CategoryRepository.class);
    CategoryService service;

    @BeforeEach
    void setUp() {
        service = new CategoryService(repo);
    }

    @Test
    void testCheckUniqueInNewModelReturnDuplicated() {
        Integer id = null;
        String name = "Computer";
        String alias = "abc";
        var categories = new Categories(id, name, alias);
        when(repo.findByName(name)).thenReturn(categories);
        when(repo.findByAlias(alias)).thenReturn(categories);

        String result = service.checkUnique(id, name, alias);

        System.out.println("This is test");
        System.out.println(result);
    }

    @Test
    void testCheckUniqueInEditedModeReturnDuplicateName() {
        Integer id = 1;
        String name = "Computer";
        String alias = "abc";
        var categories = new Categories(2, name, alias);
        when(repo.findByName(name)).thenReturn(categories);
        when(repo.findByAlias(alias)).thenReturn(null);

        String result = service.checkUnique(id, name, alias);

        System.out.println("This is test");
        System.out.println(result);
    }
    @Test
    void testCheckUniqueInEditedModeReturnDUplicateAlias() {
        Integer id = 1;
        String name = "Computer";
        String alias = "computer";
        var categories = new Categories(2, name, alias);
        when(repo.findByName(name)).thenReturn(null);
        when(repo.findByAlias(alias)).thenReturn(categories);

        String result = service.checkUnique(id, name, alias);

        System.out.println("This is test");
        System.out.println(result);
    }
}