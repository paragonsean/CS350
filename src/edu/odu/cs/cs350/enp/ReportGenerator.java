package edu.odu.cs.cs350.enp;


import java.util.*;
public class ReportGenerator {

    public void generateProjectionReport(List<Course> semesterData, Date preRegistrationStart, Date addDeadline, Date lastSnapshotDate) {
        double percentageElapsed = calculatePercentageElapsed(preRegistrationStart, addDeadline, lastSnapshotDate);
        Map<String, int[]> courseEnrollments = aggregateCourse(semesterData);
        List<String> reportLines = generateReportLines(courseEnrollments, percentageElapsed);
        printReport(percentageElapsed, reportLines);
    }

    private double calculatePercentageElapsed(Date preRegistrationStart, Date addDeadline, Date lastSnapshotDate) {
        long totalPeriod = addDeadline.getTime() - preRegistrationStart.getTime();
        long elapsedPeriod = lastSnapshotDate.getTime() - preRegistrationStart.getTime();
        return Math.min(Math.max(((double) elapsedPeriod / totalPeriod) * 100, 0), 100);
    }

    private Map<String, int[]> aggregateCourse(List<Course> semesterData) {
        Map<String, int[]> courseEnrollments = new HashMap<>();
        for (Course course : semesterData) {
            String courseName = course.getSUBJ() + course.getCRSE();
            int currentEnrollment = course.getENR();
            int cap = course.getOVERALL_CAP();

            courseEnrollments.putIfAbsent(courseName, new int[]{0, 0});
            courseEnrollments.get(courseName)[0] += currentEnrollment;
            courseEnrollments.get(courseName)[1] += cap;
        }
        return courseEnrollments;
    }

    private List<String> generateReportLines(Map<String, int[]> courseEnrollments, double percentageElapsed) {
        List<String> reportLines = new ArrayList<>();
        courseEnrollments.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String courseName = entry.getKey();
                    int currentEnrollment = entry.getValue()[0];
                    int totalCap = entry.getValue()[1];
                    int projectedEnrollment = calculateProjectedEnrollment(currentEnrollment, percentageElapsed);
                    char marker = projectedEnrollment > totalCap ? '*' : ' ';
                    reportLines.add(String.format("%c %-10s %-10d %-10d %-10d", marker, courseName, currentEnrollment, projectedEnrollment, totalCap));
                });
        return reportLines;
    }

    private int calculateProjectedEnrollment(int currentEnrollment, double percentageElapsed) {
        if (percentageElapsed == 0) {
            return currentEnrollment;
        }
        return (int) (currentEnrollment + ((100 - percentageElapsed) / percentageElapsed) * currentEnrollment);
    }

    private void printReport(double percentageElapsed, List<String> reportLines) {
        System.out.printf("%.0f%% of enrollment period has elapsed.%n", percentageElapsed);
        System.out.printf("%-10s %-10s %-10s %-10s%n", "Course", "Enrollment", "Projected", "Cap");
        reportLines.forEach(System.out::println);
    }
}
