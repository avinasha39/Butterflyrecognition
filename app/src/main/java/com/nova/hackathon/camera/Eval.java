package com.nova.hackathon.camera;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class Eval extends AppCompatActivity {
    private static final String MODEL_FILE = "file:///android_asset/freeze9_3.pb";
    private static final String INPUT_NODE = "prefix/fifo_queue_Dequeue:0";
    private static final String OUTPUT_NODE = "prefix/InceptionV3/Predictions/Softmax:0";
    int imageMean;
    float imageStd;
    private int[] intValues;
    private float[][] floatValues;
    float[] resu;
    TextView text;
    public static String st;
    private static final int[] INPUT_SIZE = {7, 299, 299, 3};
    private TensorFlowInferenceInterface inferenceInterface;

    static {
        System.loadLibrary("tensorflow_inference");
    }


    public float[] recognizeImage(final Bitmap bitmap) {
        // Log this method so that it can be analyzed with systrace.

        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
        int count=0;
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        System.out.print("1");
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[0][i * 3 + 0] = (((val >> 16) & 0xFF) - imageMean) / imageStd;
            floatValues[0][i * 3 + 1] = (((val >> 8) & 0xFF) - imageMean) / imageStd;
            floatValues[0][i * 3 + 2] = ((val & 0xFF) - imageMean) / imageStd;
            if((i==((intValues.length)-1))&&(count<7)){
                i=0;count++;
            }
        }
        System.out.print("2");

        return floatValues[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.print("3");

        inferenceInterface = new TensorFlowInferenceInterface();
        inferenceInterface.initializeTensorFlow(getAssets(), MODEL_FILE);
        System.out.print("4");


        Bitmap bmp = BitmapFactory.decodeFile("file:///android_asset/hopper.jpg");
        float[] inputFloats = recognizeImage(bmp);
        System.out.print("5");

        inferenceInterface.fillNodeFloat(INPUT_NODE, INPUT_SIZE, inputFloats);
        System.out.print("6");

        // Run the inference call.
        inferenceInterface.runInference(new String[]{OUTPUT_NODE});
        System.out.print("7");


        // Copy the output Tensor back into the output array.
        inferenceInterface.readNodeFloat(OUTPUT_NODE, resu);
        System.out.print("9");

        //final TextView textViewR = (TextView) findViewById(R.id.txtViewResult);
        //textViewR.setText(Float.toString(resu[0]) + ", " + Float.toString(resu[1]));


    }


}