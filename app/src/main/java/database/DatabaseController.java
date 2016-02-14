package database;

/**
 * Created by Jim on 14/02/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Course;
import models.User;

public class DatabaseController extends SQLiteOpenHelper{

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "barometer";
        private static final String TABLE_USERS = "users";
        private static final String TABLE_COURSES = "courses";
        private List<GregorianCalendar> periods;

        public DatabaseController(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            initPeriods();
        }

        private void initPeriods() {
            periods = new ArrayList<>();
            //Dates apply only to 2015-2016
            periods.add(new GregorianCalendar(2015, 11, 9));
            periods.add(new GregorianCalendar(2016, 2, 1));
            periods.add(new GregorianCalendar(2016, 4, 18));
            periods.add(new GregorianCalendar(2016, 7, 11));
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createCourseTable(db);
            createUserTable(db);
            createAttendanceTable(db);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
            db.execSQL("DROP TABLE IF EXISTS attendances;");
            onCreate(db);
        }

        private void createUserTable(SQLiteDatabase db) {
            String CREATE_USERS_TABLE = "CREATE TABLE users(user_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(25));";
            db.execSQL(CREATE_USERS_TABLE);
        }

        private void createCourseTable(SQLiteDatabase db) {

            String CREATE_COURSE_TABLE = "CREATE TABLE courses(course_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name VARCHAR(25),period INTEGER NOT NULL, ects INTEGER NOT NULL);";

            db.execSQL(CREATE_COURSE_TABLE);
        }

        private void createAttendanceTable(SQLiteDatabase db){
            String CREATE_ATTENDANCE_TABLE = "CREATE TABLE attendances(course_id INTEGER NOT NULL REFERENCES courses(course_id)," +
                    " user_id INTEGER NOT NULL REFERENCES user(user_id)," +
                    " grade DECIMAL(2,1));";
            db.execSQL(CREATE_ATTENDANCE_TABLE);
        }

        public User getUser(int id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE user_id = " + id + ";", null);
            try {
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                User user = new User(cursor.getInt(0), cursor.getString(1));
                db.close();
                return user;
            } catch(Exception e) {
                db.close();
                return null;
            }
        }

        public User getUserByName(String name){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE name = '" + name + "';", null);
            try {
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                User user = new User(cursor.getInt(0), cursor.getString(1));
                db.close();
                return user;
            } catch(Exception e) {
                db.close();
                return null;
            }
        }

        public void storeUser(User user) {
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement("INSERT INTO users(name)" +
                    "VALUES(?)");
            stmt.bindString(1, user.getName());
            stmt.execute();
            db.close();
            //initAttendance(user);
        }

        public void initAttendance(User user){
            for(Course course: getAllCourses()){
                double grade = 1.0;
                storeAttendance(user, course, Double.valueOf(grade));
            }
        }

        public void storeCourse(Course course) {
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement("INSERT INTO courses( name, period, ects)" +
                    "VALUES(?,?,?)");
            stmt.bindString(1, course.getCourseName());
            stmt.bindLong(2, course.getPeriod());
            stmt.bindLong(3, course.getEcts());
            stmt.execute();
            db.close();
        }

        public void truncateCourses(){
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement("DELETE FROM courses; VACUUM;");
            stmt.execute();
            db.close();
        }

        public Course getCourse(int id) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM courses where id = " + id + ";", null);
            if(cursor.moveToFirst()) {
                return new Course(cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));
            } else {
                return null;
            }

        }

        public Map<Course, Double > getCoursesForUser(User u) {
            SQLiteDatabase db = this.getReadableDatabase();

            Map<Course, Double> grades = new HashMap<Course, Double>();

            Cursor cursor = db.rawQuery("SELECT * FROM attendances WHERE user_id = "+u.getId()+";", null);
            List<Course> courses = new ArrayList<Course>();

            while(cursor.moveToNext()){
                grades.put(getCourse(cursor.getInt(1)), Double.valueOf(cursor.getDouble(2)));
            }
            return grades;
        }


        public List<Course> getAllCourses() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM courses;", null);
            List<Course> courses = new ArrayList<Course>();

            while(cursor.moveToNext()){
                courses.add(new Course(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
            }
            return courses;
        }

        public void storeAttendance(User user, Course course, Double grade){
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement("INSERT INTO attendances( user_id, course_id, grade)" +
                    "VALUES(?,?,?)");
            stmt.bindLong(1, user.getId());
            stmt.bindLong(2, course.getId());
            stmt.bindDouble(3, grade.doubleValue());
            stmt.execute();
            db.close();
        }

        public Double getGrade(User user, Course course) {
            Map<Course,Double> grades = getCoursesForUser(user);
            return grades.get(course);
        }

        public boolean hasAttended(User user, Course course){
            Map<Course,Double> grades = getCoursesForUser(user);
            return grades.containsKey(course);
        }

        /// NO FURTHER
    }

