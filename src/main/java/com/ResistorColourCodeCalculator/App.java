package com.ResistorColourCodeCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {

    private static final Scanner read = new Scanner(System.in);
    private static ArrayList<Double> decodedValues;
    private static ArrayList<Double> tolerances;
    private static double resistance, toleranceRatio;

    public static void main(String[] args) {


        System.out.println(
                """
                        
                                        ========================================
                                        | --> RESISTOR COLOUR CODE DECODER <-- |
                                        ========================================
                        """);
        int lineCount, toleranceValueLineNo, exponentValueLineNo;
        boolean iter = true;
        while (iter) {
            while (true) {
                try {
                    System.out.print("\nResister's Line Count : ");
                    lineCount = Integer.parseInt(read.next());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("~ Error: Invalid Input!");
                }
            }
            switch (lineCount) {
                case 4 -> {
                    toleranceValueLineNo = 4;
                    exponentValueLineNo = 3;
                    processValues(lineCount, toleranceValueLineNo, exponentValueLineNo);
                }
                case 5, 6 -> {
                    toleranceValueLineNo = 5;
                    exponentValueLineNo = 4;
                    processValues(lineCount, toleranceValueLineNo, exponentValueLineNo);
                }
                default -> System.out.println(
                        "\n~ Error: Incorrect Input\n\t(!) Line count for a resistor should be 4, 5 or 6"
                );

            }
            System.out.print("\n Exit (y/n) : ");
            iter = read.next().equalsIgnoreCase("n");

        }
    }

    private static void processValues(int lineCount, int toleranceValueLineNo, int exponentValueLineNo) {
        ArrayList<String> inputColourList = readLineColours(lineCount, toleranceValueLineNo);
        String ppmValue = inputColourList.getLast();
        inputColourList.removeLast();
        decodedValues = ColourCodeDecoder.decode(toleranceValueLineNo, lineCount, inputColourList);
        resistance = ColourCodeDecoder.getResistance(exponentValueLineNo, decodedValues);
        toleranceRatio = decodedValues.get(toleranceValueLineNo - 1);
        tolerances = ColourCodeDecoder.getTolerance(toleranceRatio, resistance);
        ArrayList<String> newResistanceValues = ColourCodeDecoder.convToSubUnit(
                new ArrayList<>(Arrays.asList(resistance, tolerances.get(0), tolerances.get(1)))
        );


        System.out.printf(
                """
                        
                        ==========================================
                        Resistance : %.2f %6$s ±%.2f%% %s
                        Positive Tolerance : %5.2f %6$s
                        Negative Tolerance : %5.2f %6$s
                        ==========================================
                        
                        """,
                Float.parseFloat(newResistanceValues.get(0)), toleranceRatio, ppmValue,
                Float.parseFloat(newResistanceValues.get(1)),
                Float.parseFloat(newResistanceValues.get(2)),
                newResistanceValues.getLast()
        );

    }

    private static ArrayList<String> readLineColours(int lineCount, int toleranceColourLineNo) {
        String[] suffix = {"st", "nd", "rd"};
        ArrayList<String> colourArr = new ArrayList<>();
        ArrayList<String> colours = ColourCodeDecoder.getColourList();
        ArrayList<Object> toleranceColours = new ArrayList<>(
                Arrays.asList(ColourCodeDecoder.getToleranceValues().keySet().toArray())
        );
        ArrayList<Object> ppmValueColours = new ArrayList<>(
                Arrays.asList(ColourCodeDecoder.getPPM().keySet().toArray())
        );
        String inputColour = "";
        String ppmValue = "";
        System.out.println();

        for (int i = 0; i < lineCount; i++) {
            boolean iter = true;
            while (iter) {
                System.out.printf("Enter %d %s line's colour : ", i + 1, i < 3 ? suffix[i] : "th");
                inputColour = read.next().trim().toLowerCase();

                if (i != toleranceColourLineNo - 1) {
                    if (lineCount == 6 && i == lineCount - 1) {
                        iter = !ppmValueColours.contains(inputColour);
                        if (iter) {
                            System.out.printf("\n~ Error: Invalid input\n\t(!) Inputted colour for the %d th (ppm value) line is incorrect \n\n", i + 1);
                        } else {
                            ppmValue = "( " + ColourCodeDecoder.getPPM().getInt(inputColour) + " PPM )";
                        }
                    } else {
                        iter = !colours.contains(inputColour);
                        if (iter) {
                            System.out.printf("\n~ Error: Invalid input\n\t(!) Inputted colour for the line %d is incorrect\n\n", i + 1);
                        }
                    }
                } else {
                    iter = !toleranceColours.contains(inputColour);
                    if (iter) {
                        System.out.println("\n~ Error: Invalid input\n\t(!) Inputted colour for the tolerance line is incorrect\n");
                    }
                }
            }
            colourArr.add(inputColour);
        }
        colourArr.add(ppmValue);
        return colourArr;
    }

}
