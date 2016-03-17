package com.kbeanie.multipicker;

import com.kbeanie.multipicker.utils.BitmapUtils;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by kbibek on 3/18/16.
 */
public class BitmapUtilsTestCase extends TestCase {
    public void testGetScaledDimensionsNoScaling() {
        int imageWidth = 100;
        int imageHeight = 200;
        int maxWidth = 300;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Assert.assertNotNull(dimensions);
        Assert.assertEquals(imageWidth, dimensions[0]);
        Assert.assertEquals(imageHeight, dimensions[1]);
    }

    public void testGetScaledDimensionsLargeHeight() {
        int imageWidth = 100;
        int imageHeight = 500;
        int maxWidth = 300;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Assert.assertNotNull(dimensions);
        Assert.assertEquals((int) (((float) 100 / 500) * 300), dimensions[0]);
        Assert.assertEquals(maxHeight, dimensions[1]);
    }

    public void testGetScaledDimensionsLargeWidth() {
        int imageWidth = 700;
        int imageHeight = 200;
        int maxWidth = 300;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Assert.assertNotNull(dimensions);
        Assert.assertEquals(maxWidth, dimensions[0]);
        Assert.assertEquals((int) (((float) 200 / 700) * 300), dimensions[1]);
    }


    public void testGetScaledDimensionsLargeBoth() {
        int imageWidth = 800;
        int imageHeight = 800;
        int maxWidth = 300;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Assert.assertNotNull(dimensions);
        Assert.assertEquals(300, dimensions[0]);
        Assert.assertEquals(300, dimensions[1]);
    }

    public void testGetScaledDimensionsLargeBoth2() {
        int imageWidth = 800;
        int imageHeight = 1200;
        int maxWidth = 200;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Assert.assertNotNull(dimensions);
        Assert.assertEquals((int) ((800 / (float) 1200) * 300), dimensions[0]);
        Assert.assertEquals(300, dimensions[1]);
    }
}
