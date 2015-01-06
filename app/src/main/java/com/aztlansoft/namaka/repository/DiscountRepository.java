package com.aztlansoft.namaka.repository;

import android.content.Context;

import com.aztlansoft.namaka.Startup;
import com.aztlansoft.namaka.dao.DiscountDao;
import com.aztlansoft.namaka.model.Discount;

import java.util.List;

public class DiscountRepository
{
  public static void insertOrUpdate(Context context, Discount discount)
  {
    getDiscountDao(context).insertOrReplace(discount);
  }

  public static void clearDiscounts(Context context)
  {
    getDiscountDao(context).deleteAll();
  }

  public static void deleteDiscountWithId(Context context, long id)
  {
    getDiscountDao(context).delete(getDiscountForId(context, id));
  }

  public static List<Discount> getAllDiscounts(Context context)
  {
    return getDiscountDao(context).loadAll();
  }

  public static Discount getDiscountForId(Context context, long id)
  {
    return getDiscountDao(context).load(id);
  }

  private static DiscountDao getDiscountDao(Context c)
  {
    return ((Startup) c.getApplicationContext()).getDaoSession()
                                                .getDiscountDao();
  }
}
