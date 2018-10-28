package com.curonsys.android_java;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.imgproc.Imgproc;

/**
 * Created by ijin-yeong on 2018. 7. 26..
 * 이미지 프로세싱 관련 클래스
 * 분산 계산  /  특징점 추출
 */

public class ImageProcessingManager {
    private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);

    static {
        try {
            System.loadLibrary("opencv_java");
        } catch (UnsatisfiedLinkError e) {
            System.load("opencv_java");
        }
        try {
            System.loadLibrary("nonfree");
        } catch (UnsatisfiedLinkError e) {
            System.load("nonfree");
        }
    }

    public double sift(Bitmap origin){
        Mat output=new Mat();
        Utils.bitmapToMat(origin,output);
        // scaling 기능 추가 할것

        MatOfKeyPoint featurePoints=new MatOfKeyPoint();
        float width = output.width();
        float height = output.height();
        float wh_rate = width/height;
        width = Math.round(width * (800.f/width));
        height = Math.round(width/wh_rate);
        //resizing to 500pixel that width of image.
        Imgproc.resize(output,output,new Size(width,height));
        Imgproc.cvtColor(output,output,Imgproc.COLOR_RGBA2GRAY);

        detector.detect(output,featurePoints);
        KeyPoint[] keypoints = featurePoints.toArray();
        double[] pointX = new double[keypoints.length];
        double[] pointY = new double[keypoints.length];

        for (int i=0; i<keypoints.length; i++){
            pointX[i] = keypoints[i].pt.x;
            pointY[i] = keypoints[i].pt.y;
        }

        pointX = norm_array(pointX,Math.round(width),0);
        pointY = norm_array(pointY,Math.round(height),0);

        double varianceX = array_variance(pointX);
        double varianceY = array_variance(pointY);

        return (varianceX+varianceY)/2 * 60;

    }



    public double[] norm_array(double[] arrays,int max, int min){

        for(int i =0; i<arrays.length;i++){
            arrays[i] = (arrays[i]-min)/(max-min);
        }

        return arrays;

    }
    public double array_variance(double[] data){
        double variance = 0;
        double mean = 0;
        for (int i = 0; i < data.length; i++) {
            mean += data[i];
        }
        mean /= data.length;


        for (int i = 0; i < data.length; i++) {
            variance += (data[i] - mean) * (data[i] - mean);
        }
        variance /= data.length;
        return variance;

    }
}
