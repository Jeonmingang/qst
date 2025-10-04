package com.minkang.uaq.model;

public class Quest {
    public String id;
    public String name;
    public QuestType type = QuestType.GENERIC_COUNTER;
    public int goal = 1;
    public boolean daily = false;
}
