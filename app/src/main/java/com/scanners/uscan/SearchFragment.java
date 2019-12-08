package com.scanners.uscan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchFragment extends Fragment {
    View myView;
    DatabaseReference mProductDatabase;
    RecyclerView mResultList;
    ImageButton mSearchBtn;
    EditText mSearchField;
    int i;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.search_fragment, container, false);

        mSearchBtn = (ImageButton) myView.findViewById(R.id.search_btn);
        mProductDatabase = FirebaseDatabase.getInstance().getReference("Products");
        mSearchField = myView.findViewById(R.id.search_field);
        mResultList = myView.findViewById(R.id.result_list);

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchField.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String searchText = mSearchField.getText().toString();
                    firebaseProductSearch(searchText);
                    return true;
                }
                return false;
            }
        });//End onKeyListener

        mSearchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String searchText = mSearchField.getText().toString();
                firebaseProductSearch(searchText);

            }
        });//End onClickListener

        return myView;

    }//End onCreateView

    private void firebaseProductSearch(String searchText) {
        //Add query to search database for entries that match users search
        Query firebaseSearchQuery = mProductDatabase.orderByChild("Name").startAt(searchText).endAt(searchText+"\uf8ff");
        //Create Recycler Adapter to hold all appropriate entries
        FirebaseRecyclerAdapter<products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<products, ProductViewHolder>(
                products.class,
                R.layout.list_layout,
                ProductViewHolder.class,
                firebaseSearchQuery
        ){
            //Create populateViewHolder to store database entries
            protected void populateViewHolder(ProductViewHolder holder, products model, int position) {
                i++;
                holder.setDetails(myView.getContext(), model.getName(), model.getPrice(), model.getRegion(), model.getLink(), model.getImage(), model.getDescription());
            }


        };//End recycler
        mResultList.setAdapter(adapter);
    }//End firebaseProductSearch

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        //Used to ensure correct view is used
        View mView;
        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }//End constructor
        //Create setDetails to add entry to results list. Displayed to user using list_layout.xml template
        public void setDetails(Context ctx, String Name, String Price, String Region, String Link, String Image, String Description) {
            TextView product_name = mView.findViewById(R.id.name_text);
            TextView product_price = mView.findViewById(R.id.price_text);
            TextView product_region = mView.findViewById(R.id.region_text);
            TextView product_link = mView.findViewById(R.id.link_text);
            TextView product_description = mView.findViewById(R.id.description_text);
            ImageView product_image = mView.findViewById(R.id.product_image);
            if(Price == null){
                Price = "Not Available";
            }
            if(Link == null){
                Link = "Not Available";
            }
            if(Description == null){
                Description ="Sorry. A description for this item is not currently available. We will remedy this issue as soon as possible. Please check back later!";
            }

            product_name.setText(Name);
            product_description.setText(Description);
            product_price.setText(Price);
            product_link.setText(Link);
            product_region.setText(Region);
            Glide.with(ctx).load(Image).into(product_image);
        }//End setDetails
    }//End productViewHolder

}
