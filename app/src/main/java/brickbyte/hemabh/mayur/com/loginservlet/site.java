package brickbyte.hemabh.mayur.com.loginservlet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayursanjaydevgaonkar on 01/07/15.
 */
public class site {



    String siteName=null;
    String siteLocation=null;
    String siteID=null;

    //site parameters

    String oil_pressure=null;
    String coolant_temperature=null;
    String fuel_level=null;
    String dg1_battery_voltage=null;
    String coolant_pressure1=null;
    String inlet_manfold_temp1=null;
    String engine_run_time=null;
    String dg_kvah=null;
    String number_of_starts=null;
    String fuel_used=null;
    String time_of_reading=null;

    String[] siteParams=null;





    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String[] getSiteParams() {
        return siteParams;



    }


    public void setSiteParams(String[] siteParams) {
        this.siteParams = siteParams;



    }

    public String getOil_pressure() {
        return oil_pressure;
    }

    public void setOil_pressure(String oil_pressure) {
        this.oil_pressure = oil_pressure;
    }

    public String getCoolant_temperature() {
        return coolant_temperature;
    }

    public void setCoolant_temperature(String coolant_temperature) {
        this.coolant_temperature = coolant_temperature;
    }

    public String getFuel_level() {
        return fuel_level;
    }

    public void setFuel_level(String fuel_level) {
        this.fuel_level = fuel_level;
    }

    public String getDg1_battery_voltage() {
        return dg1_battery_voltage;
    }

    public void setDg1_battery_voltage(String dg1_battery_voltage) {
        this.dg1_battery_voltage = dg1_battery_voltage;
    }

    public String getCoolant_pressure1() {
        return coolant_pressure1;
    }

    public void setCoolant_pressure1(String coolant_pressure1) {
        this.coolant_pressure1 = coolant_pressure1;
    }

    public String getInlet_manfold_temp1() {
        return inlet_manfold_temp1;
    }

    public void setInlet_manfold_temp1(String inlet_manfold_temp1) {
        this.inlet_manfold_temp1 = inlet_manfold_temp1;
    }

    public String getEngine_run_time() {
        return engine_run_time;
    }

    public void setEngine_run_time(String engine_run_time) {
        this.engine_run_time = engine_run_time;
    }

    public String getDg_kvah() {
        return dg_kvah;
    }

    public void setDg_kvah(String dg_kvah) {
        this.dg_kvah = dg_kvah;
    }

    public String getNumber_of_starts() {
        return number_of_starts;
    }

    public void setNumber_of_starts(String number_of_starts) {
        this.number_of_starts = number_of_starts;
    }

    public String getTime_of_reading() {
        return time_of_reading;
    }

    public void setTime_of_reading(String time_of_reading) {
        this.time_of_reading = time_of_reading;
    }

    public String getFuel_used() {
        return fuel_used;
    }

    public void setFuel_used(String fuel_used) {
        this.fuel_used = fuel_used;
    }
}
