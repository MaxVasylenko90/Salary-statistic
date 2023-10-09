package info.hartmann.interview.salarystats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {
    private Map<String, Integer> salariesStats = new HashMap<>() {
        {
            put("Donald OConnell", 2600);
            put("Shelley Higgins", 12008);
        }
    };
    private String[] HEADERS = {"employeeId", "firstName", "lastName", "email", "phone", "hireDate", "jobId",
            "salary", "comissionPct", "managerId", "departmentId"};

    @Test
    void givenCSVFileWhenReadThenSalaryAsExpected() throws IOException {
        Reader in = new FileReader("input/salaries.csv");
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build();
        Iterable<CSVRecord> records = csvFormat.parse(in);
        StringBuilder sb = new StringBuilder();
        for (CSVRecord record : records) {
            if (record.get("firstName").equals("Donald") && record.get("lastName").equals("OConnell")) {
                int salary = Integer.parseInt(record.get("salary"));
                sb.append(record.get("firstName"));
                sb.append(" ");
                sb.append(record.get("lastName"));
                String fullName = sb.toString();
                assertEquals(salariesStats.get(fullName), salary);
            }
        }
    }

    @Test
    void throwFileNotFoundExceptionIfFileWasNotFound() {
        assertThrows(FileNotFoundException.class, () -> new FileReader("input/salary.csv"));
    }

    @Test
    void throwIOExceptionIfHaveSomeInputOutputIssues() {
        assertThrows(IOException.class, () -> CSVFormat.DEFAULT.parse(new FileReader("input/salaris.csv")));
    }

    @Test
    void salariesStatsSizeIfSomeoneAdded() {
        salariesStats.put("John Doe", 20000);
        assertEquals(3, salariesStats.size());
    }
}
