package com.example.dervis.theguardian;

class Article {
    private final String Section;
    private final String Title;
    private final String Author;
    private final String Date;
    private final String webUrl;

    Article(String section, String title, String author, String date, String webUrl) {
        Section = section;
        Title = title;
        Author = author;
        Date = date;
        this.webUrl = webUrl;
    }

    public String getSection() {
        return Section;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDate() {
        return Date;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
