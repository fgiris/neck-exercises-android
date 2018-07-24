package com.ceng.muhendis.muneckexercises;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by muhendis on 18.01.2018.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    private static final String TAG = "DownloadImageTask";
    int id;


    public DownloadImageTask(ImageView bmImage,int id) {
        this.bmImage = bmImage;
        this.id = id;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            Log.d(TAG,"Downloading image...");
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        Log.d(TAG,"Downloading finished...");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if(result==null)
            Log.d(TAG,"RESULT IS NULL: ");
        else
        {
            result.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            final int maxSize = 960;
            int outWidth;
            int outHeight;
            int inWidth = result.getWidth();
            int inHeight = result.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(result, outWidth, outHeight, false);



            bmImage.setImageBitmap(resizedBitmap);

            new File(Environment.getExternalStorageDirectory()
                    + File.separator + "marmaraegzersiz/images").mkdirs();
            //you can create a new file name "test.jpg" in sdcard folder.
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "marmaraegzersiz/images"+File.separator+id+".jpg");

            Log.d(TAG,"DIRECTORY: "+Environment.getExternalStorageDirectory()
                    + File.separator + "marmaraegzersiz"+File.separator+(id+1)+".jpg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                Log.d(TAG,"FILE CANNOT BE CREATED ERROR: "+e.getLocalizedMessage());
                e.printStackTrace();
            }
            //write the bytes in file
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(f);
                //ObjectOutputStream os = new ObjectOutputStream(fo);
                //writeObject(os,result);
                fo.write(bytes.toByteArray());

                // remember close de FileOutput
                fo.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG,"FILE not found ERROR: "+e.getLocalizedMessage());

                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG,"FILE CANNOT IO ERROR: "+e.getLocalizedMessage());
                e.printStackTrace();
            }
        }




    }

    /** Included for serialization - write this layer to the output stream. */
    private void writeObject(ObjectOutputStream out,Bitmap currentImage) throws IOException{

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        BitmapDataObject bitmapDataObject = new BitmapDataObject();
        bitmapDataObject.imageByteArray = stream.toByteArray();

        out.writeObject(bitmapDataObject);
        out.close();
    }

    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 111696345129311948L;
        public byte[] imageByteArray;
    }


}