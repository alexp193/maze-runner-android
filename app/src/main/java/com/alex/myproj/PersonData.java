package com.alex.myproj;

/**
 * Created by Alex on 08/06/2016.
 */


public class PersonData {
    private String person;      //person player
    private String level;       //level player
    private long timeRecord;  //record player

    public PersonData(String person, String level, long timeRecord) {
        this.person = person;
        this.level = level;
        this.timeRecord = timeRecord;
    }
    public String getPerson() {
        return person;
    }
    public void setPerson(String person) {
        this.person = person;
    }
    public String getLevel()
    {
        return level;
    }
    public void setLevel(String level)
    {
        this.level = level;
    }
    public long getTimeRecord()
    {
        return timeRecord;
    }
    public void setTimeRecord(int timeRecord)
    {
        this.timeRecord = timeRecord;
    }
}