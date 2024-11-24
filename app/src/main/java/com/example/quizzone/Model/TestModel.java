package com.example.quizzone.Model;

public class TestModel {
    private String testID;
    private String topic;
    private int topScore;
    private int time;

    public TestModel(String testID,String topic, int topScore, int time) {
        this.testID = testID;
        this.topic = topic;
        this.topScore = topScore;
        this.time = time;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
