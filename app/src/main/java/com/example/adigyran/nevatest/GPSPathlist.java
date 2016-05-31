package com.example.adigyran.nevatest;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by adigyran on 26.05.2016.
 */
public class GPSPathlist {

    private ArrayList<GPSPathpoint> GPSPoints;

    public int GetSize()
    {
        return GPSPoints.size();
    }
    public ArrayList<GPSPathpoint> getGPSPoints() {
        return GPSPoints;
    }

    public void setGPSPoints(ArrayList<GPSPathpoint> GPSPoints) {
        this.GPSPoints = GPSPoints;
    }
    public void addGPSPoint(GPSPathpoint newpoint)
    {
        if (newpoint ==null) {
            Log.d("nevatest", "addGPSPoint: nullpoint add");
            return;
        }
        GPSPoints.add(newpoint);
    }
    public void removeGPSPoints(ArrayList<GPSPathpoint> removpoints)
    {
        if (removpoints ==null) {
            Log.d("nevatest", "removeGPSPoint: nullpoints remove");
            return;
        }
        GPSPoints.removeAll(removpoints);
    }

    public ArrayList<GPSPathpoint> getrange(GPSPathpoint firstpoint,GPSPathpoint secondpoint)
    {   int firstindex = 0;
        int secondindex = 0;
        boolean isfirstpoint =false;
        boolean issecondpoint =false;
        if (GPSPoints==null){Log.d("ERROR", "getrange: list_is_empty");return null;}
        if(firstpoint==null && secondpoint ==null){Log.d("ERROR", "getrange: points_empty");return null;}

        ArrayList<GPSPathpoint> returnrange =null;
        if(firstpoint == null)
        {Log.d("ERROR", "getrange: firstpoint_not_set");return null;}
        if(secondpoint == null)
        {Log.d("ERROR", "getrange: secondpoint_not_set");return null;}
        for (GPSPathpoint GPSpoint:GPSPoints) {  //Checking contain first point and second in list and get their indexes
           // if (!GPSpoint.getSubtitle().equals(firstpoint.getSubtitle())){Log.d("ERROR", "getrange: firstpoint_not_in_list");return null;}
            //else {firstindex = GPSPoints.indexOf(GPSpoint);}
           // if (!GPSpoint.getSubtitle().equals(secondpoint.getSubtitle())){Log.d("ERROR", "getrange: secondpoint_not_in_list");return null;}

            if((GPSpoint.getPointdatetime().compareTo(firstpoint.getPointdatetime())==0)&& isfirstpoint==false)
            { isfirstpoint = true; firstindex = GPSPoints.indexOf(GPSpoint);}
            if((GPSpoint.getPointdatetime().compareTo(secondpoint.getPointdatetime())==0)&& issecondpoint==false)
            { issecondpoint = true; secondindex = GPSPoints.indexOf(GPSpoint);}
        }
        if (isfirstpoint ==false || issecondpoint ==false)
        {
            {Log.d("ERROR", "getrange: points_not_in_list");return null;}
        }
      //  int b=0;
        for (int b = firstindex;b<secondindex;b++)
        {
            returnrange.add(GPSPoints.get(b));
        }
      //  for (int i = GPSPoints.indexOf())
        //if(GPSPoints.contains(firstpoint)==false)
         //    {Log.d("ERROR", "getrange: firstpoint_not_in_list");return null;}
        //if(GPSPoints.contains(secondpoint)==false)
         //   {Log.d("ERROR", "getrange: secondpoint_not_in_list");return null;}

        return returnrange;
    }





}
