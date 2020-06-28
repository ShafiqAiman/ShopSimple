package com.ruth.checkmeout.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ruth.checkmeout.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;


public class ScanningFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private CameraSource cameraSource;
    @BindView(R.id.txtBarcodeValue)
    TextView txtBarcodeValue;
    @BindView(R.id.btnGoToCart)
    FloatingActionButton btnGoToCart;
    @BindView(R.id.btnAddItem) FloatingActionButton btnAddItem;
    private Class fragmentClass;
    private Fragment fragment = null;
    private String intentData="";
    private ArrayList<String> itemCodes=new ArrayList<String>();
    private boolean firstDetected=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_scaning, container, false);
        ButterKnife.bind(this,view);
        Snackbar.make(view, "Click on + to add items", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
        Snackbar.make(view, "Click on the basket to checkout", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
        btnGoToCart.setOnClickListener(this);
        btnAddItem.setOnClickListener(this);
        initialiseDetectorsAndSources();

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v==btnGoToCart){
            Log.i(TAG, "onClick: " + itemCodes.size());
            Bundle bundle=new Bundle();
            bundle.putStringArrayList("codes",itemCodes);

            fragmentClass=ShopFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(bundle);

            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getFragmentManager();
            assert fragmentManager != null;
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }
        if(v==btnAddItem){
            firstDetected=true;
            initialiseDetectorsAndSources();

        }

    }
    private void initialiseDetectorsAndSources() {

        Toast.makeText(getContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                        Log.i(TAG, "surfaceCreated: bloop");
                        Toast.makeText(getContext(), "Surface created", Toast.LENGTH_SHORT).show();

                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i(TAG, "surfaceCreated: blipp");
                Toast.makeText(getContext(), "Surface Changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
//                Log.i(TAG, "surfaceCreated: bleep");
                Toast.makeText(getContext(), "Surface  Destroyed", Toast.LENGTH_SHORT).show();
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Log.i(TAG, "surfaceCreated: bluup");
                //Toast.makeText(getContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                //Toast.makeText(getContext(), barcodes.valueAt(0).rawValue,Toast.LENGTH_SHORT).show();


                if (barcodes.size() != 0 && firstDetected) {
                    //barcodeDetector.release();
                    firstDetected=false;
                    Log.i(TAG, "surfaceCreated:"+barcodes.size());
//                    Toast.makeText(getContext(), barcodes.valueAt(0).rawValue,Toast.LENGTH_LONG).show();


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {


                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;

                                txtBarcodeValue.setText(intentData);

                                //isEmail = true;
                                //btnAction.setText("ADD CONTENT TO THE MAIL");
                                cameraSource.stop();

                            } else {
                                //isEmail = false;
                                //btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                itemCodes.add(intentData);
                                txtBarcodeValue.setText(intentData);
                            }
                        }
                    });

                }


            }
        });
    }
}
