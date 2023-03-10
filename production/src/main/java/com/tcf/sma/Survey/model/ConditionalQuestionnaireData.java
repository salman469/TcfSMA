package com.tcf.sma.Survey.model;

import org.json.JSONObject;

public class ConditionalQuestionnaireData extends QuestionnaireData {

    public String validatingChoice;

    public ConditionalQuestionnaireData(String choice, JSONObject question) {
        super(question);
        this.validatingChoice = choice;

    }

}
