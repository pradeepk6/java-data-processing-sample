package com.connectgroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataFilterer {

    public static Collection<?> filterByCountry(Reader source, String country) {
        List list = Collections.emptyList();
        try (BufferedReader br = new BufferedReader(source)) {
            list = br.lines()
                    .skip(1)
                    .map(DataFilterer::validateLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
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
                    .map(DataFilterer::validateLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
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
            List<String> dataList = br.lines()
                    .skip(1)
                    .map(DataFilterer::validateLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            double avg = dataList.stream()
                    .mapToLong(line -> Long.parseLong(line.split(",")[2]))
                    .average().orElseThrow(() -> new IllegalArgumentException("Error calculating average"));

            list = dataList.stream()
                    .filter(line -> Long.parseLong(line.split(",")[2]) > avg)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // if line is invalid return null
    private static Optional<String> validateLine(String line) {
        try {
            String[] tokens = line.split(",");
            if (tokens.length < 3) throw new Exception("Num of cols is less than 3 for line : " + line);
            Long.parseLong(tokens[2]); // make sure col2 is a number
        } catch (Exception e) {
            System.out.println("Invalid line : " + line + " : " + e.getMessage());
            line = null;
        }
        return Optional.ofNullable(line);
    }

}