package com.example.quizzone.Model;

public class FillInWordModel {
    private String hint;
    private String yourAnswer;
    private String answer;
    private int status;

    public FillInWordModel(String hint, String yourAnswer, String answer, int status) {
        this.hint = hint;
        this.yourAnswer = yourAnswer;
        this.answer = answer;
        this.status = status;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
