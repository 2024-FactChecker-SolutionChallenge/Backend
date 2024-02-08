package com.solutionchallenge.factchecker.api.Member.entity;

// 1 : "POLITICS"
// 2 : "ECONOMY"
// 3 : "SOCIETY"
// 4 : "GLOBAL"
// 5 : "IT/SCIENCE"
// 6 : "CULTURE"
public enum Interests {
    POLITICS("politics"),
    ECONOMY("economy"),
    SOCIETY("society"),
    GLOBAL("global"),
    ITSCIENCE("it/science"),
    CULTURE("culture");

    private final String interests;

    Interests(String interests) {
        this.interests = interests;
    }

    public String getInterests(){return interests; }

    public static Interests getInterests(String interests){
        for (Interests interests1 : Interests.values()){
            if (interests1.interests.equalsIgnoreCase(interests)){
                return interests1;
            }
        }
        return null;
    }
}
