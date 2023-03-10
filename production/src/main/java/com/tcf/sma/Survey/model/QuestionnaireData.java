package com.tcf.sma.Survey.model;

import android.graphics.Bitmap;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionnaireData {

    public String SectionName;
    public String QuestionText;    //can you stay abroad
    public String Prefix;
    public String FieldType;
    public String InstructionText;
    public ArrayList<OptionField> choicesList = new ArrayList<OptionField>();
    public ArrayList<ConditionalQuestionnaireData> conditionalQuest = new ArrayList<ConditionalQuestionnaireData>();
    //	public HashMap<String, String> gridQuestions = new HashMap<String, String>();
//	public HashMap<String, String> gridOptions = new HashMap<String, String>();
    public ArrayList<OptionField> gridQuestions = new ArrayList<OptionField>();
    public ArrayList<OptionField> gridOptions = new ArrayList<OptionField>();
    public ArrayList<QuestionnaireData> pageQuests = new ArrayList<QuestionnaireData>();

    public String dateTime_Date = "";
    public String dateTime_Time = "";

    //public String tagData;

    public String userAnswer;
    public View answerView;
    public Bitmap shopImage;

    public ArrayList<String> listUserAnswers = new ArrayList<String>();
    public ArrayList<View> listAnswerViews = new ArrayList<View>();
//	public ArrayList<String[]> listUserAnswersMultiChoice = new ArrayList<String[]>();

    public String FieldId;

    public String storedAssetLocation = "";

    public boolean mandatory;

    public QuestionnaireData(String fieldId) {
        this.FieldId = fieldId;
    }

    public QuestionnaireData(JSONObject quest) {
        try {
            this.QuestionText = quest.getString("FieldName");
            this.FieldType = quest.getString("FieldType");

            if (quest.has("Id")) {    //to be safe in the start
                this.FieldId = quest.getString("Id");
            }
            if (quest.has("Mandatory")) {
                this.mandatory = quest.getBoolean("Mandatory");
            }

            if (quest.has("Prefix")) {
                this.Prefix = quest.getString("Prefix");
            }
			
/*			if(quest.has("TAG")) {
				this.tagData = quest.getString("TAG");
			}*/

            if (this.FieldType.equals("RDO") || this.FieldType.equals("CHK") || this.FieldType.equals("DDN")) {

                JSONArray options = quest.getJSONArray("Options");
                for (int i = 0; i < options.length(); i++) {
                    JSONObject opt = options.getJSONObject(i);
                    this.choicesList.add(new OptionField(opt.getString("Id"), opt.getString("Option")));
                }

                if (quest.has("Conditions")) {
                    JSONArray cq = quest.getJSONArray("Conditions");
                    for (int i = 0; i < cq.length(); i++) {
                        conditionalQuest.add(new ConditionalQuestionnaireData(cq.getJSONObject(i).getString("choice"), cq.getJSONObject(i).getJSONObject("Quest")));
                    }
                }
            } else if (this.FieldType.equals("gridsinglechoice") || this.FieldType.equals("gridmultichoice")) {
                JSONArray questions = quest.getJSONArray("Questions");
                for (int i = 0; i < questions.length(); i++) {
                    JSONObject opt = questions.getJSONObject(i);
                    this.gridQuestions.add(new OptionField(opt.getString("Id"), opt.getString("FieldName")));
                }

                JSONArray options = quest.getJSONArray("Options");
                for (int i = 0; i < options.length(); i++) {
                    JSONObject opt = options.getJSONObject(i);
                    this.gridOptions.add(new OptionField(opt.getString("Id"), opt.getString("Option")));
                }
            } else if (this.FieldType.equals("gridtext") || this.FieldType.equals("gridinteger")) {
                JSONArray questions = quest.getJSONArray("Questions");
                for (int i = 0; i < questions.length(); i++) {
                    JSONObject opt = questions.getJSONObject(i);
                    this.gridQuestions.add(new OptionField(opt.getString("Id"), opt.getString("FieldName")));
                }
            } else if (this.FieldType.equals("XXX")) {
                if (quest.has("FieldName")) {
                    this.InstructionText = quest.getString("FieldName");
                }
            } else if (this.FieldType.equals("PAGE")) {

                JSONArray options = quest.getJSONArray("Questions");
                for (int i = 0; i < options.length(); i++) {
                    JSONObject opt = options.getJSONObject(i);
                    this.pageQuests.add(new QuestionnaireData(opt));
                }
            }
        } catch (JSONException exp) {
            exp.printStackTrace();
            int ii;
            ii = 10;
        }
    }

    public QuestionnaireData() {
    }

    public String getOptionId(String optionValue) {
        String ret = "";

        for (OptionField of : choicesList) {
            if (of.OptionValue.equals(optionValue)) {
                ret = of.OptionId;
                break;
            }
        }

        return ret;
    }

}
