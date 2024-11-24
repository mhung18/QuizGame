package com.example.quizzone.Model;

public class FlashCardModel {
    private String question;
    private String translate;
    private int status;


    public FlashCardModel(String question, String translate, int status) {
        this.question = question;
        this.translate = translate;
        this.status = status;

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
