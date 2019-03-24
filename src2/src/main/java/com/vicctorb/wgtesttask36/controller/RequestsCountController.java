package com.vicctorb.wgtesttask36.controller;

import java.util.TimerTask;

public class RequestsCountController extends TimerTask {


    private static RequestsCountController ourInstance = new RequestsCountController();

    public static RequestsCountController getInstance() {
        return ourInstance;
    }

    static int currentRequestsCount;

    private RequestsCountController() {
        currentRequestsCount = 0;
    }

    @Override
    public void run() {
        currentRequestsCount = 0;
    }

    public void increase() {
        currentRequestsCount++;
    }

    public void reset() {
        currentRequestsCount = 0;
    }

}
