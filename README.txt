

    private Uri mPhotoURI;

    private static final int MEDIA_TYPE_IMAGE = 2;

    private static final int REQUEST_READ_MEDIA = 3;

    private static final String IMAGE_DIRECTORY_NAME = "tenent";

	    //mPhotoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", getOutputMediaFile(MEDIA_TYPE_IMAGE) );


	    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Log.d(LOG_TAG, "onActivityResult ");

        /* check whether able to get the image from camera or gallery */
        if (resultCode == RESULT_OK) {

            /* Check to see whether it obtain the read permission from external source
             *  And If it failed to obtain the read permission, request the read permission.
             */
            if( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_MEDIA);
            } else {

                Uri selectedImage = imageReturnedIntent.getData();

                switch (requestCode) {
                    case PICK_Camera_IMAGE:
                    case PICK_Gallery_IMAGE:

                        selectedImage = imageReturnedIntent.getData();




                        break;
                }
                previewCapturedImage(selectedImage);
            }
        } else {
            Log.d(LOG_TAG, "onActivityResult : Unable to get the image");
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //readDataExternal();
                }
                break;

            default:
                break;
        }
    }



    private void previewCapturedImage(Uri photoUri) {
        Log.d(LOG_TAG, "previewCapturedImage " );

        try {
            // hide video preview
            mImageView.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger images
            options.inSampleSize = 8;

            InputStream inputStream = getContentResolver().openInputStream(photoUri);
            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

            // rotated
            Bitmap thumbnail_r = imageOreintationValidator(resizedBitmap, photoUri.getPath());

            mImageView.setBackground(null);
            mImageView.setImageBitmap(thumbnail_r);
            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // for roted image......
    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        Log.d(LOG_TAG, "imageOreintationValidator " );

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Log.d(LOG_TAG, "rotateImage " );

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            source.recycle();

            Date curDate = java.util.Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("yyyy / MM / dd");
            String today = formatter.format(curDate);

            String fullPath = Environment.getExternalStorageDirectory() + "/RYB_pic/" + today + ".jpg";
            if ((fullPath != null) && (new File(fullPath).exists())) {
                new File(fullPath).delete();
            }
            bitmap = null;
            err.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathToFile, int reqWidth, int reqHeight) {
        Log.d(LOG_TAG, "decodeSampledBitmapFromResource " );

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.e("inSampleSize", "inSampleSize______________in storage" + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        Log.d(LOG_TAG, "calculateInSampleSize " );

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public String getPath(Uri uri) {
        Log.d(LOG_TAG, "getPath " );

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private static File getOutputMediaFile(int type) {
        Log.d(LOG_TAG, "getOutputMediaFile " );

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",  Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator  + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public Uri getOutputMediaFileUri(int type) {
        Log.d(LOG_TAG, "getOutputMediaFileUri " );

        return Uri.fromFile(getOutputMediaFile(type));
    }