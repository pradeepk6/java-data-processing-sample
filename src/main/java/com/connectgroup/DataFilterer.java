package com.connectgroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataFilterer {

    public static Collection<?> filterByCountry(Reader source, String country) {
        List list = Collections.emptyList();
        try (BufferedReader br = new BufferedReader(source)) {
            list = br.lines()
                    .skip(1)
                    .filter(line -> {
                        String[] tokens = line.split(",");
                         return (tokens[1].equalsIgnoreCase(country));
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Collection<?> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) {
        List list = Collections.emptyList();
        try (BufferedReader br = new BufferedReader(source)) {
            list = br.lines()
                     .skip(1)
                     .filter(line -> {
                        String[] tokens = line.split(",");
                        return ( tokens[1].equalsIgnoreCase(country) && Long.parseLong(tokens[2])>limit );
                      })
                     .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Collection<?> filterByResponseTimeAboveAverage(Reader source) {
        List list = Collections.emptyList();
        try( BufferedReader br = new BufferedReader(source)) {
            List<String> dataList = br.lines().skip(1).collect(Collectors.toList());
            double avg = dataList.stream()
                                 .mapToLong(line -> {
                                    String[] tokens = line.split(",");
                                    return Long.decode(tokens[2]);
                                 })
                                .average().getAsDouble();
            list = dataList.stream()
                    .filter(line -> {
                        String[] tokens = line.split(",");
                        return Long.parseLong(tokens[2]) > avg;
                     })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}