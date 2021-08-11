package com.duc.smallproject.modaldialog.service;

import com.duc.smallproject.modaldialog.model.Categories;
import com.duc.smallproject.modaldialog.repo.CategoryRepository;
import com.duc.smallproject.modaldialog.util.CategoryNotFondException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private static final int ELEMENTS_IN_PAGE = 15;
    private final CategoryRepository repo;

    public List<Categories> listAll(String sortDir) {
        var sort = Sort.by("name");
        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("des")) {
            sort = sort.descending();
        }
        var rootCategories = repo.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    private List<Categories> listHierarchicalCategories(List<Categories> root, String sortDir) {
        List<Categories> hierarchicalCategories = new ArrayList<>();
        for (Categories cat : root) {
            hierarchicalCategories.add(Categories.copyFull(cat));
            Set<Categories> children = sortSubCategories(cat.getChildren(), sortDir);
            for (Categories sub : children) {
                String name = "--" + sub.getName();
                hierarchicalCategories.add(Categories.copyFull(sub, name));

                listSubHierarchicalCategories(hierarchicalCategories, sub, 1, sortDir);

            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Categories> hierarchicalCategories,
                                               Categories parent, int sublevel, String sortDir) {
        Set<Categories> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = sublevel + 1;
        for (Categories sub : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += sub.getName();
            hierarchicalCategories.add(Categories.copyFull(sub, name));
            listSubHierarchicalCategories(hierarchicalCategories, sub, newSubLevel, sortDir);
        }
    }


    public Page<Categories> listCategories(String sortField, String sortDir,
                                           int page, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, ELEMENTS_IN_PAGE, sort);
        return repo.findAll(pageable, keyword);
    }

    public List<Categories> listCategoriesUserInform() {
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
                                              Categories sub, int subLevel) {
        int newSubLevel = subLevel + 1;
        var children = sortSubCategories(sub.getChildren());
        for (Categories cat : children) {
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

    public String checkUnique(Integer id, String name, String alias) {
        var cate = repo.findByName(name);
        boolean isCreateNew = id == null || id == 0;
        if (isCreateNew) {
            if (cate != null) {
                return "Duplicated Name";
            } else {
                var _alias = repo.findByAlias(alias);
                if (_alias != null) {
                    return "Duplicated Alias";
                }
            }
        } else {
            if (cate != null && !cate.getId().equals(id)) {
                return "Duplicated Name";
            }
            Categories categoriesByAlias = repo.findByAlias(alias);
            if (categoriesByAlias != null && !categoriesByAlias.getId().equals(id))
                return "Duplicated Alias";
        }
        return "OK";
    }

    private SortedSet<Categories> sortSubCategories(Set<Categories> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Categories> sortSubCategories(Set<Categories> children, String sortDir) {
        SortedSet<Categories> sortedSet = new TreeSet<>(new Comparator<Categories>() {
            @Override
            public int compare(Categories o1, Categories o2) {
                return sortDir.equals("asc")
                        ? o1.getName().compareTo(o2.getName())
                        : o2.getName().compareTo(o1.getName());
            }
        });
        sortedSet.addAll(children);
        return sortedSet;
    }

    @Transactional
    public void enabledCate(Integer id) throws CategoryNotFondException {
        var category = repo.findById(id);
        category.stream()
                .peek(this::enabled)
                .findAny().orElseThrow(CategoryNotFondException::new);
    }
    private void enabled(Categories cate) {
        var enabled = !cate.isEnabled();
        repo.updateEnabledStatus(cate.getId(), enabled);
    }

    public void delete(Integer id) throws CategoryNotFondException {
        Long countById = repo.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFondException("Could not find any categories with id: " + id);
        }
        repo.deleteById(id);
    }
}
