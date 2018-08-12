package rendserve.rendserveltd.Pojo;

import java.util.ArrayList;

/**
 * Created by abhi on 19-03-2018.
 */

public class ReportDataPojo {

    private String id;
    private String user_id;
    private String project_id;
    private String startdate;
    private String enddate;
    private String state;
    private String address;
    private String temperature;
    private String pressure;
    private String humidity;
    /*private ReportDetailPojo delayscausedbyweather;
    private ReportDetailPojo workperformtoday;
    private ReportDetailPojo subconstractorprogress;
    private ReportDetailPojo accidentonsite;
    private ReportDetailPojo extraworkrequire;
    private ReportDetailPojo materialpurchase;
    private ReportDetailPojo qualitycontrol;
    private ReportDetailPojo safetyobservations;
    private ReportDetailPojo scheduledelays;
    private ReportDetailPojo whatelse;*/
    private String file;
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    private boolean isEdit;

}
