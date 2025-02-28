package base;

import java.util.ArrayList;
import java.util.List;

public class ResultData {

    private String name;
    private String surname;
    private List<ResultTestData> results;
    private double averageExecutionTime;

    public ResultData(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void addResult(ResultTestData result) {
        if (results == null) {
            results = new ArrayList<>();
        }
        results.add(result);
    }

    public void setAverageExecutionTime() {
        double average = 0;
        if (results != null && !results.isEmpty()) {
            average = results.stream().mapToLong(ResultTestData::getExecutionTime).average().orElse(0);
        }
        this.averageExecutionTime = average;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<ResultTestData> getResults() {
        return results;
    }

    public double getAverageExecutionTime() {
        return averageExecutionTime;
    }

    static class ResultTestData {

        private final long executionTime;
        private int steps;
        private String error;

        public ResultTestData(long executionTime) {
            this.executionTime = executionTime;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        public int getSteps() {
            return steps;
        }

        public String getError() {
            return error;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
