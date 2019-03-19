/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysql.to.pkgdo.list;

/**
 *
 * @author shamin
 */
public class Work {
    private String date;
    private String time;
    private String header;
    private String description;
    private String location;

    public Work(String date, String time, String header, String description, String location) {
        this.date = date;
        this.time = time;
        this.header = header;
        this.description = description;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "at " + time;
    }
    
    public String workDescription(){
        return "Date: " + date + '\n' + "Time: " + time + '\n'  + "Header: " + header + '\n' +
                "Task Description: " + description + '\n'  + "Location: " + location + '\n' ;
    }
    
    
}
