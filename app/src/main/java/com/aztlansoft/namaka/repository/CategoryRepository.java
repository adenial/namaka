package com.aztlansoft.namaka.repository;

import android.content.Context;

import com.aztlansoft.namaka.Startup;
import com.aztlansoft.namaka.dao.CategoryDao;
import com.aztlansoft.namaka.model.Category;

import java.util.List;

public class CategoryRepository
{
  public static void insertOrUpdate(Context context, Category category)
  {
    getCategoryDao(context).insertOrReplace(category);
  }

  public static void clearCategories(Context context)
  {
    getCategoryDao(context).deleteAll();
  }

  public static void deleteCategoryWithId(Context context, long id)
  {
    getCategoryDao(context).delete(getCategoryForId(context, id));
  }

  public static List<Category> getAllCategories(Context context)
  {
    return getCategoryDao(context).loadAll();
  }

  public static Category getCategoryForId(Context context, long id)
  {
    return getCategoryDao(context).load(id);
  }

  private static CategoryDao getCategoryDao(Context c)
  {
    return ((Startup) c.getApplicationContext()).getDaoSession()
                                                .getCategoryDao();
  }
}
