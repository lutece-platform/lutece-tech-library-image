/*
 * Copyright (c) 2002-2013, Mairie de Paris
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


import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

/**
 * Utils for image processing
 */
public class ImageUtil
{

    private static final Logger _logger = Logger.getLogger( ImageUtil.class );

    /**
     * Resize an image to a given size with a given quality using a JPEG output format
     * @param blobImage The image as a BLOB
     * @param strWidth The new width
     * @param strHeight The new Height
     * @param fQuality The quality as a float between 0.0 (lower) and 1.0 (higher)
     * @return
     */
    public static byte[] resizeImage( Object blobImage, String strWidth, String strHeight, float fQuality )
    {

        byte[] newBlobImage = ( byte[] ) blobImage;
        if ( blobImage != null )
        {
            ByteArrayInputStream bais = new ByteArrayInputStream( newBlobImage );
            BufferedImage image = null;
            try
            {
                image = ImageIO.read( bais );

                // Parameters
                double nParamWidth = Double.valueOf( strWidth );
                double nParamHeight = Double.valueOf( strHeight );
                double nParamRatio = BigDecimal.valueOf( nParamWidth ).divide( BigDecimal.valueOf( nParamHeight ), 6 ).doubleValue();

                // Image attributes
                double nImageWidth = Double.valueOf( image.getWidth() );
                double nImageHeight = Double.valueOf( image.getHeight() );
                double nImageRatio = BigDecimal.valueOf( nImageWidth ).divide( BigDecimal.valueOf( nImageHeight ), 6 ).doubleValue();

                if ( nImageWidth > nParamWidth || nImageHeight > nParamHeight )
                {
                    double nTargetWidth = 0;
                    double nTargetHeight = 0;
                    // The width is larger than the height (landscape)
                    if ( nParamRatio > nImageRatio )
                    {
                        // Set the Height target to min ( Image Height , Param Height )
                        nTargetHeight = ( nImageHeight > nParamHeight ) ? nParamHeight : nImageHeight;
                        nTargetWidth = BigDecimal.valueOf( nTargetHeight ).multiply( BigDecimal.valueOf( nImageRatio ) ).doubleValue();
                    }
                    // The Height is larger than the width (portrait)
                    else
                    {
                        // Set the Width target to min ( Image Width , Param Width )
                        nTargetWidth = ( nImageWidth > nParamWidth ) ? nParamWidth : nImageWidth;
                        nTargetHeight = BigDecimal.valueOf( nTargetWidth ).divide( BigDecimal.valueOf( nImageRatio ), 6 ).doubleValue();
                    }
                    BufferedImage bdest = new BufferedImage( ( ( Double ) nTargetWidth ).intValue(), ( ( Double ) nTargetHeight ).intValue(), BufferedImage.TYPE_INT_RGB );
                    Graphics2D g = bdest.createGraphics();
                    AffineTransform at = AffineTransform.getScaleInstance( Double.valueOf( nTargetWidth ) / image.getWidth(), Double.valueOf( nTargetHeight ) / image.getHeight() );
                    g.drawRenderedImage( image, at );

                    return getJPEGImageAsByteTab( bdest , fQuality ).toByteArray();
                }
            }
            catch ( IOException e )
            {
                _logger.error( "Error ImageUtil : " + e.getMessage(), e );
            }
        }
        return newBlobImage;
    }

    /**
     * Convert the buffered image as an array of bytes in a JPEG format
     * @param image The image
     * @param fQuality The quality
     * @return The image as a Byte array
     */
    private static ByteArrayOutputStream getJPEGImageAsByteTab( BufferedImage image, float fQuality )
    {
        ByteArrayOutputStream outBuffered = null;
        try
        {
            outBuffered = new ByteArrayOutputStream();

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( outBuffered );
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam( image );

            param.setQuality( fQuality, true );
            encoder.setJPEGEncodeParam( param );

            encoder.encode( image );

        }
        catch ( IOException e2 )
        {
            return null;
        }
        return outBuffered;
    }
}
