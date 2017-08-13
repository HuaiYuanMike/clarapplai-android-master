package com.clarifai.clarapplai;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;

/**
 * Created by mikehhsu on 8/13/17.
 */

public class TrainPhotoManager {
    private ArrayList<Double> dataList; //list of eular distance of embeddings
    private double mean;
    private double sd;

    private static TrainPhotoManager instance;

    public static TrainPhotoManager getInstance(){
        if(instance != null){
            return instance;
        }
        return new TrainPhotoManager();
    }

    private TrainPhotoManager(){
        dataList = new ArrayList<>();
        mean = 0.0;
        sd = 0.0;
    }

    private void calculateStandardDeviation(){
        StandardDeviation standardDeviation = new StandardDeviation();
        setSd(standardDeviation.evaluate(getDataArray()));
    }

    private void calculateMean(){
        Mean mean = new Mean();
        setMean(mean.evaluate(getDataArray()));
//        double temp = 0.0;
//        for(double d : dataSet){
//            d = d / dataSet.length;
//            temp += d;
//        }
    }

    public void addOneEmbedding(double[] embedding){
        double distance = getDistance(embedding);
        addOneDistance(distance);
    }

    public boolean isTheSamePhoto(double[] targetEmbedding){
        double distance = getDistance(targetEmbedding);
        if((distance >= mean - sd) && (distance <= mean + sd)){
            return true;
        }
        return false;
    }

    private void addOneDistance(double distance){
        dataList.add(distance);
        calculateMean();
        calculateStandardDeviation();
    }

    private double getDistance(double[] embedding){
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        return euclideanDistance.compute(embedding, new double[embedding.length]);
    }

    public void setDataSet(ArrayList<Double> dataSet) {
        this.dataList = dataSet;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public void setSd(Double sd) {
        this.sd = sd;
    }

    private double[] getDataArray(){
        double[] dataArry = new double[dataList.size()];
        for(int i = 0; i < dataList.size(); i++){
            dataArry[i] = dataList.get(i);
        }
        return dataArry;
    }

}
