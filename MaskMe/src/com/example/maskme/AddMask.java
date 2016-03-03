package com.example.maskme;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddMask extends Activity{

	public Button mPreview;
	public ImageView mImage;
	String mCurrentPhotoPath;
	private Bitmap mFaceDetectionBitmap;
	LinearLayout mLayoutImageBackground;
	//Paint mPaint;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	
	private static final int MAX_FACES = 10;
	private RectF[] rects = new RectF[MAX_FACES];
	private int [] mPX = null;
	private int [] mPY = null;
	private int mDisplayStyle = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_mask);
		
		
		//Paint
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setColor(Color.YELLOW);
		mPaint.setStrokeWidth(6);
		final Intent intent = new Intent(this, Photo.class);
		
		mLayoutImageBackground=(LinearLayout)findViewById(R.id.view_drawing);	
		
		//Bundle bundle = getIntent().getExtras();
		mCurrentPhotoPath = getIntent().getStringExtra("imagePath");
		
		//Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_LONG).show();
		
		
		mImage = (ImageView) findViewById(R.id.imageView2);
		
		// SHOW THE IMAGE
		setPic(mCurrentPhotoPath);
		
		mPreview = (Button) findViewById(R.id.btn_preview);
		mPreview.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				
				 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent); 
				/*Intent i = new Intent("android.intent.action.MAIN");
				    startActivity(i);
				    finish();
				    */
			}
			});
	}

	private void setPic(String PhotoPath) {
		// TODO Auto-generated method stub
		
		/* Get the size of the ImageView */
		int targetW = mImage.getWidth();
		int targetH = mImage.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(PhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(PhotoPath);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		
		// Convert bitmap in 556
		//mFaceDetectionBitmap= bitmap.copy(Bitmap.Config.RGB_565, true);
		
		
		
		//Create a new image bitmap and attach a brand new canvas to it
		 mFaceDetectionBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
		Canvas tempCanvas = new Canvas(mFaceDetectionBitmap);
		
		//Draw the image bitmap into the cavas
		tempCanvas.drawBitmap(bitmap, 0, 0, null);
		
		
		//Attach the canvas to the ImageView
		mImage.setImageDrawable(new BitmapDrawable(getResources(), mFaceDetectionBitmap));
		
		
		/*
		mLayoutImageBackground.setBackground(new BitmapDrawable(mFaceDetectionBitmap));
		//SHOW IMAGE
		//mImage.setImageBitmap(mFaceDetectionBitmap);
		*/
		mImage.setVisibility(View.VISIBLE);
		detectFaces(mFaceDetectionBitmap);
		
		//Draw everything else you want into the canvas, in this example a rectangle with rounded edges
		draw(tempCanvas);

		bitmap.recycle();
	}

	
	private void detectFaces(Bitmap tmpBmp) {
		// TODO Auto-generated method stub
		
		FaceDetector fd;
    	FaceDetector.Face [] faces = new FaceDetector.Face[MAX_FACES];
    	PointF eyescenter = new PointF();
    	float eyesdist = 0.0f;
    	int [] fpx = null;
    	int [] fpy = null;
    	int count = 0;
    	
    	fd = new FaceDetector(tmpBmp.getWidth(), tmpBmp.getHeight(), MAX_FACES);        
		count = fd.findFaces(tmpBmp, faces);
		
		String s = Integer.toString(count);
		Toast.makeText(this,"No of faces detected: " + s, Toast.LENGTH_LONG).show();
		
		
		// check if we detect any faces
    	if (count > 0) {
    		fpx = new int[count * 2];
    		fpy = new int[count * 2];

    		for (int i = 0; i < count; i++) { 
    			                 
    				faces[i].getMidPoint(eyescenter);                  
    				eyesdist = faces[i].eyesDistance();                  

    				// set up left eye location
    				fpx[2 * i] = (int)(eyescenter.x - eyesdist / 2);
    				fpy[2 * i] = (int)eyescenter.y;
    				
    				// set up right eye location
    				fpx[2 * i + 1] = (int)(eyescenter.x + eyesdist / 2);
    				fpy[2 * i + 1] = (int)eyescenter.y;

    			}            
    		}  
    	setDisplayPoints(fpx, fpy, count * 2, 1);
	}

	private void setDisplayPoints(int [] xx, int [] yy, int total, int style) {
		// TODO Auto-generated method stub
		
		mDisplayStyle = style;
		mPX = null;
		mPY = null;
		
		if (xx != null && yy != null && total > 0) {			
			mPX = new int[total];
			mPY = new int[total];

			for (int i = 0; i < total; i++) {
				mPX[i] = xx[i];
				mPY[i] = yy[i];
			}
		}
	}
	
	
	protected void draw(Canvas canvas) {	
		
			if (mPX != null && mPY != null) {
				for (int i = 0; i < mPX.length; i++) {
					if (mDisplayStyle == 1) {
						canvas.drawCircle(mPX[i], mPY[i], 40.0f, mPaint);
					} else {
						canvas.drawRect(mPX[i] - 20,  mPY[i] - 20, mPX[i] + 20,  mPY[i] + 20, mPaint);
					}
				}
			}
		
	}
	
}
