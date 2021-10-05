package com.dsc.fptublog.service.implementations;

import com.dsc.fptublog.dao.interfaces.ICategoryDAO;
import com.dsc.fptublog.database.ConnectionWrapper;
import com.dsc.fptublog.entity.CategoryEntity;
import com.dsc.fptublog.service.interfaces.ICategoryService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

@Service
public class ImplCategoryService implements ICategoryService {

    @Inject
    private ConnectionWrapper connectionWrapper;

    @Inject
    private ICategoryDAO categoryDAO;

    @Override
    public CategoryEntity getCategory(String id) throws SQLException {
        CategoryEntity category;

        try {
            connectionWrapper.beginTransaction();

            category = categoryDAO.getById(id);

            connectionWrapper.commit();
        } finally {
            connectionWrapper.close();
        }

        return category;
    }

    @Override
    public List<CategoryEntity> getCategories() throws SQLException {
        List<CategoryEntity> categoryList;

        try {
            connectionWrapper.beginTransaction();

            categoryList = categoryDAO.getAll();

            connectionWrapper.commit();
        } finally {
            connectionWrapper.close();
        }

        return categoryList;
    }
}