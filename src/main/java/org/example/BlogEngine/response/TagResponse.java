package org.example.BlogEngine.response;

public class TagResponse {
    private final String name;
    private final double wight;

    public String getName() {
        return name;
    }

    public double getWight() {
        return wight;
    }

    public TagResponse(String name, double wight) {
        this.name = name;
        this.wight = wight;
    }
}
