package com.aztlansoft.namaka;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aztlansoft.namaka.model.Discount;
import com.aztlansoft.namaka.repository.DiscountRepository;

public class DiscountFragment extends Fragment
{
  public static final String ARG_DISCOUNT_ID = "discount_id";
  private Long discountId;
  private Discount discount = null;

  public DiscountFragment()
  {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View rootView = inflater.inflate(R.layout.fragment_discount, container, false);

    if (getArguments() != null)
    {
      discountId = getArguments().getLong(ARG_DISCOUNT_ID);
    }

    this.discount = DiscountRepository.getDiscountForId(getActivity().getApplicationContext(), this.discountId);

    if (this.discount != null)
    {
      TextView textViewTittle = (TextView) rootView.findViewById(R.id.textView);
      textViewTittle.setText(this.discount.getName());

      TextView textViewDetail = (TextView) rootView.findViewById(R.id.textViewDetail);
      textViewDetail.setText(this.discount.getDetail());

      TextView textViewDetailContact = (TextView) rootView.findViewById(R.id.textViewContact);
      textViewDetailContact.setText(this.discount.getContact());

      TextView textViewDetailPhone = (TextView) rootView.findViewById(R.id.textViewPhone);
      if (this.discount.getPhone() != null && !this.discount.getPhone().isEmpty())
      {
        textViewDetailPhone.setText("Tel: " + this.discount.getPhone());
      }
      else
      {
        textViewDetailPhone.setText(this.discount.getPhone());
      }

      TextView textViewDetailEmail = (TextView) rootView.findViewById(R.id.textViewEmail);
      textViewDetailEmail.setText(this.discount.getEmail());

      TextView textViewDetailDisclosure = (TextView) rootView.findViewById(R.id.textViewDisclosure);
      textViewDetailDisclosure.setText(this.discount.getDisclosure());

      TextView textViewDetailUrl = (TextView) rootView.findViewById(R.id.textViewUrl);
      textViewDetailUrl.setText(this.discount.getUrl());

      TextView textViewDetailAddress = (TextView) rootView.findViewById(R.id.textViewAddress);
      textViewDetailAddress.setText(this.discount.getAddress());
    }

    return rootView;
  }
}
