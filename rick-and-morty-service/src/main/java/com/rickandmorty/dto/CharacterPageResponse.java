package com.rickandmorty.dto;

import java.util.ArrayList;
import java.util.List;

public class CharacterPageResponse {

    private Info info;
    private List<CharacterApiDto> results;

    public CharacterPageResponse() {
    }

    public CharacterPageResponse(Info info, List<CharacterApiDto> results) {
        this.info = info;
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<CharacterApiDto> getResults() {
        if (results == null) {
            results = new ArrayList<>();
        }
        return results;
    }

    public void setResults(List<CharacterApiDto> results) {
        this.results = results;
    }

    public static class Info {

        private int count;
        private int pages;
        private String next;
        private String prev;

        public Info() {
        }

        public Info(int count, int pages, String next, String prev) {
            this.count = count;
            this.pages = pages;
            this.next = next;
            this.prev = prev;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }
    }
}
