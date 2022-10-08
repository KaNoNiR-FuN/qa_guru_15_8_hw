package model;

import java.util.List;

public class Champion {

    public String name;
    public String type;
    public int price;
    public List<String> skills;
    public Champion.Stats stats;

    public static class Stats {
        public int health;
        public int armor;
        public int damage;
    }
}
