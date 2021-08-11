package com.duc.smallproject.modaldialog.controller.categorycontroller;

import com.duc.smallproject.modaldialog.model.Categories;
import com.duc.smallproject.modaldialog.service.CategoryService;
import com.duc.smallproject.modaldialog.util.CategoryNotFondException;
import com.duc.smallproject.modaldialog.util.FileUploadUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String categoryManager(@Param("sortDir") String sortDir, Model model) {
//        return categoryList(1, "id", "asc", "", model);
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverse", reverseSortDir);
        model.addAttribute("categories",
                categoryService.listAll(sortDir));
        return "categories/categories";
    }

    @GetMapping("/categories/page/{page}")
    public String categoryList(@PathVariable("page") int pageNum,
                               @RequestParam String sortField,
                               @RequestParam String sortDir,
                               @RequestParam String keyword,
                               Model model) {
        var page = categoryService.listCategories(sortField, sortDir, pageNum - 1, keyword);
        model.addAttribute("categories", page.getContent());
        return "categories/categories";
    }


    @GetMapping("/categories/new")
    public String newCategories(Model model) {
        var categories = categoryService.listCategoriesUserInform();
        model.addAttribute("category", new Categories());
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@NonNull Categories category,
                               @RequestParam("fileImage") @NonNull MultipartFile file,
                               @NonNull RedirectAttributes attribute) {
        if (!file.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            category.setImage(filename);
            var saveCategory = categoryService.save(category);

            String uploadDir = "category-images/" + saveCategory.getId();
            FileUploadUtils.saveFile(uploadDir, filename, file);
        } else {
            categoryService.save(category);
        }

        attribute.addFlashAttribute("message", "Saving " + category.getName() + " Successfully");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    String getFormUpdate(@PathVariable(name = "id") Integer id,
                         Model model, RedirectAttributes redirectAttributes) {
        try {
            var cate = categoryService.findCate(id);
            var listCate = categoryService.listCategoriesUserInform();
            model.addAttribute("category", cate);
            model.addAttribute("categories", listCate);
            model.addAttribute("pageTitle", "Edit Category(" + id + ")");
            redirectAttributes.addFlashAttribute("message", "Edit Category(ID:" + id + ")");

            return "/categories/category_form";
        } catch (CategoryNotFondException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled")
    String updateEnabled(@PathVariable("id") @NonNull Integer id, RedirectAttributes attributes) {
        try {
            var cate = categoryService.findCate(id);
            categoryService.enabledCate(id);
            attributes.addFlashAttribute("message","success to update: " + id);
        } catch (CategoryNotFondException e) {
            attributes.addFlashAttribute("message", "No category with " + id + "is found");
        }
        return "redirect:/categories";
    }
    @GetMapping("/categories/delete/{id}")
    public String deleteCategories(@PathVariable(name = "id") Integer id,
                                   RedirectAttributes attributes) {
        try {
            categoryService.delete(id);
            String cateDir = "category-images/" + id;
            FileUploadUtils.remove(cateDir);
            attributes.addFlashAttribute("message", "The Categories Id: " + id + " has been deleted success");
        } catch (Exception e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }
}
