package com.scanners.uscan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AIFragment extends Fragment implements ScanResultReceiver, View.OnClickListener{
    Button myButton;
    static View myView;
    static DatabaseReference mProductDatabase;
    static String searchText;
    static TextView product_name;
    private static final int CAMERA_REQUEST = 1888;
    FirebaseVisionImage image;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.ai_fragment, container, false);
        myButton = (Button) myView.findViewById(R.id.btn_scan_now);
        myButton.setOnClickListener(this);

        product_name = myView.findViewById(R.id.name_text);

        mProductDatabase = FirebaseDatabase.getInstance().getReference("Items");

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
                    int i = 0;
                    for (FirebaseVisionImageLabel label : labels) {
                        textArray.add(label.getText());
                        confidenceArray.add(label.getConfidence());
                        confidenceStrArray.add(Float.toString(confidenceArray.get(i)));
                        i++;
                    }
                    searchText = textArray.get(0);
                    setResult();

                }
            });
        }else{
            product_name.setText("Error No Photo Received....");
            product_name.setAllCaps(true);
            product_name.setTextSize(30);
        }
    }//End OnActivity result method

    public void setResult() {
        //Queries database
        mProductDatabase.orderByChild("name").equalTo(searchText).addChildEventListener(new ChildEventListener() {
            //Add onChildAdded method. gets entry for query and creates object to store information in. Using products object.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                products prod = dataSnapshot.getValue(products.class);
                product_name.setAllCaps(false);
                product_name.setTextSize(14);
                product_name.setText("Name: "+searchText);


            }//End onChildAdded

            //Unused methods for adding to database ect. Required for query to function.
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
            //End unused methods
        });//Ends query

        //If statement to remove information if query is unsuccessful
        if(product_name.getText().toString().matches("")){
            product_name.setText(searchText);
            product_name.setAllCaps(true);
            product_name.setTextSize(30);
        }//End if statement

    }//End setResult

}//End class
