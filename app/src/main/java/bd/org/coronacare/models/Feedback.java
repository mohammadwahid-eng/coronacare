package bd.org.coronacare.models;

public class Feedback {
    private String id;
    private String comment;
    private float score;
    private long time;

    public Feedback() {

    }

    public Feedback(String id, String comment, float score, long time) {
        this.id = id;
        this.comment = comment;
        this.score = score;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}