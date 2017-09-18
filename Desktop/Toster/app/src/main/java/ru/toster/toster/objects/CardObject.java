package ru.toster.toster.objects;



public class CardObject {
    private String urlImage;
    private String tag;
    private String question;
    private int questionNumber;
    private boolean subscribe;
    private String subscribeNumber;
    private double progressBar;
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public double getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(double progressBar) {
        this.progressBar = progressBar;
    }

    public CardObject(String urlImage, String tag, String question, int questionNumber, boolean subscribe, String subscribeNumber) {
        this.urlImage = urlImage;
        this.tag = tag;
        this.question = question;
        this.questionNumber = questionNumber;
        this.subscribe = subscribe;
        this.subscribeNumber = subscribeNumber;
    }

    public String getSubscribeNumber() {
        return subscribeNumber;
    }

    public void setSubscribeNumber(String subscribeNumber) {
        this.subscribeNumber = subscribeNumber;
    }

    public CardObject(String urlImage, String tag, String question, int questionNumber, boolean subscribe) {
        this.urlImage = urlImage;
        this.tag = tag;
        this.question = question;
        this.questionNumber = questionNumber;
        this.subscribe = subscribe;
    }

    public CardObject() {
    }

    public CardObject(String urlImage, String tag, String question, int questionNumber) {
        this.urlImage = urlImage;
        this.tag = tag;
        this.question = question;
        this.questionNumber = questionNumber;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}
