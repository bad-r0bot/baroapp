package database;

/**
 * Created by Jim on 14/02/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Course;
import models.User;

public class DatabaseController extends SQLiteOpenHelper{

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "barometer_db6";
        private static final String TABLE_USERS = "users";
        private static final String TABLE_COURSES = "courses";
        private List<GregorianCalendar> periods;
    private Context context;

        public DatabaseController(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            initPeriods();
            this.context = context;
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

        public boolean attendenceIsInitialisedForUser(User user){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM attendances WHERE user_id = " + user.getId() + ";", null);
            int i = 0;

            while(cursor.moveToNext()){
               i++;
            }
            return i > 0;
        }

        public void initAttendance(User user){
            for(Course course: getAllCourses()){
                double grade = 0.0;
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
            SQLiteStatement stmt1 = db.compileStatement("DELETE FROM courses; VACUUM;");
            SQLiteStatement stmt2 = db.compileStatement("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='courses';");

            try {
                stmt1.execute();

                stmt2.execute();
            }
            catch(Exception e){
                throw new RuntimeException(e.getMessage());
            }
            db.close();
        }
/*
        public void getGrade(String name){
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement("SELECT grade FROM course WHERE name = '" + name + "';", null);

            //return ;
        }

        public void setGrade(Double grade){
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement();
        }
*/
        public Course getCourse(int id) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM courses where course_id = " + id + ";", null);
            if(cursor.moveToFirst()) {
                return new Course(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3                                     ));
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


        public Course getCourseByName(String name){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM courses WHERE name = '" + name + "';", null);
            try {
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                else{
                    return null;
                }
                Course course = new Course(cursor.getInt(0), cursor.getString(1), cursor.getInt(3), cursor.getInt(2));
                db.close();
                return course;
            } catch(Exception e) {
                db.close();
                return null;
            }
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

        public void updateAttendance(User user, Course course, Double grade){
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement("UPDATE attendances SET grade=?" +
                    "WHERE user_id=? AND course_id=?");
            stmt.bindDouble(1, grade.doubleValue());
            stmt.bindLong(2, user.getId());
            stmt.bindLong(3, course.getId());
            stmt.execute();
            db.close();
        }

        public Double getGrade(User user, Course course) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM attendances WHERE user_id = " + user.getId() + " AND course_id = " + course.getId() + ";", null);
            try {
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                Double grade = cursor.getDouble(2);
                db.close();
                if(grade==null){
                    return null;
                }
                return grade;
            } catch(Exception e) {
                db.close();
                throw new RuntimeException("FOR COURSE: "+course.getId()+"\n"+e.getMessage());
                //return null;
            }

        }

        public boolean hasAttended(User user, Course course){
            Map<Course,Double> grades = getCoursesForUser(user);
            return grades.containsKey(course);
        }

        public int getMissedEcts(User user){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM attendances WHERE user_id = " + user.getId() + " AND grade = 0.0 ;", null);

            int total = 0;

            while(cursor.moveToNext()){
                Course course = this.getCourse(cursor.getInt(0));
                if(course.getPeriod() < getCurrentPeriod()) {
                    total += this.getCourse(cursor.getInt(0)).getEcts();
                }
            }
            return total;
        }

        public int getFailedEcts(User user){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM attendances WHERE user_id = " + user.getId() + " AND grade BETWEEN 1.0 AND 5.4 ;", null);

            int total = 0;

            while(cursor.moveToNext()){
                total += this.getCourse(cursor.getInt(0)).getEcts();
            }
            return total;
        }

        public int getTotalEcts(User user){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM attendances WHERE user_id = " + user.getId() + " AND grade > 5.4;", null);

            int total = 0;

            while(cursor.moveToNext()){
                total += this.getCourse(cursor.getInt(0)).getEcts();
            }
            return total;
        }

        public int getRequiredEcts(User user){
            return 60 - getTotalEcts(user);
        }

        public int getRemainingEcts(User user){
            int ects = 0;
            for(Course c : getRemainingCourses()){
                ects += c.getEcts();
            }
            return ects;
        }

        public List<Course> getRemainingCourses(){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM courses WHERE period >= " + (getCurrentPeriod())+ ";", null);
            List<Course> courses = new ArrayList<Course>();
            while(cursor.moveToNext()){
                courses.add(new Course(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
            }
            return courses;
        }

    public int getCurrentWeek(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR,2015);
        return calendar.get(Calendar.WEEK_OF_YEAR)-1;
    }

    public int getCurrentPeriod(){
        int currentWeek = getCurrentWeek();

        if(currentWeek>35 && currentWeek < 47) {
            return 1;
        }
        else if(((currentWeek>46&&currentWeek<52)||(currentWeek>0&&currentWeek<8))){
            return 2;
        }
        else  if(currentWeek>7 && currentWeek < 18){
            return 3;
        }
        else if(currentWeek>17 && currentWeek < 29){
            return 4;
        }
        else{
            return 5;
        }
    }
    }

