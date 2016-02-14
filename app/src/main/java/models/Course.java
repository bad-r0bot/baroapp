package models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jim on 14/02/2016.
 */
public class Course {

    private int id;
    private String courseName;
    private int period;
    private int ects;

    public Course(int id, String courseName, int period, int ects){

        this.id = id;
        this.courseName = courseName;
        this.period = period;
        this.ects = ects;

    }
    public Course(JSONObject course){
        this.id = 0;
        try {
            this.courseName = course.getString("name");
            this.period = course.getInt("period");
            this.ects = course.getInt("ects");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }
}
