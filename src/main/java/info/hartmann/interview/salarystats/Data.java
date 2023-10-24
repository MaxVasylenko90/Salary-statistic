package info.hartmann.interview.salarystats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Data {
    private Map<String, Integer> salariesStats;
    private final String[] HEADERS = {"employeeId", "firstName", "lastName", "email", "phone", "hireDate", "jobId",
            "salary", "comissionPct", "managerId", "departmentId"};

    public Data() {
        this.salariesStats = new HashMap<>();
        readRequiredDataFromFileAndPutToMap();
    }

    public void readRequiredDataFromFileAndPutToMap() {
        try (Reader in = new FileReader("input/salaries.csv")) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true)
                    .build();

            Iterable<CSVRecord> records = csvFormat.parse(in);
            StringBuilder sb = new StringBuilder();

            for (CSVRecord record : records) {
                int salary = Integer.parseInt(record.get("salary"));
                sb.append(record.get("firstName"));
                sb.append(" ");
                sb.append(record.get("lastName"));
                String fullName = sb.toString();
                sb.setLength(0);
                salariesStats.put(fullName, salary);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getSalariesStats() {
        return salariesStats;
    }

    public void setSalariesStats(Map<String, Integer> salariesStats) {
        this.salariesStats = salariesStats;
    }

    public String[] getHEADERS() {
        return HEADERS;
    }


    public void printAverageSalary() {
        AtomicInteger totalSalary = new AtomicInteger();
        salariesStats.forEach((k, v) -> totalSalary.addAndGet(v));
        System.out.println("The average salary : " + totalSalary.get() / salariesStats.size());
    }

    public void printHighestSalary() {
        Map<String, Integer> result = traverseAndFindResultMap("highest");
        result.forEach((k, v) -> System.out.println("The highest salary: " + v + ", owner : " + k));
    }

    public void printLowestSalary() {
        Map<String, Integer> result = traverseAndFindResultMap("lowest");
        result.forEach((k, v) -> System.out.println("The lowest salary: " + v + ", owner : " + k));
    }

    private Map<String, Integer> traverseAndFindResultMap(String searchingParam) {
        if (searchingParam == null)
            throw new RuntimeException("traversTheMap() can't be null");

        if (searchingParam.equals("lowest") || searchingParam.equals("highest")) {
            Map<String, Integer> result = new HashMap<>();
            String owner = null;
            int requiredSalary = 0;
            if (searchingParam.equals("lowest"))
                requiredSalary = Integer.MAX_VALUE;

            for (Map.Entry<String, Integer> entry : salariesStats.entrySet()) {
                String tmpOwner = entry.getKey();
                int salary = entry.getValue();
                if (searchingParam.equals("lowest")) {
                    if (salary < requiredSalary) {
                        requiredSalary = salary;
                        owner = tmpOwner;
                    }
                } else {
                    if (salary > requiredSalary) {
                        requiredSalary = salary;
                        owner = tmpOwner;
                    }
                }
            }
            result.put(owner, requiredSalary);
            return result;
        } else
            throw new RuntimeException("only \"lowest\" or \"highest\" argument can be passed to traversTheMap() method");
    }
}
