package com.aztlansoft.namaka;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.aztlansoft.namaka.dao.DaoMaster;
import com.aztlansoft.namaka.dao.DaoSession;
import com.aztlansoft.namaka.model.Category;
import com.aztlansoft.namaka.model.Discount;
import com.aztlansoft.namaka.repository.CategoryRepository;
import com.aztlansoft.namaka.repository.DiscountRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Startup extends Application
{
  public DaoSession daoSession;

  @Override
  public void onCreate()
  {
    super.onCreate();

    boolean dbExist = doesDatabaseExist(getApplicationContext(), "namaka-db");
    setupDatabase();

    if(!dbExist)
    {

      installCategories();
      installDiscounts();
    }
  }

  private boolean doesDatabaseExist(Context context, String dbName)
  {
    File dbFile = context.getDatabasePath(dbName);
    return dbFile.exists();
  }

  private void installDiscounts()
  {
    ArrayList<List<Discount>> arrayOfDiscounts = getArrayOfDiscounts();

    for(List<Discount> discountArray : arrayOfDiscounts)
    {
      for(Discount discount : discountArray)
      {
        DiscountRepository.insertOrUpdate(getApplicationContext(), discount);
      }
    }
  }

  private ArrayList<List<Discount>> getArrayOfDiscounts()
  {
    ArrayList<List<Discount>> arrayOfDiscounts = new ArrayList<>();

    List<Discount> aerolineDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountsaerolines), "discountsAerolines");
    List<Discount> travelAgenciesDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountstravelagencies), "discountsTravelAgencies");
    List<Discount> homeArticlesDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountshomearticles), "discountsHomeArticles");
    List<Discount> partyArticlesDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountspartyarticles), "discountsPartyArticles");
    List<Discount> insuranceCompanyDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountsinsurancecompanies), "discountsInsuranceCompanies");
    List<Discount> automativeEngineeringDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountsautomotiveengineering), "discountsAutomativeEngineering");
    List<Discount> gymsDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountsgyms), "discountsGyms");
    List<Discount> educationDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountseducation), "discountsEducation");
    List<Discount> entertainmentDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountsentertainment), "discountsEntertainment");
    List<Discount> beautyDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountsbeauty), "discountsBeauty");
    List<Discount> flowerShopsDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountsflowershops), "discountsFlowerShops");
    List<Discount> funeralHomesShopsDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountsfuneralhomes), "discountsFuneralHomes");
    List<Discount> hotelsDiscounts = getDiscounts(getApplicationContext().getResources().openRawResource(R.raw.discountshotels), "discountsHotels");

    arrayOfDiscounts.add(aerolineDiscounts);
    arrayOfDiscounts.add(travelAgenciesDiscounts);
    arrayOfDiscounts.add(homeArticlesDiscounts);
    arrayOfDiscounts.add(partyArticlesDiscounts);
    arrayOfDiscounts.add(insuranceCompanyDiscounts);
    arrayOfDiscounts.add(automativeEngineeringDiscounts);
    arrayOfDiscounts.add(gymsDiscounts);
    arrayOfDiscounts.add(educationDiscounts);
    arrayOfDiscounts.add(entertainmentDiscounts);
    arrayOfDiscounts.add(beautyDiscounts);
    arrayOfDiscounts.add(flowerShopsDiscounts);
    arrayOfDiscounts.add(funeralHomesShopsDiscounts);
    arrayOfDiscounts.add(hotelsDiscounts);
    return arrayOfDiscounts;
  }

  private List<Discount> getDiscounts(InputStream inputStream, String discountCategory)
  {
    JSONObject jsonObject;
    JSONArray jsonDiscounts;
    JSONArray jsonDiscountConditions;
    List<Discount> discounts = new ArrayList<>();
    try
    {
      jsonObject = new JSONObject(convertStreamToString(inputStream));
      jsonDiscounts = jsonObject.getJSONArray(discountCategory);


      for (int i = 0, m = jsonDiscounts.length(); i < m; i++)
      {
        StringBuilder sb = new StringBuilder();
        JSONObject jsonDiscount = jsonDiscounts.getJSONObject(i);
        jsonDiscountConditions = jsonDiscount.getJSONArray("conditions");

        Discount discount = new Discount();
        discount.setName(jsonDiscount.getString("discount"));
        discount.setCategoryId(jsonDiscount.getLong("categoryId"));
        discount.setContact(jsonDiscount.getString("contact"));
        discount.setEmail(jsonDiscount.getString("email"));
        discount.setDisclosure(jsonDiscount.getString("disclosure"));
        discount.setPhone(jsonDiscount.getString("phone"));
        discount.setUrl(jsonDiscount.getString("url"));
        discount.setAddress(jsonDiscount.getString("address"));

        for(int x = 0, y = jsonDiscountConditions.length(); x < y; x++)
        {
          String jsonDiscountDetail = jsonDiscountConditions.getString(x);
          sb.append(jsonDiscountDetail + "\n\r\r\n");
        }

        discount.setDetail(sb.toString());
        discounts.add(discount);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }

    return discounts;
  }

  private void installCategories()
  {
    List<Category> categories = CategoryRepository.getAllCategories(getApplicationContext());

    if (categories.isEmpty())
    {
      // install the categories.
      categories = getDefaultCategories();

      for (Category category : categories)
      {
        CategoryRepository.insertOrUpdate(getApplicationContext(), category);
      }
    }
  }

  private List<Category> getDefaultCategories()
  {
    InputStream jsonStream = getApplicationContext().getResources()
                                                    .openRawResource(R.raw.categories);
    JSONObject jsonObject;
    JSONArray jsonCategories;
    List<Category> categories = new ArrayList<>();
    try
    {
      jsonObject = new JSONObject(convertStreamToString(jsonStream));
      jsonCategories = jsonObject.getJSONArray("categories");

      for (int i = 0, m = jsonCategories.length(); i < m; i++)
      {
        JSONObject jsonCategory = jsonCategories.getJSONObject(i);
        Category category = new Category();
        category.setName(jsonCategory.getString("category"));
        categories.add(category);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }

    return categories;
  }

  private String convertStreamToString(InputStream jsonStream) throws IOException
  {
    if (jsonStream != null)
    {
      Writer writer = new StringWriter();

      char[] buffer = new char[1024];
      try
      {
        Reader reader = new BufferedReader(
            new InputStreamReader(jsonStream, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1)
        {
          writer.write(buffer, 0, n);
        }
      }
      finally
      {
        jsonStream.close();
      }
      return writer.toString();
    }
    else
    {
      return "";
    }
  }

  private void setupDatabase()
  {
    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "namaka-db", null);
    SQLiteDatabase db = helper.getWritableDatabase();
    DaoMaster daoMaster = new DaoMaster(db);
    daoSession = daoMaster.newSession();
  }

  public DaoSession getDaoSession()
  {
    return daoSession;
  }
}
