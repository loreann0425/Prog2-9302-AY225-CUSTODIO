package JAVA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MiniDataAnalyticsConsoleDashboard {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<GameRecord> records = null;

        System.out.println("==============================================");
        System.out.println("      MINI DATA ANALYTICS CONSOLE DASHBOARD   ");
        System.out.println("==============================================");

        while (true) {
            System.out.print("Enter dataset file path: ");
            String path = input.nextLine().trim();

            // Remove surrounding quotes (common when copying from Windows)
            if ((path.startsWith("\"") && path.endsWith("\"")) ||
                (path.startsWith("'") && path.endsWith("'"))) {
                path = path.substring(1, path.length() - 1).trim();
            }

            // Validate path is not empty
            if (path.isEmpty()) {
                System.out.println("Error: File path cannot be empty. Please try again.\n");
                continue;
            }

            // Check CSV format BEFORE creating File object
            if (!path.toLowerCase().endsWith(".csv")) {
                System.out.println("Error: File is not in CSV format. Please provide a .csv file.\n");
                continue;
            }

            // Try to find the file in multiple locations
            File file = findFile(path);

            if (file == null) {
                System.out.println("Error: File '" + path + "' not found.\n");
                System.out.println("Tried locations:");
                System.out.println("  - " + new File(path).getAbsolutePath());
                if (!path.contains(File.separator)) {
                    System.out.println("  - " + new File("JAVA" + File.separator + path).getAbsolutePath());
                    System.out.println("  - " + new File(".." + File.separator + "JAVA" + File.separator + path).getAbsolutePath());
                }
                System.out.println("Please provide the correct file path.\n");
                continue;
            }

            if (!file.isFile()) {
                System.out.println("Error: The path is not a valid file. Please try again.\n");
                continue;
            }

            if (!file.canRead()) {
                System.out.println("Error: File cannot be read. Please check permissions.\n");
                continue;
            }

            try {
                records = loadDataset(file);

                if (records.isEmpty()) {
                    System.out.println("Error: CSV file is valid but contains no data rows.\n");
                    continue;
                }

                System.out.println("\nDataset loaded successfully!");
                System.out.println("Total records loaded: " + records.size());
                break;

            } catch (IllegalArgumentException e) {
                System.out.println("Error: Invalid CSV format - " + e.getMessage() + "\n");
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage() + "\n");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage() + "\n");
            }
        }

        while (true) {
            System.out.println("\n==============================================");
            System.out.println("                 MAIN MENU                    ");
            System.out.println("==============================================");
            System.out.println("1 - View Dataset Summary");
            System.out.println("2 - Monthly Sales");
            System.out.println("3 - Top Customers");
            System.out.println("4 - Category Analysis");
            System.out.println("5 - Exit");
            System.out.print("Enter your choice: ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    viewDatasetSummary(records);
                    break;
                case "2":
                    showMonthlySales(records);
                    break;
                case "3":
                    showTopCustomers(records);
                    break;
                case "4":
                    showCategoryAnalysis(records);
                    break;
                case "5":
                    System.out.println("Exiting program. Thank you!");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1 to 5.");
            }
        }
    }

    private static File findFile(String path) {
        // List of locations to search for the file
        String[] searchLocations = {
            path,                                           // As provided
            "JAVA" + File.separator + path,                // In JAVA subdirectory
            ".." + File.separator + "JAVA" + File.separator + path,  // In JAVA parent directory
            "." + File.separator + path                     // Current directory explicit
        };

        // Try each location
        for (String location : searchLocations) {
            File file = new File(location);
            if (file.exists() && file.isFile() && file.canRead()) {
                return file;
            }
        }

        // If not found in any location, return null
        return null;
    }

    public static List<GameRecord> loadDataset(File file) throws IOException {
        List<GameRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();

            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty.");
            }

            List<String> headers = parseCSVLine(headerLine);
            Map<String, Integer> headerMap = new HashMap<>();

            for (int i = 0; i < headers.size(); i++) {
                headerMap.put(headers.get(i).trim().toLowerCase(), i);
            }

            List<String> requiredHeaders = Arrays.asList(
                "img", "title", "console", "genre", "publisher", "developer",
                "critic_score", "total_sales", "na_sales", "jp_sales",
                "pal_sales", "other_sales", "release_date", "last_update"
            );

            for (String required : requiredHeaders) {
                if (!headerMap.containsKey(required)) {
                    throw new IllegalArgumentException("Missing required column: " + required);
                }
            }

            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> values = parseCSVLine(line);

                while (values.size() < headers.size()) {
                    values.add("");
                }

                try {
                    String img = getValue(values, headerMap, "img");
                    String title = getValue(values, headerMap, "title");
                    String console = getValue(values, headerMap, "console");
                    String genre = getValue(values, headerMap, "genre");
                    String publisher = getValue(values, headerMap, "publisher");
                    String developer = getValue(values, headerMap, "developer");
                    double criticScore = parseDoubleSafe(getValue(values, headerMap, "critic_score"));
                    double totalSales = parseDoubleSafe(getValue(values, headerMap, "total_sales"));
                    double naSales = parseDoubleSafe(getValue(values, headerMap, "na_sales"));
                    double jpSales = parseDoubleSafe(getValue(values, headerMap, "jp_sales"));
                    double palSales = parseDoubleSafe(getValue(values, headerMap, "pal_sales"));
                    double otherSales = parseDoubleSafe(getValue(values, headerMap, "other_sales"));
                    LocalDate releaseDate = parseDateSafe(getValue(values, headerMap, "release_date"));
                    String lastUpdate = getValue(values, headerMap, "last_update");

                    GameRecord record = new GameRecord(
                        img, title, console, genre, publisher, developer,
                        criticScore, totalSales, naSales, jpSales,
                        palSales, otherSales, releaseDate, lastUpdate
                    );

                    records.add(record);

                } catch (Exception e) {
                    System.out.println("Warning: Skipping invalid row at line " + lineNumber);
                }
            }
        }

        return records;
    }

    public static void viewDatasetSummary(List<GameRecord> records) {
        System.out.println("\n==============================================");
        System.out.println("              DATASET SUMMARY                 ");
        System.out.println("==============================================");

        int totalRecords = records.size();
        Set<String> consoles = new HashSet<>();
        Set<String> genres = new HashSet<>();
        double totalSales = 0.0;
        double totalCriticScore = 0.0;
        int criticCount = 0;

        LocalDate earliestDate = null;
        LocalDate latestDate = null;

        for (GameRecord record : records) {
            consoles.add(record.getConsole());
            genres.add(record.getGenre());
            totalSales += record.getTotalSales();

            if (record.getCriticScore() > 0) {
                totalCriticScore += record.getCriticScore();
                criticCount++;
            }

            if (record.getReleaseDate() != null) {
                if (earliestDate == null || record.getReleaseDate().isBefore(earliestDate)) {
                    earliestDate = record.getReleaseDate();
                }
                if (latestDate == null || record.getReleaseDate().isAfter(latestDate)) {
                    latestDate = record.getReleaseDate();
                }
            }
        }

        double averageCriticScore = criticCount > 0 ? totalCriticScore / criticCount : 0.0;

        System.out.printf("Total Records        : %d%n", totalRecords);
        System.out.printf("Unique Consoles      : %d%n", consoles.size());
        System.out.printf("Unique Genres        : %d%n", genres.size());
        System.out.printf("Total Global Sales   : %.2f million%n", totalSales);
        System.out.printf("Average Critic Score : %.2f%n", averageCriticScore);

        if (earliestDate != null && latestDate != null) {
            System.out.printf("Date Range           : %s to %s%n", earliestDate, latestDate);
        } else {
            System.out.println("Date Range           : No valid release dates found");
        }
    }

    public static void showMonthlySales(List<GameRecord> records) {
        System.out.println("\n==============================================");
        System.out.println("                MONTHLY SALES                 ");
        System.out.println("==============================================");

        Map<YearMonth, Double> monthlySales = new HashMap<>();

        for (GameRecord record : records) {
            if (record.getReleaseDate() != null) {
                YearMonth ym = YearMonth.from(record.getReleaseDate());
                monthlySales.put(ym, monthlySales.getOrDefault(ym, 0.0) + record.getTotalSales());
            }
        }

        if (monthlySales.isEmpty()) {
            System.out.println("No valid release dates found for monthly analysis.");
            return;
        }

        List<YearMonth> sortedMonths = new ArrayList<>(monthlySales.keySet());
        Collections.sort(sortedMonths);

        System.out.printf("%-15s %-15s%n", "Month", "Total Sales");
        System.out.println("----------------------------------------------");

        for (YearMonth ym : sortedMonths) {
            System.out.printf("%-15s %-15.2f%n", ym, monthlySales.get(ym));
        }
    }

    public static void showTopCustomers(List<GameRecord> records) {
        System.out.println("\n==============================================");
        System.out.println("               TOP CUSTOMERS                  ");
        System.out.println("==============================================");
        System.out.println("Note: This dataset has no customer column.");
        System.out.println("Showing Top-Selling Titles instead.\n");

        List<GameRecord> sortedRecords = new ArrayList<>(records);
        sortedRecords.sort(Comparator.comparingDouble(GameRecord::getTotalSales).reversed());

        System.out.printf("%-5s %-35s %-20s %-12s%n", "Rank", "Title", "Publisher", "Sales");
        System.out.println("--------------------------------------------------------------------------");

        int limit = Math.min(10, sortedRecords.size());

        for (int i = 0; i < limit; i++) {
            GameRecord record = sortedRecords.get(i);
            System.out.printf("%-5d %-35s %-20s %-12.2f%n",
                    i + 1,
                    shortenText(record.getTitle(), 34),
                    shortenText(record.getPublisher(), 19),
                    record.getTotalSales());
        }
    }

    public static void showCategoryAnalysis(List<GameRecord> records) {
        System.out.println("\n==============================================");
        System.out.println("              CATEGORY ANALYSIS               ");
        System.out.println("==============================================");

        Map<String, Integer> genreCount = new HashMap<>();
        Map<String, Double> genreSales = new HashMap<>();
        Map<String, Double> genreCriticTotal = new HashMap<>();
        Map<String, Integer> genreCriticCount = new HashMap<>();

        for (GameRecord record : records) {
            String genre = record.getGenre();

            genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
            genreSales.put(genre, genreSales.getOrDefault(genre, 0.0) + record.getTotalSales());

            if (record.getCriticScore() > 0) {
                genreCriticTotal.put(genre, genreCriticTotal.getOrDefault(genre, 0.0) + record.getCriticScore());
                genreCriticCount.put(genre, genreCriticCount.getOrDefault(genre, 0) + 1);
            }
        }

        List<Map.Entry<String, Double>> sortedGenres = new ArrayList<>(genreSales.entrySet());
        sortedGenres.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        System.out.printf("%-20s %-10s %-15s %-15s%n", "Genre", "Count", "Total Sales", "Avg Critic");
        System.out.println("----------------------------------------------------------------");

        for (Map.Entry<String, Double> entry : sortedGenres) {
            String genre = entry.getKey();
            int count = genreCount.getOrDefault(genre, 0);
            double sales = genreSales.getOrDefault(genre, 0.0);

            double avgCritic = 0.0;
            if (genreCriticCount.getOrDefault(genre, 0) > 0) {
                avgCritic = genreCriticTotal.get(genre) / genreCriticCount.get(genre);
            }

            System.out.printf("%-20s %-10d %-15.2f %-15.2f%n",
                    shortenText(genre, 19), count, sales, avgCritic);
        }
    }

    private static String getValue(List<String> values, Map<String, Integer> headerMap, String column) {
        Integer index = headerMap.get(column.toLowerCase());
        if (index == null || index >= values.size()) {
            return "";
        }
        return values.get(index).trim();
    }

    private static double parseDoubleSafe(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return 0.0;
            }
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static LocalDate parseDateSafe(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                // Toggle quote state but don't add the quote to output
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                // Field separator found (only outside quotes)
                result.add(current.toString().trim());
                current.setLength(0);
            } else {
                // Regular character
                current.append(ch);
            }
        }

        // Add the last field
        result.add(current.toString().trim());
        return result;
    }

    private static String shortenText(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}