package info.hartmann.interview.salarystats;

public class Main {
    public static void main(String... args) {
        Data data = new Data();
        data.printAverageSalary();
        data.printHighestSalary();
        data.printLowestSalary();
    }
}