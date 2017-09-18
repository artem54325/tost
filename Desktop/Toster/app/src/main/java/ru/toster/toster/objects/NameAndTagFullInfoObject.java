package ru.toster.toster.objects;



public class NameAndTagFullInfoObject {
    private String name;
    private String dopName;
    private String urlPhoto;
    private String contribution;
    private String question;
    private String answer;
    private String decisions;
    private boolean subscribe;
    //Дополнительная информация!!
    private String textInfo = null;

    public NameAndTagFullInfoObject() {
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public String getTextInfo() {
        return textInfo;
    }

    public void setTextInfo(String textInfo) {
        this.textInfo = textInfo;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDopName() {
        return dopName;
    }

    public void setDopName(String dopName) {
        this.dopName = dopName;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDecisions() {
        return decisions;
    }

    public void setDecisions(String decisions) {
        this.decisions = decisions;
    }
}
