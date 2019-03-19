/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysql.to.pkgdo.list;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author shamin
 */
public class Time {

    public Time() {
    }
    

    public ObservableList set12Values() {
        List<String> list= new ArrayList();
        list.add("12 AM");
        list.add("01 AM");
        list.add("02 AM");
        list.add("03 AM");
        list.add("04 AM");
        list.add("05 AM");
        list.add("06 AM");
        list.add("07 AM");
        list.add("08 AM");
        list.add("09 AM");
        list.add("10 AM");
        list.add("11 AM");
        list.add("12 PM");
        list.add("01 PM");
        list.add("02 PM");
        list.add("03 PM");
        list.add("04 PM");
        list.add("05 PM");
        list.add("06 PM");
        list.add("07 PM");
        list.add("08 PM");
        list.add("09 PM");
        list.add("10 PM");
        list.add("11 PM");

        ObservableList observableList= FXCollections.observableList(list);
        
        return observableList;
    }
    
    public ObservableList set24Values() {
        List<String> list= new ArrayList();
        list.add("00");
        list.add("01");
        list.add("02");
        list.add("03");
        list.add("04");
        list.add("05");
        list.add("06");
        list.add("07");
        list.add("08");
        list.add("09");
        list.add("10");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        list.add("16");
        list.add("17");
        list.add("18");
        list.add("19");
        list.add("20");
        list.add("21");
        list.add("22");
        list.add("23");
        ObservableList observableList= FXCollections.observableList(list);
        
        return observableList;
    }
    
    public ObservableList setMinutes(){
        List<String> list= new ArrayList();
        list.add("00");
        list.add("01");
        list.add("02");
        list.add("03");
        list.add("04");
        list.add("05");
        list.add("06");
        list.add("07");
        list.add("08");
        list.add("09");
        list.add("10");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        list.add("16");
        list.add("17");
        list.add("18");
        list.add("19");
        list.add("20");
        list.add("21");
        list.add("22");
        list.add("23");
        list.add("24");
        list.add("25");
        list.add("26");
        list.add("27");
        list.add("28");
        list.add("29");
        list.add("30");
        list.add("31");
        list.add("32");
        list.add("33");
        list.add("34");
        list.add("35");
        list.add("36");
        list.add("37");
        list.add("38");
        list.add("39");
        list.add("40");
        list.add("41");
        list.add("42");
        list.add("43");
        list.add("44");
        list.add("45");
        list.add("46");
        list.add("47");
        list.add("48");
        list.add("49");
        list.add("50");
        list.add("51");
        list.add("52");
        list.add("53");
        list.add("54");
        list.add("55");
        list.add("56");
        list.add("57");
        list.add("58");
        list.add("59");
        
        ObservableList observableList= FXCollections.observableList(list);
        return observableList;
    }
    
    
}
