/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.leptonica.android.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;

/**
 * @author alanv@google.com (Alan Viverette)
 */
public class ReadFileTest extends TestCase {

    private static final String TAG = ReadFileTest.class.getSimpleName();

    @SmallTest
    public void testReadBitmap() {
        testReadBitmap(1, 1, Bitmap.Config.ARGB_8888);
        testReadBitmap(640, 480, Bitmap.Config.ARGB_8888);
    }

    private void testReadBitmap(int width, int height, Bitmap.Config format) {
        Bitmap bmp = TestUtils.createTestBitmap(640, 480, format);
        Pix pix = ReadFile.readBitmap(bmp);

        assertEquals(bmp.getWidth(), pix.getWidth());
        assertEquals(bmp.getHeight(), pix.getHeight());

        float match = TestUtils.compareImages(pix, bmp);
        Log.d(TAG, "match=" + match);
        assertTrue("Images do not match.", (match >= 0.99f));

        bmp.recycle();
        pix.recycle();
    }

    @SmallTest
    public void testReadFile_bmp() throws IOException {
        File file = File.createTempFile("testReadFile", ".bmp");
        FileOutputStream fileStream = new FileOutputStream(file);
        Bitmap bmp = TestUtils.createTestBitmap(640, 480, Bitmap.Config.ARGB_8888);
        bmp.compress(CompressFormat.PNG, 100, fileStream);
        Pix pix = ReadFile.readFile(file);

        assertEquals(bmp.getWidth(), pix.getWidth());
        assertEquals(bmp.getHeight(), pix.getHeight());

        float match = TestUtils.compareImages(pix, bmp);
        Log.d(TAG, "match=" + match);
        assertTrue("Images do not match.", (match >= 0.99f));

        fileStream.close();
        bmp.recycle();
        pix.recycle();
    }

    @SmallTest
    public void testReadFile_jpg() throws IOException {
        File file = File.createTempFile("testReadFile", ".jpg");
        FileOutputStream fileStream = new FileOutputStream(file);
        Bitmap bmp = TestUtils.createTestBitmap(640, 480, Bitmap.Config.ARGB_8888);
        bmp.compress(CompressFormat.JPEG, 85, fileStream);
        Pix pix = ReadFile.readFile(file);

        assertEquals(bmp.getWidth(), pix.getWidth());
        assertEquals(bmp.getHeight(), pix.getHeight());

        float match = TestUtils.compareImages(pix, bmp);
        Log.d(TAG, "match=" + match);
        assertTrue("Images do not match.", (match >= 0.99f));

        fileStream.close();
        bmp.recycle();
        pix.recycle();
    }

    @SmallTest
    public void testReadMem_jpg() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Bitmap bmp = TestUtils.createTestBitmap(640, 480, Bitmap.Config.ARGB_8888);
        bmp.compress(CompressFormat.JPEG, 85, byteStream);
        byte[] encodedData = byteStream.toByteArray();
        Pix pix = ReadFile.readMem(encodedData);

        assertEquals(bmp.getWidth(), pix.getWidth());
        assertEquals(bmp.getHeight(), pix.getHeight());

        float match = TestUtils.compareImages(pix, bmp);
        Log.d(TAG, "match=" + match);
        assertTrue("Images do not match.", (match >= 0.99f));

        byteStream.close();
        bmp.recycle();
        encodedData = null;
        pix.recycle();
    }
}
