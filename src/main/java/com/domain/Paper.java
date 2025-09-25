package com.domain;

import java.util.Objects;

public class Paper {

    private String paperCode;

    public Paper(){

    }

    public Paper(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    @Override
    public int hashCode() {
        int hash = 2;
        hash = hash * 31 + Objects.hashCode(this.paperCode);
        return hash;
    }
}
