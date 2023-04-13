import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.lang.model.util.ElementScanner14;

import java.util.Arrays;
import java.text.CollationElementIterator;
import java.util.*;
import java.util.Comparator;

// run report every hour
// get temperature every minute
// report has top 5 lowest and top 5 highest of hour
// and has 10 minute interval with biggest difference

// loop 60 times, sleep 25 ms then put random number in list
// make arraylist of arraylist, or 2d array, with 8 arraylists, one for each sensor
// each of the 8 sensors records a temperature every minute
// so there are 8 arraylists of 60 temperatures for each hour

public class p2 {

    public static void main(String[] args) {

        final int sensors = 8;
        final int hours = 5;

        // each sensor has a list of temperatures
        ArrayList<ArrayList<Integer>> temps = new ArrayList<ArrayList<Integer>>(sensors);

        // initialize a rover
        Rover[] rv;

        // used to find out how long the simulation took
        long start = 0;
        long end = 0;
        long duration = 0;

        // start the simulation
        start = System.currentTimeMillis();

        // get temperatures each hour
        for (int i = 0; i < hours; i++) {

            // make an array of 8 threads, one for each sensor
            rv = new Rover[sensors];

            // populate each thread
            for (int j = 0; j < sensors; j++) {

                ArrayList<Integer> sensorTemps = new ArrayList<Integer>();
                rv[i] = new Rover(sensorTemps, j);
                temps.add(sensorTemps); // add the list of temps from each sensor to the greater list
            }

            // start each thread
            for (int j = 0; j < sensors; j++) {

                rv[i].run();

            }

            // stop the threads
            for (int j = 0; j < sensors; j++) {

                try {

                    rv[i].join();
                }

                catch (Exception e) {

                    e.printStackTrace();
                }
            }

            // print output from the generated report each hour
            Report rep = new Report(temps, i);
            rep.highTemps();
            rep.lowTemps();
            rep.printOutput();

        }

        end = System.currentTimeMillis();
        duration = end - start;

        // print output
        System.out.println("It took " + duration + " ms to record the temperature for " + hours + " hours.");
    }

}

class Rover extends Thread {

    ArrayList<Integer> temperatures; // the list of temperatures held by this sensor
    int id; // the id of the current sensor

    // constructor
    Rover(ArrayList<Integer> arr, int id) {
        this.temperatures = arr;
        this.id = id;
    }

    public void run() {

        // fills a sensor's list with temperatures each minute of an hour

        Random rand = new Random();

        for (int i = 0; i < 60; i++) {

            // simulate a minute passing
            try {

                Rover.sleep(28);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            // get a random temperature in range -100F to 70F
            int nextTemp = rand.nextInt(70 - (-100 - 1)) + (-100);

            temperatures.add(nextTemp);
        }
    }
}

class Report {

    int id; // the id is the hour the report is for
    ArrayList<ArrayList<Integer>> temps; // all the temperatures for all the sensors this hour
    int rangedInterval; // the interval with the highest range

    // the top 5 highest and lowest temperatures of the hour
    ArrayList<Integer> highestTemps = new ArrayList<>();
    ArrayList<Integer> lowestTemps = new ArrayList<>();

    // constructor
    Report(ArrayList<ArrayList<Integer>> temperatures, int hourNum) {
        this.temps = temperatures;
        this.id = hourNum;
    }

    // get the lowest/highest temperatures

    public void highTemps() {

        // merge each sensor's list into one
        ArrayList<Integer> total = new ArrayList<>();

        for (ArrayList<Integer> list : temps) {
            total.addAll(list);
        }

        // get the 5 top temperatures
        total.sort(Comparator.reverseOrder());

        for (int i = 0; i < 5; i++)
            highestTemps.add(total.get(i));

    }

    public void lowTemps() {

        // merge each sensor's list into one
        ArrayList<Integer> total = new ArrayList<>();

        for (ArrayList<Integer> list : temps) {
            total.addAll(list);
        }

        // get the 5 lowest temperatures
        total.sort(Comparator.naturalOrder());

        for (int i = 0; i < 5; i++)
            lowestTemps.add(total.get(i));

    }

    // get the 10-minute intervals and ranges
    public int largestRangeInterval() {

        // there are 6 10-minute intervals in the hour
        // every 10 temperatures for a sensor will be in an interval
        // there will be 80 temperatures in each interval
        // 10 from each sensor

        // create lists for each interval
        ArrayList<Integer> int1 = new ArrayList<>();
        ArrayList<Integer> int2 = new ArrayList<>();
        ArrayList<Integer> int3 = new ArrayList<>();
        ArrayList<Integer> int4 = new ArrayList<>();
        ArrayList<Integer> int5 = new ArrayList<>();
        ArrayList<Integer> int6 = new ArrayList<>();

        // every 10 temperatures from each sensor
        // goes into the next interval
        for (ArrayList<Integer> list : temps) {

            if (list.size() != 0) {
                for (int i : list) {
                    for (int j = 0; j < 80; j++) {
                        int1.add(list.get(j));
                    }
                    for (int j = 80; j < 160; j++) {
                        int2.add(list.get(j));
                    }
                    for (int j = 160; j < 240; j++) {
                        int3.add(list.get(j));
                    }
                    for (int j = 240; j < 320; j++) {
                        int4.add(list.get(j));
                    }
                    for (int j = 320; j < 400; j++) {
                        int5.add(list.get(j));
                    }
                    for (int j = 400; j < 480; j++) {
                        int6.add(list.get(j));
                    }
                }
            }

        }

        // sort each interval
        int1.sort(Comparator.naturalOrder());
        int2.sort(Comparator.naturalOrder());
        int3.sort(Comparator.naturalOrder());
        int4.sort(Comparator.naturalOrder());
        int5.sort(Comparator.naturalOrder());
        int6.sort(Comparator.naturalOrder());

        // determine the ranges
        int range1 = int1.get(79) - int1.get(0);
        int range2 = int2.get(79) - int2.get(0);
        int range3 = int3.get(79) - int3.get(0);
        int range4 = int4.get(79) - int4.get(0);
        int range5 = int5.get(79) - int5.get(0);
        int range6 = int6.get(79) - int6.get(0);

        // return the largest range
        if (range1 >= range2 && range1 >= range3 && range1 >= range4 && range1 >= range5 && range1 >= range6)
            return 1;
        else if (range2 >= range1 && range2 >= range3 && range2 >= range4 && range2 >= range5 && range2 >= range6)
            return 2;
        else if (range3 >= range1 && range2 >= range3 && range3 >= range4 && range3 >= range5 && range3 >= range6)
            return 3;
        else if (range4 >= range1 && range4 >= range2 && range4 >= range3 && range4 >= range5 && range4 >= range6)
            return 4;
        else if (range5 >= range1 && range5 >= range2 && range5 >= range3 && range5 >= range4 && range5 >= range6)
            return 5;
        else if (range6 >= range1 && range6 >= range2 && range6 >= range3 && range6 >= range4 && range6 >= range5)
            return 6;
        else
            return 0;

    }

    public void printOutput() {

        System.out.println("Report for hour: " + (id + 1));
        System.out.println("------------");
        System.out.println("Top 5 highest temperatures:");
        for (int i : highestTemps) {
            System.out.println(i);
        }
        System.out.println("Top 5 lowest temperatures:");
        for (int i : lowestTemps) {
            System.out.println(i);
        }
        System.out.println("Interval with the highest temperature difference: " + largestRangeInterval());
        System.out.println("------------");
    }

}
