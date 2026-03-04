package JAVA;

import java.time.LocalDate;

public class GameRecord {
    private String img;
    private String title;
    private String console;
    private String genre;
    private String publisher;
    private String developer;
    private double criticScore;
    private double totalSales;
    private double naSales;
    private double jpSales;
    private double palSales;
    private double otherSales;
    private LocalDate releaseDate;
    private String lastUpdate;

    public GameRecord(String img, String title, String console, String genre,
                      String publisher, String developer, double criticScore,
                      double totalSales, double naSales, double jpSales,
                      double palSales, double otherSales, LocalDate releaseDate,
                      String lastUpdate) {
        this.img = img;
        this.title = title;
        this.console = console;
        this.genre = genre;
        this.publisher = publisher;
        this.developer = developer;
        this.criticScore = criticScore;
        this.totalSales = totalSales;
        this.naSales = naSales;
        this.jpSales = jpSales;
        this.palSales = palSales;
        this.otherSales = otherSales;
        this.releaseDate = releaseDate;
        this.lastUpdate = lastUpdate;
    }

    public String getImg() { return img; }
    public String getTitle() { return title; }
    public String getConsole() { return console; }
    public String getGenre() { return genre; }
    public String getPublisher() { return publisher; }
    public String getDeveloper() { return developer; }
    public double getCriticScore() { return criticScore; }
    public double getTotalSales() { return totalSales; }
    public double getNaSales() { return naSales; }
    public double getJpSales() { return jpSales; }
    public double getPalSales() { return palSales; }
    public double getOtherSales() { return otherSales; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public String getLastUpdate() { return lastUpdate; }
}