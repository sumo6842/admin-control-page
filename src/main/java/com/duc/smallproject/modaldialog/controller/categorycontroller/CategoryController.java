package com.duc.smallproject.modaldialog.controller.categorycontroller;

import com.duc.smallproject.modaldialog.model.Categories;
import com.duc.smallproject.modaldialog.service.CategoryService;
import com.duc.smallproject.modaldialog.util.CategoryNotFondException;
import com.duc.smallproject.modaldialog.util.FileUploadUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String categoryManager(Model model) {
//        return categoryList(1, "id", "asc", "", model);
        model.addAttribute("categories", categoryService.listAll());
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


}
