package com.locomotor.mydatabasehelper;


import java.util.ArrayList;

/**
 * Created by MiloRambaldi on 23-Jan-16.
 */
public class RequestItem {

    public ArrayList<RequestItem> items;

    public String _id;
    public String requestDateStart;
    public String requestDateEnd;
    public String requestUserName;
    public int requestSmilePercentage;



    public RequestItem() {
        this.items = new ArrayList<>();
    }

}
