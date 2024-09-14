package com.ResistorColourCodeCalculator;


import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

/**
 * jsonReader
 */
public class jsonReader {

    public static JSONObject readJsonData(String jsonFilPath) throws IOException {
        FileReader fileReader = null;
        int out = 0;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileReader = new FileReader(jsonFilPath);
        } catch (IOException e) {
            e.fillInStackTrace();
        }

        assert fileReader == null;

        do {
            out = fileReader.read();
            stringBuilder.append((char) out);
        } while (out != -1);

        return  new JSONObject(stringBuilder.toString());
    }

}