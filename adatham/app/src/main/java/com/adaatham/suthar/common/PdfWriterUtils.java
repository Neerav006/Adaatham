package com.adaatham.suthar.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import androidx.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfWriterUtils {


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    ArrayList<Bitmap> overWritePdf(File pdfFile, Context context, String name, String number, String amount
            , String[] nameArr, String[] mem_no_arr, String[] amount_arr) {

        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        Log.e("nameArr", "" + nameArr.length);
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_WRITE));

            final int pageCount = renderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);

                Log.e("density", "" + context.getResources().getDisplayMetrics().densityDpi);


                int width = 480 / 72 * page.getWidth();
                int height = 480 / 72 * page.getHeight();
                final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                if (i == 0) {

                    Canvas canvas = new Canvas(bitmap);
                    // new antialised Paint
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    // text color - #3D3D3D
                    paint.setColor(Color.BLACK);
                    // text size in pixels
                    paint.setTextSize((int) (60));
                    // text shadow


//                canvas.drawText(name, 850, 1150, paint);
//                canvas.drawText(number, 2500, 1150, paint);
//                canvas.drawText(amount, 2950, 1150, paint);

                    int yPos = 1150;

                    for (int j = 0; j < nameArr.length; j++) {


                        canvas.drawText(nameArr[j].toUpperCase(), 850, yPos, paint);
                        yPos += 100;
                    }

                    yPos = 1150;

                    for (int j = 0; j < mem_no_arr.length; j++) {


                        canvas.drawText(mem_no_arr[j], 2500, yPos, paint);
                        yPos += 100;
                    }

                    yPos = 1150;
                    for (int j = 0; j < amount_arr.length; j++) {


                        canvas.drawText(amount_arr[j], 2950, yPos, paint);
                        yPos += 100;
                    }
                }

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                // Bitmap scaleBitmap = getResizedBitmap(bitmap,842,595);

                bitmaps.add(bitmap);

                // close the page
                page.close();


            }

            // close the renderer
            renderer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (bitmaps.size() > 0) {

            Log.e("bitmap", "bitmap created..");

            drawTextToBitmap(context, bitmaps, "Hello World..", pdfFile);
        }

        return bitmaps;

    }

    private void drawTextToBitmap(Context mContext, ArrayList<Bitmap> bitmaps, String mText, File pdfFile) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;

//            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
//            // set default bitmap config if none
//            if(bitmapConfig == null) {
//                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
//            }
//            // resource bitmaps are imutable,
//            // so we need to convert it to mutable one
//            bitmap = bitmap.copy(bitmapConfig, true);
//            Log.e("bitmap width:",""+bitmap.getWidth());
//            Log.e("bitmap height:",""+bitmap.getHeight());
//
//            Canvas canvas = new Canvas(bitmap);
//            // new antialised Paint
//            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            // text color - #3D3D3D
//            paint.setColor(Color.BLACK);
//            // text size in pixels
//            paint.setTextSize((int) (20));
//            // text shadow
//
//
//            canvas.drawText(mText, 100, 200 , paint);
            PdfDocument document = new PdfDocument();
            for (int j = 0 ; j<bitmaps.size();j++){
                Bitmap resizedBitmap = getResizedBitmap(bitmaps.get(j), 2384, 3370);

                /**
                 *    Re Create pdf
                 */



                // crate a page description
                PdfDocument.PageInfo pageInfo =
                        new PdfDocument.PageInfo.Builder(2384, 3370, j+1).create();


                PdfDocument.Page page = document.startPage(pageInfo);
                page.getCanvas().drawBitmap(resizedBitmap, 0, 0, null);
                document.finishPage(page);


                // write the document content

                try {
                    document.writeTo(new FileOutputStream(pdfFile));
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }



            // close the document
            document.close();

            /**
             *
             */



        } catch (Exception e) {
            // TODO: handle exception
            Log.e("exception ", e.getMessage());

        }

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

}
