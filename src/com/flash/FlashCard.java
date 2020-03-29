package com.flash;

public abstract class FlashCard {
    protected String term;
    protected String definition;
    protected int error;

    public void setTerm(String term) {
        this.term = term;
    }
    public String getTerm() {
        return term;
    }
    public void setDefinition(String definition) {
        this.definition = definition;
    }
    public String getDefinition() {
        return definition;
    }
    public void setError(int error) {
        this.error = error;
    }
    public int getError() {
        return error;
    }


}
