package com.company;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String url = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";
        String response = getJSONArray(url, "GET");
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonList = (JSONArray) jsonResponse.get("list");
        int count = jsonList.length();
        for (int i = 0; i < count; i++) {
            JSONObject jsonObject = jsonList.getJSONObject(i);
            String dateString = (String) jsonObject.get("dt_txt");
            String[] stringArray = dateString.split(" ");
            jsonObject.put("date", stringArray[0]);
            jsonObject.put("time", stringArray[1]);
        }
        while (true) {
            System.out.println("Enter the value:");
            int userInput = input.nextInt();
            if (userInput == 0) {
                break;
            } else {
                Scanner newinput = new Scanner(System.in);
                System.out.println("Enter the date (in the format of YYYY-MM-DD) ");
                String date = newinput.nextLine();
                System.out.println("Enter the time (in the format of HH:MM:SS)");
                String time = newinput.nextLine();
                getAnswer(userInput, date, time, jsonList);
            }
        }
    }

    static void getAnswer(int a, String date, String time, JSONArray jsonList) {
        int count = jsonList.length();
        for (int i = 0; i < count; i++) {
            JSONObject jsonObject = jsonList.getJSONObject(i);
            if (jsonObject.get("date").equals(date) && jsonObject.get("time").equals(time)) {
                if (a == 1) {
                    JSONObject jo = (JSONObject) jsonObject.get("main");
                    System.out.println("The temperature is " + jo.get("temp"));
                    return;
                } else if (a == 3) {

                    JSONObject jo = (JSONObject) jsonObject.get("main");
                    System.out.println("The Pressure is " + jo.get("pressure"));
                    return;
                } else if (a == 2) {
                    JSONObject jo = (JSONObject) jsonObject.get("wind");
                    System.out.println("The wind speed is " + jo.get("speed"));
                    return ;
                }
            }
        }
        System.out.println("No Data available for this date and time");
    }

    static String getJSONArray(String url, String httpCall) {

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(httpCall);
            int responseCode = 0;
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            }
            String errorMsg = "Error: " + responseCode ;
            return errorMsg;
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
          return  e.getMessage();
        }
    }
}
