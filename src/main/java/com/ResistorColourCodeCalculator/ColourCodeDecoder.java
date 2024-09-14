package com.ResistorColourCodeCalculator;


import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author banuka20431
 */
public class ColourCodeDecoder {

    private static JSONObject jsonData = null;

    static {
        try {
            jsonData = jsonReader.readJsonData("data.json");
        } catch (IOException _) {
        }
    }

    public static ArrayList<Double> decode(int toleranceValueLineNo, int lineCount, ArrayList<String> inputColourArr) {
        ArrayList<Double> valueArr = new ArrayList<>();
        ArrayList<String> colours = getColourList();
        JSONObject toleranceValues = getToleranceValues();
        double colourValue, toleranceValue;

        for (int i = 1; i <= lineCount; i++) {
            if (i != toleranceValueLineNo) {
                colourValue = colours.indexOf(inputColourArr.get(i - 1));
                valueArr.add(colourValue);
            } else {
                toleranceValue = toleranceValues.getDouble(inputColourArr.get(i - 1));
                valueArr.add(toleranceValue);
            }
        }
        return valueArr;
    }



    public static double getResistance(int exponentValueLineNo, ArrayList<Double> decodedValues) {
        double resistance = 0.0;
        double exponent = decodedValues.get(exponentValueLineNo - 1);
        Iterator<Double> decodedValuesIter = decodedValues.iterator();

        for (int i = exponentValueLineNo - 2; i >= 0; i--) {
            resistance += decodedValuesIter.next() * Math.pow(10, i);
        }
        // System.out.println(resistance + " > " + exponent);
        resistance *= Math.pow(10, exponent);

        return resistance;
    }

    public static ArrayList<Double> getTolerance(Double toleranceRatio, double resistance) {
        ArrayList<Double> toleranceArr = new ArrayList<>();
        toleranceArr.add(0, resistance + resistance * toleranceRatio / 100);
        toleranceArr.add(1, resistance - resistance * toleranceRatio / 100);
        return toleranceArr;
    }

    public static ArrayList<String> convToSubUnit(ArrayList<Double> resistanceValues) {
        ArrayList<String> returnArr = new ArrayList<>();
        JSONObject subUnits = getSubUnits();
        String selectedSubUnit = "Ohm";

        for (Double value : resistanceValues) {
            for (String unit : new ArrayList<>(Arrays.asList("Ohm", "Kohm", "Mohm", "Gohm", "Tohm"))) {
                if (value / Math.pow(10, subUnits.getInt(unit)) < 1) {
                    break;
                } else {
                    selectedSubUnit = unit;
                }
            }
            double newValue = value / Math.pow(10, subUnits.getInt(selectedSubUnit));
            returnArr.add(String.valueOf(newValue));
        }
        returnArr.add(selectedSubUnit);
        return returnArr;
    }

    public static JSONObject getJSONdata() {
        return jsonData;
    }

    public static JSONObject getToleranceValues() {
        return getJSONdata().getJSONObject("tolerance");
    }

    public static ArrayList<String> getColourList() {
        ArrayList<String> colours = new ArrayList<>();
        for (Object obj : getJSONdata().getJSONArray("colours").toList()) {
            colours.add((String) obj);
        }
        return colours;
    }

    public static JSONObject getSubUnits() {
        return getJSONdata().getJSONObject("subUnits");
    }

    public static JSONObject getPPM() {
        return getJSONdata().getJSONObject("ppm");
    }
}
