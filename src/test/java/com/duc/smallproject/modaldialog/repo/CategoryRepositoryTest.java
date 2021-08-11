package com.duc.smallproject.modaldialog.repo;

import com.duc.smallproject.modaldialog.model.Categories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CategoryRepositoryTest {
    private final CategoryRepository repo;

    @Autowired
    CategoryRepositoryTest(CategoryRepository repo) {
        this.repo = repo;
    }

    List<Categories> categoriesList = asList(
            new Categories("Camera"),
            new Categories("TV"),
            new Categories("Phone")
    );

    @Test
    void insertDate() {
        categoriesList.forEach(repo::save);
    }

    @Test
    public void testCreateCategory() {
        var computers = new Categories("Electronic");
        var save = repo.save(computers);
        assertThat(save).isNotNull();
    }
    @Test
    public void testCreateSubcategory() {
        var parent = new Categories(7);
        Categories subCategories = new Categories("iPhone", parent);
        Categories savedCate = repo.save(subCategories);
        assertThat(savedCate.getId()).isGreaterThan(0);

    }
    @Test
    public void testCreateSubcategories() {
        var parent = new Categories(11);
        var desktop = new Categories("Webcam", parent);
        var component = new Categories("Canon", parent);
        repo.saveAll(List.of(desktop, component));
    }
    @Test
    public void testSubcategories() {
        var parent = new Categories(4);
        var desktop = new Categories("Laptop Gaming", parent);
        repo.saveAll(List.of(desktop));
    }

    @Test
    public void testGetChildren() {
        var categories = repo.findById(11).get();
        System.out.println(categories.getName());

        Set<Categories> children = categories.getChildren();

        children.stream()
                .map(Categories::getName)
                .forEach(System.out::println);
        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchy() {
        var all = repo.findAll();
        for (Categories category : all) {
            if (category.getParent() == null) {
                System.out.println(category.getName());
                for (Categories categories : category.getChildren()) {
                    System.out.println("--" + categories.getName());
                    this.recursive(categories, 1);
                }
            }
        }
    }

    private void recursive(Categories categories, int subLevel) {
        int newSubLevel = subLevel + 1;
        for (Categories category : categories.getChildren()) {
            for (int i = 0; i < subLevel; ++i) {
                System.out.print("--");
            }
            System.out.println(category.getName());
            recursive(category, newSubLevel);
        }
    }

    @Test
    public void testFindPageable() {
        var page = mock(Pageable.class);
        var pageSize = page.getPageSize();
        System.out.println(pageSize);
    }

    @Test
    public void testListRootCategories() {
        List<Categories> rootCategories = repo.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(System.out::println);
    }

    @Test
    void getAllCategories() {
        repo.findRootCategories(Sort.by("name").descending()).forEach(System.out::println);
        repo.findRootCategories(Sort.by("name").descending()).stream()
                .map(Categories::getChildren)
                .forEach(System.out::println);
    }

    @Test
    void lazyTest() {
        var byId = repo.findById(7);
        var cat = byId.get();
        Set<Categories> children = cat.getChildren();
        System.out.println("Children: ");
        for (Categories category : children) {
            System.out.println(category);
        }
    }

    @Test
    void testFindByName() {
        String name = "Computer";
        var computer = repo.findByName(name);
        assertThat(computer).isNotNull();
        System.out.println("This is test: ");
    }

    @Test
    void testFindByNameFail() {
        var chair = repo.findByName("Chair");
        assertThat(chair).isNull();
    }
}