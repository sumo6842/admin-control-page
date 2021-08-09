package com.duc.smallproject.modaldialog.repo;

import com.duc.smallproject.modaldialog.model.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Categories, Integer> {
    @Query("SELECT CATE FROM Categories AS CATE WHERE CONCAT(CATE.id, ' '," +
            "CATE.name,' ', CATE.alias) LIKE %?1%")
    Page<Categories> findAll(Pageable page, String keyword);

    @Query("SELECT c FROM Categories c WHERE c.parent.id is NULL")
    List<Categories> findRootCategories();

}
