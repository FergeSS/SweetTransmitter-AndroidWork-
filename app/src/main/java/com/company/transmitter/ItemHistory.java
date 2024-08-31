package com.company.transmitter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ItemHistory {
    static private int countOfOrders = 0;
    private final int image;
    private final String total;
    private final String timeAndDate;

    public ItemHistory(int image, String total, String timeAndDate) {
        this.image = image;
        this.total = total;
        this.timeAndDate = timeAndDate;
        ++countOfOrders;
    }

    public int getImage() {
        return image;
    }
    public String getTitle() {
        return "Order №";
    }
    public String getTotal() { return total; }
    public String getTimeAndDate() { return timeAndDate; }

    public static String toString(List<ItemHistory> list) {
        StringBuilder string = new StringBuilder();
        for (ItemHistory item : list) {
            string.append(item.getTimeAndDate() + "," + item.getTotal() + ","+ item.getImage() + "\n");
        }
        return string.toString();
    }

    public static ItemHistory parseLine(String line) {
        Log.d("str", line);
        String[] parts = line.split(",");
        String date = parts[0].trim();
        String total = parts[1].trim();
        int image = Integer.parseInt(parts[2].trim());
        return new ItemHistory(image, total, date);
    }

    public static List<ItemHistory> parser(String array) {
        List<ItemHistory> dataList = new ArrayList<>();
        String[] lines = array.split("\\r?\\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                dataList.add(parseLine(line));
            }
        }
        return dataList;
    }
}