package com.duc.smallproject.modaldialog.service;

import com.duc.smallproject.modaldialog.model.Categories;
import com.duc.smallproject.modaldialog.repo.CategoryRepository;
import com.duc.smallproject.modaldialog.util.CategoryNotFondException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CategoryService {
    private final CategoryRepository repo;
    private int ELEMENTS_IN_PAGE = 10;

    @Autowired
    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Categories> listAll() {
        var rootCategories = repo.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }
    private List<Categories> listHierarchicalCategories(List<Categories> root) {
        List<Categories> hierarchicalCategories = new ArrayList<>();
        for (Categories cat : root) {
            hierarchicalCategories.add(Categories.copyFull(cat));
            Set<Categories> children = cat.getChildren();
            for (Categories sub : children) {
                String name = "--" + sub.getName();
                hierarchicalCategories.add(Categories.copyFull(sub, name));

                listSubHierarchicalCategories(hierarchicalCategories,sub,1);

            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Categories> hierarchicalCategories, Categories parent, int sublevel) {
        Set<Categories> children = parent.getChildren();
        int newSubLevel = sublevel + 1;
        for (Categories sub : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += sub.getName();
            hierarchicalCategories.add(Categories.copyFull(sub, name));
            listSubHierarchicalCategories(hierarchicalCategories, sub, newSubLevel);
        }
    }


    public Page<Categories> listCategories(String sortField, String sortDir,
                                           int page, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, ELEMENTS_IN_PAGE, sort);
        return repo.findAll(pageable, keyword);
    }

    public List<Categories> listCategoriesUserInform(){
        var categoriesUserInform = new ArrayList<Categories>();
        var all = repo.findAll();
        for (Categories cat : all) {
            if (cat.getParent() == null) {
                categoriesUserInform.add(Categories.copyIdAndName(cat));
                var children = cat.getChildren();
                for (Categories sub : children) {
                    String name = "--" + sub.getName();
                    categoriesUserInform.add(Categories.copyIdAndName(sub.getId(), sub.getName()));
                    listSubCategorisedUsedInForm(categoriesUserInform, sub, 1);
                }
            }
        }
        return categoriesUserInform;
    }

    private void listSubCategorisedUsedInForm(List<Categories> categoriesUserInform,
                                              @NotNull Categories sub, int subLevel) {
        int newSubLevel = subLevel + 1;
        for (Categories cat : sub.getChildren()) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += cat.getName();
            categoriesUserInform.add(Categories.copyIdAndName(sub.getId(), name));
            listSubCategorisedUsedInForm(categoriesUserInform, cat, newSubLevel);
        }
    }

    public Categories save(Categories category) {
        return repo.save(category);
    }


    public Categories findCate(Integer id) throws CategoryNotFondException {
            return repo.findById(id)
                    .orElseThrow(() -> new CategoryNotFondException(
                            "Could not find any category with Id: " + id));
    }
}
