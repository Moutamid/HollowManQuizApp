package com.moutamid.quizapp.models;

public class Answer {

    private int question;
    private int correct;
    private int wrong;


    public Answer(){

    }

    public Answer(int question, int correct, int wrong) {
        this.question = question;
        this.correct = correct;
        this.wrong = wrong;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }
}
