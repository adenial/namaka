package com.aztlansoft.namaka;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aztlansoft.namaka.model.Category;
import com.aztlansoft.namaka.repository.CategoryRepository;

import java.util.List;

public class MainActivity extends Activity
{
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private CharSequence mDrawerTitle;
  private CharSequence mTitle;
  private List<Category> categories;
/*  private String[] categoriesTitles;*/

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTitle = mDrawerTitle = getTitle();
    // categoriesTitles = getResources().getStringArray(R.array.categories_array);
    categories = CategoryRepository.getAllCategories(getApplicationContext());

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    // set up the drawer's list view with items and click listener
    mDrawerList.setAdapter(new ArrayAdapter<Category>(this, R.layout.drawer_list_item, categories));

    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    // enable ActionBar app icon to behave as action to toggle nav drawer
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close)
    {
      public void onDrawerClosed(View view)
      {
        getActionBar().setTitle(mTitle);

        // creates call to onPrepareOptionsMenu()
        invalidateOptionsMenu();
      }

      public void onDrawerOpened(View drawerView)
      {
        getActionBar().setTitle(mDrawerTitle);

        // creates call to onPrepareOptionsMenu()
        invalidateOptionsMenu();
      }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    if (savedInstanceState == null)
    {
      selectItem(0);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    // If the nav drawer is open, hide action items related to the content view
    boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    menu.findItem(R.id.action_search)
        .setVisible(!drawerOpen);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
    if (mDrawerToggle.onOptionsItemSelected(item))
    {
      return true;
    }

    // Handle action buttons
    switch (item.getItemId())
    {
      case R.id.action_search:
        // create intent to perform web search for this planet
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());

        // catch event that there's no activity to handle intent
        if (intent.resolveActivity(getPackageManager()) != null)
        {
          startActivity(intent);
        }
        else
        {
          Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG)
               .show();
        }

        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  /* The click listener for ListView in the navigation drawer */
  private class DrawerItemClickListener implements ListView.OnItemClickListener
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      selectItem(position);
    }
  }

  private void selectItem(int position)
  {
    Category selectedCategory = this.categories.get(position);

    // update the main content by replacing fragments
    Fragment fragment = new CategoryFragment();
    Bundle args = new Bundle();

    args.putLong(CategoryFragment.ARG_CATEGORY_ID, selectedCategory.getId());

    fragment.setArguments(args);
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction()
                   .replace(R.id.content_frame, fragment)
                   .commit();

    // update selected item and title, then close the drawer
    mDrawerList.setItemChecked(position, true);

    setTitle(categories.get(position).getName());
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  @Override
  public void setTitle(CharSequence title)
  {
    mTitle = title;
    getActionBar().setTitle(mTitle);
  }

  /**
   * When using the ActionBarDrawerToggle, you must call it during
   * onPostCreate() and onConfigurationChanged()...
   */

  @Override
  protected void onPostCreate(Bundle savedInstanceState)
  {
    super.onPostCreate(savedInstanceState);

    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);

    // Pass any configuration change to the drawer toggls
    mDrawerToggle.onConfigurationChanged(newConfig);
  }
}
