/*
 * Copyright (c) 2002-2016, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.util.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * ImageUtilTest
 */
public class ImageUtilTest
{

    /**
     * Test of JPG resize.
     * @throws Exception e
     */
    @Test
    public void testResizeJPG( )
    throws Exception
    {
        byte[] data = loadBytes("marco_orig.jpg");
        byte[] myByteArray = ImageUtil.resizeImage( data, "40", "40", 0.8f );
        byte[] expected = loadBytes("marco_resized_jpg.jpg");
        dumpBytes("marco_resized_jpg.jpg", myByteArray);
        Assert.assertArrayEquals(myByteArray, expected);
    }

    /**
     * Test of PNG resize.
     * @throws Exception e
     */
    @Test
    public void testResizePNG( )
    throws Exception
    {
        byte[] data = loadBytes("marco_orig.png");
        byte[] myByteArray = ImageUtil.resizeImage( data, "40", "40", 0.8f );
        byte[] expected = loadBytes("marco_resized_png.jpg");
        dumpBytes("marco_resized_png.jpg", myByteArray);
        Assert.assertArrayEquals(myByteArray, expected);
    }

    private byte[] loadBytes(String filename) throws Exception {
        InputStream in = this.getClass().getResourceAsStream("/" + filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        for (;;) {
            int nread = in.read(buffer);
            if (nread <= 0) {
                break;
            }
            baos.write(buffer, 0, nread);
        }
        byte[] data = baos.toByteArray();
        return data;
    }

    private void dumpBytes(String filename, byte[] myByteArray) throws Exception {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        File out = new File(baseDir, filename);
        System.out.println("Saving test output to : " + out.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(out.getAbsolutePath());
        fos.write(myByteArray);
        fos.close();
    }
}
