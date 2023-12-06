package com.resume.bot.json.entity.metro;

import java.util.List;

@SuppressWarnings("unused")
public class Metro {

    private String id;
    private List<Line> lines;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
