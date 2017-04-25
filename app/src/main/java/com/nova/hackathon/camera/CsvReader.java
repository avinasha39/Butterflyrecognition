package com.nova.hackathon.camera;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CsvReader {
    InputStream inputStream;

    public CsvReader(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public String[] read(String stname){
        String[] loc={"India"};
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(";");
                if(row[0].contains(stname)) {
                    loc = row[1].substring(1, row[1].length()-1).split(", ");
                    break;
                }
            }

        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return loc;
    }
}