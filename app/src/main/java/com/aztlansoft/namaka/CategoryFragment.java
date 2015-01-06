package com.aztlansoft.namaka;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.aztlansoft.namaka.model.Category;
import com.aztlansoft.namaka.model.Discount;
import com.aztlansoft.namaka.repository.CategoryRepository;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class CategoryFragment extends Fragment implements AbsListView.OnItemClickListener
{
  public static final String ARG_CATEGORY_ID = "category_id";
  private Long categoryId;
  private OnFragmentInteractionListener mListener;
  private Category category;
  /**
   * The fragment's ListView/GridView.
   */
  private AbsListView mListView;

  /**
   * The Adapter which will be used to populate the ListView/GridView with
   * Views.
   */
  private ListAdapter mAdapter;

  // TODO: Rename and change types of parameters
  public static CategoryFragment newInstance(String param1)
  {
    CategoryFragment fragment = new CategoryFragment();
    Bundle args = new Bundle();
    args.putString(ARG_CATEGORY_ID, param1);

    fragment.setArguments(args);
    return fragment;
  }

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public CategoryFragment()
  {
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    if (getArguments() != null)
    {
      categoryId = getArguments().getLong(ARG_CATEGORY_ID);
    }

    this.category = CategoryRepository.getCategoryForId(getActivity().getApplicationContext(), this.categoryId);

    if(this.category.getDiscounts().isEmpty())
    {
    }
    else
    {
      mAdapter = new ArrayAdapter<Discount>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, this.category.getDiscounts());
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_category, container, false);

    // Set the adapter
    mListView = (AbsListView) view.findViewById(android.R.id.list);
    ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

    // Set OnItemClickListener so we can be notified on item clicks
    mListView.setOnItemClickListener(this);

    return view;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id)
  {
    selectItem(position);
  }

  private void selectItem(int position)
  {
    Discount selectedDiscount = this.category.getDiscounts().get(position);

    // update the main content by replacing fragments
    Fragment discountfragment = new DiscountFragment();
    Bundle args = new Bundle();

    args.putLong(DiscountFragment.ARG_DISCOUNT_ID, selectedDiscount.getId());

    discountfragment.setArguments(args);
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction()
                   .replace(R.id.content_frame, discountfragment)
                   .addToBackStack(null)
                   .commit();

    // update selected item and title, then close the drawer
    //mDrawerList.setItemChecked(position, true);

    //setTitle(categories.get(position).getName());
    //mDrawerLayout.closeDrawer(mDrawerList);
  }

  /**
   * The default content for this Fragment has a TextView that is shown when
   * the list is empty. If you would like to change the text, call this method
   * to supply the text it should use.
   */
  public void setEmptyText(CharSequence emptyText)
  {
    View emptyView = mListView.getEmptyView();

    if (emptyView instanceof TextView)
    {
      ((TextView) emptyView).setText(emptyText);
    }
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener
  {
    // TODO: Update argument type and name
    public void onFragmentInteraction(String id);
  }
}
