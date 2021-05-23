package model;

public class Score {

    private String courseID;
    private String scoreType;
    private double score;

    public Score (String id, String type, double s) {
        courseID = id;
        scoreType = type;
        score = s;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }


}
