package com.duc.smallproject.modaldialog.model;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "category")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", length = 128, nullable = false, unique = true)
    private String name;
    @Column(name = "alia", length = 64, nullable = false, unique = true)
    private String alias;

    @Column(name = "image", length = 128, nullable = false)
    private String image;
    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Categories parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<Categories> children = new HashSet<>();


    public Categories() {
    }

    public Categories(Integer id) {
        this.id = id;
    }

    public Categories(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public Categories(String name, Categories parent) {
        this(name);
        this.parent = parent;
    }

    public Categories(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Transient
    public boolean hasChildren;

    @Transient
    public String getImagePath() {
        if (id == null) {
            return "/images/image-thumbnail.png";
        }
        return "/category-images/" + this.id + "/" + this.image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Categories getParent() {
        return parent;
    }

    public void setParent(Categories parent) {
        this.parent = parent;
    }

    public Set<Categories> getChildren() {
        return children;
    }

    public void setChildren(Set<Categories> children) {
        this.children = children;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public static Categories copyIdAndName(Categories categories) {
        Categories copyCategory = new Categories();
        copyCategory.setId(categories.id);
        copyCategory.setName(categories.name);
        return copyCategory;
    }

    public static Categories copyIdAndName(Integer id, String name) {
        Categories copyCategory = new Categories();
        copyCategory.setId(id);
        copyCategory.setName(name);
        return copyCategory;
    }

    public static Categories copyFull(Categories category) {

        Categories copyCategory = new Categories();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        copyCategory.setImage(category.image);
        copyCategory.setAlias(category.getAlias());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setHasChildren(category.getChildren().size() > 0);
        return copyCategory;
    }
    public static Categories copyFull(Categories cate, String name) {
        var copyCate = Categories.copyFull(cate);
        copyCate.setName(name);
        return copyCate;
    }

    @Override
    public String toString() {
        return "[id: " + id + " | name: " + name + "]";
    }
}
