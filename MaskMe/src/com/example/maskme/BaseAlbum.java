package com.example.maskme;

import java.io.File;

import android.os.Environment;

public final class BaseAlbum  extends Album{
	// Standard storage location for digital camera files
		private static final String CAMERA_DIR = "/dcim/";

		@Override
		public File getAlbumStorageDir(String albumName) {
			return new File (
					Environment.getExternalStorageDirectory()
					+ CAMERA_DIR
					+ albumName
			);
		}
}
