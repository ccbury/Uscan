package com.scanners.uscan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AIFragment extends Fragment implements ScanResultReceiver, View.OnClickListener{
    Button myButton;
    View myView;
    DatabaseReference mProductDatabase;
    String searchText;
    RecyclerView mResultList;
    private static final int CAMERA_REQUEST = 1888;
    FirebaseVisionImage image;
    int i;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.ai_fragment, container, false);
        myButton = (Button) myView.findViewById(R.id.btn_scan_now);
        myButton.setOnClickListener(this);

        mResultList = myView.findViewById(R.id.result_list);

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProductDatabase = FirebaseDatabase.getInstance().getReference("Products");

        return myView;

    }//End onCreateView

    public void scanNow(View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }//End scannow

    public void onClick(View v)  {
        scanNow(v);
    }//End onClick
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Bitmap photo2 = photo.copy(Bitmap.Config.ARGB_8888, true);
            image = FirebaseVisionImage.fromBitmap(photo2);

            FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler();

            labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                @Override
                public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                    ArrayList<String> textArray = new ArrayList<String>();
                    ArrayList<Float> confidenceArray = new ArrayList<Float>();
                    ArrayList<String> confidenceStrArray = new ArrayList<String>();
                    int ii = 0;
                    for (FirebaseVisionImageLabel label : labels) {
                        textArray.add(label.getText());
                        confidenceArray.add(label.getConfidence());
                        confidenceStrArray.add(Float.toString(confidenceArray.get(ii)));
                        ii++;
                    }
                    searchText = textArray.get(0);
                    setResult();

                }
            });
        }
    }//End OnActivity result method

    private void setResult() {

        //TestText.setText(searchText);
        //Queries database
        Query firebaseSearchQuery = mProductDatabase.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");
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
    }//End setResult
    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        //Used to ensure correct view is used
        View mView;
        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }//End constructor
        //Create setDetails to add entry to results list. Displayed to user using list_layout.xml template
        public void setDetails(Context ctx, String name, String price, String region, String link, String image, String description) {
            TextView product_name = mView.findViewById(R.id.name_text);
            TextView product_price = mView.findViewById(R.id.price_text);
            TextView product_region = mView.findViewById(R.id.region_text);
            TextView product_link = mView.findViewById(R.id.link_text);
            TextView product_description = mView.findViewById(R.id.description_text);
            ImageView product_image = mView.findViewById(R.id.product_image);
            if(price == null){
                price = "Not Available";
            }
            if(link == null){
                link = "Not Available";
            }
            if(description == null){
                description ="Sorry. A description for this item is not currently available. We will remedy this issue as soon as possible. Please check back later!";
            }
            product_name.setText(name);
            product_description.setText(description);
            product_price.setText(price);
            product_link.setText(link);
            product_region.setText(region);
            Glide.with(ctx).load(image).into(product_image);

        }//End setDetails
    }//End productViewHolder
}//End class
