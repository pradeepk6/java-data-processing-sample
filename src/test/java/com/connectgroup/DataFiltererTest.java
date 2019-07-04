package com.connectgroup;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataFiltererTest {
    @Test
    public void shouldReturnEmptyCollection_WhenLogFileIsEmpty() throws FileNotFoundException {
        assertTrue(DataFilterer.filterByCountry(openFile("src/test/resources/empty"), "GB").isEmpty());
    }
    @Test
    public void shouldReturnNonEmptyCollection_WhenLogFileIsMultiLines() throws FileNotFoundException {
        assertTrue(DataFilterer.filterByCountry(openFile("src/test/resources/multi-lines"), "US").size()==3);
    }
    @Test
    public void shouldReturnNonEmptyCollection_WhenLogFileIsSingleLine() throws FileNotFoundException {
        assertTrue(DataFilterer.filterByCountry(openFile("src/test/resources/single-line"), "GB").size()==1);
    }
    @Test
    public void shouldReturnNonEmptyCollection_WhenResponseTimeAboveLimit() throws FileNotFoundException {
        assertEquals(2,DataFilterer.filterByCountryWithResponseTimeAboveLimit(openFile("src/test/resources/multi-lines"), "US",700).size());
    }

    @Test
    public void shouldReturnNonEmptyCollection_WhenResponseTimeAboveAverage() throws FileNotFoundException {
        assertEquals(3,DataFilterer.filterByResponseTimeAboveAverage(openFile("src/test/resources/multi-lines")).size());
    }

    private FileReader openFile(String filename) throws FileNotFoundException {
        return new FileReader(new File(filename));
    }
}
