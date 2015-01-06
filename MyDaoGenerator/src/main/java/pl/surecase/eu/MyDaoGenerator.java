package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator
{

  public static void main(String args[]) throws Exception
  {
    Schema schema = new Schema(3, "com.aztlansoft.namaka.dao");

    Entity category = schema.addEntity("Category");
    category.setJavaPackage("com.aztlansoft.namaka.model");
    category.addIdProperty();
    category.addStringProperty("name");
    category.addStringProperty("description");


    Entity discount = schema.addEntity("Discount");
    discount.setJavaPackage("com.aztlansoft.namaka.model");
    discount.addIdProperty();
    discount.addStringProperty("name");
    discount.addStringProperty("detail");
    discount.addStringProperty("disclosure");
    discount.addStringProperty("url");
    discount.addStringProperty("contact");
    discount.addStringProperty("email");
    discount.addStringProperty("phone");
    discount.addStringProperty("address");

    // relationship, between Category and Discount To-Many.
    Property categoryId = discount.addLongProperty("categoryId")
                                  .notNull()
                                  .getProperty();
    ToMany categoryToDiscounts = category.addToMany(discount, categoryId);
    categoryToDiscounts.setName("discounts");

    new DaoGenerator().generateAll(schema, args[0]);
  }
}
