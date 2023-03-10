package com.tcf.sma.Adapters.HRTCT;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDesignationModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjTagReasonModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTLeaveTypeModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTSubjectsModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TCTEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //    pending: need to make actions for reason 7 and 9. clear subject models for reasons that have no subject selection (long leave, deceased, etc)
    private Context context;
    private List<TCTEmpSubjectTaggingModel> empSubjectTaggingModels;
    private List<TCTEmpSubjectTaggingModel> prevSubjectTaggingModels;
    private ArrayAdapter<TCTSubjectsModel> adp1, adp2;
    private TCTEntryAdapter.DatasetUpdateListener datasetUpdateListener;
    private int schoolId;
    private boolean isFirstTime = true;

    public TCTEntryAdapter(Context context, List<TCTEmpSubjectTaggingModel> empSubjectTaggingModels,
                           List<TCTEmpSubjectTaggingModel> prevSubjectTaggingModels, TCTEntryAdapter.DatasetUpdateListener datasetUpdateListener,
                           int schoolId) {
        this.context = context;
        this.empSubjectTaggingModels = empSubjectTaggingModels;
        this.datasetUpdateListener = datasetUpdateListener;
        this.prevSubjectTaggingModels = prevSubjectTaggingModels;
        this.schoolId = schoolId;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_tct_entry_data, viewGroup, false);
        return new TCTEntryDataVH(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
//        if (position > 0) {


        TCTEntryDataVH TCTEntryDataVH = (TCTEntryDataVH) viewHolder;

        if (position == 0) {
            TCTEntryDataVH.isMandatory.setVisibility(View.VISIBLE);
            if (empSubjectTaggingModels.get(position).isMandatory())
                TCTEntryDataVH.isMandatory.setText("Mandatory Test Takers");
            else
                TCTEntryDataVH.isMandatory.setText("Remaining Eligible test takers");

        } else if (position > 0 && !empSubjectTaggingModels.get(position).isMandatory() && empSubjectTaggingModels.get(position - 1).isMandatory()) {
            TCTEntryDataVH.isMandatory.setText("Remaining Eligible test takers");
            TCTEntryDataVH.isMandatory.setVisibility(View.VISIBLE);
        } else
            TCTEntryDataVH.isMandatory.setVisibility(View.GONE);
        TCTEntryDataVH.addSectionHeaders();

        TCTEntryDataVH.tv_empName.setText(empSubjectTaggingModels.get(position).getEMP_Name() +
                "\n(" + empSubjectTaggingModels.get(position).getEMP_Code() + ")");
        empSubjectTaggingModels.get(position).setCnicEditText(TCTEntryDataVH.et_cnic);
        datasetUpdateListener.onDataSetChanged(empSubjectTaggingModels, position);

        TCTEntryDataVH.tv_designation.setText(empSubjectTaggingModels.get(position).getDesignation_Name());
        TCTEntryDataVH.et_cnic.setText(empSubjectTaggingModels.get(position).getCNIC());
//        if (empSubjectTaggingModels.get(new_position).getComment() != null)
//            TCTEntryDataVH.et_comment.post(() -> TCTEntryDataVH.et_comment.setSelection(empSubjectTaggingModels.get(new_position).getComment().length()));

        List<TCTSubjectsModel> tctSubjectsModels = empSubjectTaggingModels.get(position).getTctSubjectsModels();
        tctSubjectsModels.add(0, new TCTSubjectsModel(0, context.getString(R.string.select_subjects)));

        if (empSubjectTaggingModels.get(position).getReasonID() <= 0) {
            populateSubject1DropDown(getSubjectsForSelectedDesigation(empSubjectTaggingModels.get(position).getDesignation_ID(), tctSubjectsModels), TCTEntryDataVH.sp_sub1, position);
            populateSubject2DropDown(tctSubjectsModels, TCTEntryDataVH.sp_sub2, position);
        }

        List<TCTEmpSubjTagReasonModel> reasonModelList = TCTHelperClass.getInstance(context).getTCTEmpSubTagReason();
        reasonModelList.add(0, new TCTEmpSubjTagReasonModel(0, context.getString(R.string.select_reason)));
        populateReasonDropDown(reasonModelList, TCTEntryDataVH.sp_reason);

        //if previously entered data then:
        int prevReasonIndex = populatePrevReasonDropDown(reasonModelList, empSubjectTaggingModels.get(position).getReasonID());
        if (prevReasonIndex > -1)
            TCTEntryDataVH.sp_reason.setSelection(prevReasonIndex);


        if (empSubjectTaggingModels.get(position).isMandatory()) {
            TCTEntryDataVH.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_app_green));
        } else {
            TCTEntryDataVH.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_color));
        }

        if (empSubjectTaggingModels.get(position).isMandatory()) {
            if (empSubjectTaggingModels.get(position).getSubject1_ID() > 0)
                TCTEntryDataVH.sp_sub1.setBackgroundColor(0x00000000);
            else
                TCTEntryDataVH.sp_sub1.setBackgroundColor(context.getResources().getColor(R.color.light_red_color));

            if (empSubjectTaggingModels.get(position).getSubject2_ID() > 0)
                TCTEntryDataVH.sp_sub2.setBackgroundColor(0x00000000);
            else
                TCTEntryDataVH.sp_sub2.setBackgroundColor(context.getResources().getColor(R.color.light_red_color));
        }


        List<EmployeeDesignationModel> designationModelList = TCTHelperClass.getInstance(context).getDesignationsForTCT();
//        List<EmployeeDesignationModel> designationModelList = EmployeeHelperClass.getInstance(context).getDesignations();
        designationModelList.add(0, new EmployeeDesignationModel(0, context.getString(R.string.select_designation)));
        populateDesignationDropDown(designationModelList, TCTEntryDataVH.sp_designationDropdown);

        List<TCTLeaveTypeModel> leaveTypeModelList = TCTHelperClass.getInstance(context).getTCTLeaveTypes();
        leaveTypeModelList.add(0, new TCTLeaveTypeModel(0, context.getString(R.string.select_leave_type)));
        populateLeaveTypeDropDown(leaveTypeModelList, TCTEntryDataVH.sp_leaveType_dropdown);


        //if previously entered data then:
        int prevLeaveIdIndex = empSubjectTaggingModels.get(position).getLeaveTypeID();
        if (prevLeaveIdIndex > -1)
            TCTEntryDataVH.sp_leaveType_dropdown.setSelection(prevLeaveIdIndex);

        if (empSubjectTaggingModels.get(position).getReasonID() == 3 && empSubjectTaggingModels.get(position).getNewDesignationId() > 0) {
            //if previously entered data then:
            int prevNewDesIdIndex = empSubjectTaggingModels.get(position).getNewDesignationId();
            ArrayAdapter<EmployeeDesignationModel> adapter = (ArrayAdapter<EmployeeDesignationModel>) TCTEntryDataVH.sp_designationDropdown.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).getId() == prevNewDesIdIndex) {
                    TCTEntryDataVH.sp_designationDropdown.setSelection(i);
                }
            }
        }

        //if previously entered data then:
        /*int prevSub1Index = empSubjectTaggingModels.get(position).getSubject1_ID();
        if (prevSub1Index > -1 && empSubjectTaggingModels.get(position).getReasonID() > 0) {

            //if previously entered data then:
            ArrayAdapter<TCTSubjectsModel> adapter = (ArrayAdapter<TCTSubjectsModel>) TCTEntryDataVH.sp_sub1.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).getId() == prevSub1Index) {
                    TCTEntryDataVH.sp_sub1.setSelection(i);
                }
            }
        }*/


        TCTEntryDataVH.shouldDisableAll(empSubjectTaggingModels.get(position).getRegStatusID());

    }

    private void populateSubject1DropDown(List<TCTSubjectsModel> subjectsModelList, Spinner sp_sub1, int new_position) {

        adp1 = new ArrayAdapter<TCTSubjectsModel>(context, R.layout.new_spinner_layout_black2, subjectsModelList);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sub1.setAdapter(adp1);

        //if previously entered data then:
        if (empSubjectTaggingModels.get(new_position).getSubject1_ID() > 0) {
            int prevSub1Index = populatePrevSubjectDropDown(subjectsModelList, empSubjectTaggingModels.get(new_position).getSubject1_ID());
            if (prevSub1Index > -1)
                sp_sub1.setSelection(prevSub1Index);
        }
    }

    private void populateSubject2DropDown(List<TCTSubjectsModel> subjectsModelList, Spinner sp_sub2, int new_position) {

        adp2 = new ArrayAdapter<TCTSubjectsModel>(context, R.layout.new_spinner_layout_black2, subjectsModelList);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sub2.setAdapter(adp2);

        if (empSubjectTaggingModels.get(new_position).getSubject2_ID() > 0) {
            int prevSub2Index = populatePrevSubjectDropDown(subjectsModelList,empSubjectTaggingModels.get(new_position).getSubject2_ID());
            if (prevSub2Index > -1)
                sp_sub2.setSelection(prevSub2Index);
        }
    }

    private int populatePrevSubjectDropDown(List<TCTSubjectsModel> list, int subjID) {
        if (list != null) {
            for (TCTSubjectsModel tsm : list) {
                if (tsm.getId() == subjID)
                    return list.indexOf(tsm);
            }
        }
        return -1;
    }

    private void populateReasonDropDown(List<TCTEmpSubjTagReasonModel> tctEmpSubjTagReasonModelList, Spinner sp_reason) {

        ArrayAdapter<TCTEmpSubjTagReasonModel> adapter = new ArrayAdapter<TCTEmpSubjTagReasonModel>(context, R.layout.new_spinner_layout_black2, tctEmpSubjTagReasonModelList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_reason.setAdapter(adapter);
    }

    private void populateDesignationDropDown(List<EmployeeDesignationModel> tctDesginationModels, Spinner sp_designation) {

        ArrayAdapter<EmployeeDesignationModel> adapter = new ArrayAdapter<EmployeeDesignationModel>(context, R.layout.new_spinner_layout_black2, tctDesginationModels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_designation.setAdapter(adapter);

    }

    private void populateLeaveTypeDropDown(List<TCTLeaveTypeModel> tctLeaveTypeModels, Spinner sp_leaveType) {

        ArrayAdapter<TCTLeaveTypeModel> adapter = new ArrayAdapter<TCTLeaveTypeModel>(context, R.layout.new_spinner_layout_black2, tctLeaveTypeModels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_leaveType.setAdapter(adapter);
    }

    private int populatePrevReasonDropDown(List<TCTEmpSubjTagReasonModel> models, int reasonID) {
        if (models != null) {
            for (TCTEmpSubjTagReasonModel testrm : models) {
                if (testrm.getId() == reasonID)
                    return models.indexOf(testrm);
            }
        }
        return -1;
    }

    private void validate(TCTEmpSubjectTaggingModel model, CardView card_view) {
        if (model.getTctSubjectsModels().size() == 2 && model.getTctSubjectsModels().get(0).getSubject().equals(context.getString(R.string.select_subjects))) {
            if (model.getSubject1_ID() == 0 && model.getReasonID() == 0) {
                card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red_color));
            } else {
                card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_color));
            }
        } else {
            if (model.getSubject1_ID() == 0 && model.getReasonID() == 0) {
                card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red_color));
            } else if (model.getSubject2_ID() == 0 && model.getReasonID() == 0 && model.getSubject1_ID() != 10) { // subject id 10 = KG class
                card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red_color));
            }
            //Transferred to School ID:
            else if (model.getSubject1_ID() == 0 && model.getSubject2_ID() == 0 && model.getReasonID() == 1
                    && (model.getComment() == null || model.getComment().isEmpty())) {
                card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red_color));
            } else {
                card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_color));
            }
        }
    }

    private boolean valid(TCTEmpSubjectTaggingModel model) {
        int i = 0;
        if (model.getTctSubjectsModels().size() == 2 && model.getTctSubjectsModels().get(0).getSubject().equals(context.getString(R.string.select_subjects))) {
            if (model.getSubject1_ID() == 0 && model.getReasonID() == 0) {
                i++;
            }
        } else {
            if (model.getSubject1_ID() == 0 && model.getReasonID() == 0) {
                i++;
            } else if (model.getSubject2_ID() == 0 && model.getReasonID() == 0) {
                i++;
            }
        }
        return i == 0;
    }

    @Override
    public int getItemCount() {
        return empSubjectTaggingModels.size();
    }

    public interface DatasetUpdateListener {
        void onDataSetChanged(List<TCTEmpSubjectTaggingModel> models, int position);
    }

    public class TCTEntryDataVH extends RecyclerView.ViewHolder {

        private TextView tv_empName, tv_designation, isMandatory;
        //        private ImageView btnSubmit;
        private LinearLayout ll_addComment;
        private CardView card_view;
        private Spinner sp_sub1, sp_sub2, sp_reason, sp_designationDropdown, sp_leaveType_dropdown;
        private EditText et_cnic;
        private LinearLayout ll_sub1, ll_sub2;
//        private boolean isSub1Selected = false, isSub2Selected = false, isReasonSelected = false;

        //        @SuppressLint("ClickableViewAccessibility")
        TCTEntryDataVH(@NonNull View itemView) {
            super(itemView);

            ll_sub1 = itemView.findViewById(R.id.ll_sub1);
            ll_sub2 = itemView.findViewById(R.id.ll_sub2);
            card_view = itemView.findViewById(R.id.card_view);
            tv_empName = itemView.findViewById(R.id.tv_empName);
            tv_designation = itemView.findViewById(R.id.tv_designation);
            sp_designationDropdown = itemView.findViewById(R.id.spinner_designation);
            sp_leaveType_dropdown = itemView.findViewById(R.id.spinner_leaveType);
            isMandatory = itemView.findViewById(R.id.mandatory);
            addSectionHeaders();
//            btnSubmit = itemView.findViewById(R.id.btnSubmit);
            et_cnic = itemView.findViewById(R.id.et_cnic);
//            et_comment.setFocusableInTouchMode(true);
//            et_comment.setFocusable(false);

            sp_sub1 = itemView.findViewById(R.id.spinner_SelectSub1);
            sp_sub2 = itemView.findViewById(R.id.spinner_SelectSub2);
            sp_reason = itemView.findViewById(R.id.spinner_SelectReason);


            sp_sub1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    List<TCTSubjectsModel> tctSubjectsModels = empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getTctSubjectsModels();

                    String subject = ((TCTSubjectsModel) adapterView.getItemAtPosition(i)).getSubject();
                    if (subject.equals(context.getString(R.string.select_subjects))) {
                        //subject id = 0
                        int subjectID = ((TCTSubjectsModel) adapterView.getItemAtPosition(i)).getId();
                        empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setSubject1_ID(subjectID);

                        subj2Dropdown(false);
                    } else {
//                        notifyItemChanged(new_position);
                        if (tctSubjectsModels.size() == 2 && tctSubjectsModels.get(0).getSubject().equals(context.getString(R.string.select_subjects))) {
                            int subjectID = ((TCTSubjectsModel) adapterView.getItemAtPosition(i)).getId();
                            empSubjectTaggingModels.get(getAdapterPosition()).setSubject1_ID(subjectID);

                            subj2Dropdown(false);

                        } else {
                            int subjectID = ((TCTSubjectsModel) adapterView.getItemAtPosition(i)).getId();
                            int previousSub1Id = empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getSubject1_ID();

                            empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setSubject1_ID(subjectID);


                            subj2Dropdown(true);


                            //If a Primary Teacher selects KG as subject 1 then subject 2 selection should be disabled as the teacher taking KG test is required to take just one subject
                            if (subjectID == 10 || subject.equals("KG")) {
                                subj2Dropdown(false);
                            } else
                                //disable 2nd subject selection if the reason is Taking full course load
                                if (empSubjectTaggingModels.get(getAdapterPosition()).getReasonID() == 4) {
                                    subj2Dropdown(false);
                                } else {

                                    //Designation id 52 = SST ,reason id 3 = Designation changed
                                    if (empSubjectTaggingModels.get(getAdapterPosition()).getDesignation_ID() == 52
                                            && empSubjectTaggingModels.get(getAdapterPosition()).getReasonID() != 3) {
                                        //(SST) must take 2 subjects Subject 2 Selection should also include SAT and SCT subjects
                                        List<TCTSubjectsModel> subList = TCTHelperClass.getInstance(context).getTCTSubjectsWithDesignation("'SAT,HSAT,SCT'", schoolId);

                                        if (subList != null && subList.size() > 0) {
                                            subList = getSubjectsForSelectedDesigation(empSubjectTaggingModels.get(getAdapterPosition()).getDesignation_ID(), subList);
                                            tctSubjectsModels = new ArrayList<>(empSubjectTaggingModels.get(getAdapterPosition()).getTctSubjectsModels());
                                            tctSubjectsModels.addAll(subList);

                                        }
                                    }
                                    //Designation id 58 = SCT ,reason id 3 = Designation changed
                                    else if (empSubjectTaggingModels.get(getAdapterPosition()).getDesignation_ID() == 58
                                            && empSubjectTaggingModels.get(getAdapterPosition()).getReasonID() != 3) {
                                        //SCT can choose one or two of the computer subjects If she chooses either of the computer subjects in Subject 1,
                                        //List of SAT subjects also need to be added for Subject 2 options along with second computer subject
                                        List<TCTSubjectsModel> subList = TCTHelperClass.getInstance(context).getTCTSubjectsWithDesignation("'SAT,HSAT'", schoolId);


                                        if (subList != null && subList.size() > 0) {
                                            subList = getSubjectsForSelectedDesigation(empSubjectTaggingModels.get(getAdapterPosition()).getDesignation_ID(), subList);
                                            tctSubjectsModels = new ArrayList<>(empSubjectTaggingModels.get(getAdapterPosition()).getTctSubjectsModels());
                                            tctSubjectsModels.addAll(subList);
                                        }
                                    }

                                    int selectedIndex = 0;
                                    for (TCTSubjectsModel item : tctSubjectsModels) {
                                        if (item.getId() == subjectID) {
                                            selectedIndex = tctSubjectsModels.indexOf(item);
                                            break;
                                        }
                                    }

                                    List<TCTSubjectsModel> sub2 = new ArrayList<>(tctSubjectsModels);
                                    sub2.remove(selectedIndex);


                                    //if KG is not selected in subject 1 then delete it from subject 2
                                    for (TCTSubjectsModel item : sub2) {
                                        if (item.getId() == 10 || item.getSubject().equals("KG")) {
                                            sub2.remove(item);
                                            break;
                                        }
                                    }


//                                  For Primary Teacher (PT) - If subject-1 is either of Pre-Primary science or Pre-Primary Math then
//                                  subject-2 option should only be Pre-Primary Math or Pre-Primary Science respectively.

//                                  For Primary Teacher (PT) - If subject-1 is not either of Pre-Primary Math/ Pre-Primary Science then
//                                  she should not have the option to select Pre-Primary Math / Science as subject-2


                                    //checking if designation is PT aka 57
                                    if (empSubjectTaggingModels.get(getAdapterPosition()).getDesignation_ID() == 57) {
                                        //checking that subjectId should be either Math Pre Primary or Science Pre Primary
                                        if (subjectID == 8 || subjectID == 9) {
                                            TCTSubjectsModel toAddModel = null;

                                            //if its science pre primary (8), then only math pre primary (9) option should be in sub 2 spinner
                                            if (subjectID == 8) {
                                                for (TCTSubjectsModel item : sub2) {
                                                    if (item.getId() == 9) {
                                                        toAddModel = item;
                                                    }
                                                }
                                                //clear all and add only 9
                                                if (toAddModel != null) {
                                                    sub2.clear();
                                                    sub2.add(0, new TCTSubjectsModel(0, context.getString(R.string.select_subjects)));
                                                    sub2.add(toAddModel);
                                                }

                                                //if its math pre primary (9), then only science pre primary (8) option should be in sub 2 spinner
                                            } else if (subjectID == 9) {
                                                for (TCTSubjectsModel item : sub2) {
                                                    if (item.getId() == 8) {
                                                        toAddModel = item;
                                                    }
                                                }
                                                //clear all and add only 8
                                                if (toAddModel != null) {
                                                    sub2.clear();
                                                    sub2.add(0, new TCTSubjectsModel(0, context.getString(R.string.select_subjects)));
                                                    sub2.add(toAddModel);
                                                }
                                            }

                                            //if user doesn't select from either 8 and 9, then sub2 drop down shouldn't have both options
                                        } else if (subjectID > 0) {
                                            TCTSubjectsModel firstId = null, secondId = null;
                                            for (TCTSubjectsModel item : sub2) {
                                                if (item.getId() == 8 || item.getId() == 9) {
                                                    if (item.getId() == 8)
                                                        firstId = item;
                                                    else if (item.getId() == 9)
                                                        secondId = item;
                                                }
                                            }

                                            if (firstId != null)
                                                sub2.remove(firstId);

                                            if (secondId != null)
                                                sub2.remove(secondId);
                                        }
                                    }

                                    populateSubject2DropDown(sub2, sp_sub2, getAdapterPosition());

                                    //if previously entered data then:
                                    if (empSubjectTaggingModels.get(getAdapterPosition()).getSubject2_ID() > 0 && subjectID == previousSub1Id) {
                                        int prevSub2Index = populatePrevSubjectDropDown(sub2, empSubjectTaggingModels.get(getAdapterPosition()).getSubject2_ID());
                                        if (prevSub2Index > -1) {
                                            sp_sub2.setSelection(prevSub2Index);
                                        }
                                    }
                                }
                        }
                    }
                    validate(empSubjectTaggingModels.get(getAdapterPosition()), card_view);
                    datasetUpdateListener.onDataSetChanged(empSubjectTaggingModels, getAdapterPosition());
                    if (!isFirstTime) {
                        et_cnic.requestFocus(getAdapterPosition());
                    }
//                    else {
//                        isSub1Selected = true;
//                    }

                    if (empSubjectTaggingModels.get(getAdapterPosition()).isMandatory()) {
                        if (empSubjectTaggingModels.get(getAdapterPosition()).getSubject1_ID() > 0)
                            sp_sub1.setBackgroundColor(0x00000000);
                        else
                            sp_sub1.setBackgroundColor(context.getResources().getColor(R.color.light_red_color));
                    }


                    et_cnic.setEnabled(isReasonOrAnySubjectSelected(empSubjectTaggingModels.get(getAbsoluteAdapterPosition())));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            sp_sub2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String subject = ((TCTSubjectsModel) adapterView.getItemAtPosition(i)).getSubject();
                    if (subject.equals(context.getString(R.string.select_subjects))) {
                        //subject id = 0
                        int subjectID = ((TCTSubjectsModel) adapterView.getItemAtPosition(i)).getId();
                        empSubjectTaggingModels.get(getAdapterPosition()).setSubject2_ID(subjectID);
                    } else {
                        int subjectID = ((TCTSubjectsModel) adapterView.getItemAtPosition(i)).getId();
                        empSubjectTaggingModels.get(getAdapterPosition()).setSubject2_ID(subjectID);

                    }
                    validate(empSubjectTaggingModels.get(getAdapterPosition()), card_view);
                    datasetUpdateListener.onDataSetChanged(empSubjectTaggingModels, getAdapterPosition());
                    if (!isFirstTime) {
                        et_cnic.requestFocus(getAbsoluteAdapterPosition());
                    }
//                    else {
//                        isSub2Selected = true;
//                    }

                    if (empSubjectTaggingModels.get(getAdapterPosition()).isMandatory()) {
                        if (empSubjectTaggingModels.get(getAdapterPosition()).getSubject2_ID() > 0)
                            sp_sub2.setBackgroundColor(0x00000000);
                        else
                            sp_sub2.setBackgroundColor(context.getResources().getColor(R.color.light_red_color));
                    }

                    et_cnic.setEnabled(isReasonOrAnySubjectSelected(empSubjectTaggingModels.get(getAbsoluteAdapterPosition())));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            sp_leaveType_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    int leaveTypeId = ((TCTLeaveTypeModel) adapterView.getItemAtPosition(i)).getID();

                    empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setLeaveTypeID(leaveTypeId);

                    validate(empSubjectTaggingModels.get(getAdapterPosition()), card_view);
                    datasetUpdateListener.onDataSetChanged(empSubjectTaggingModels, getAbsoluteAdapterPosition());

                    subj1Dropdown(false);
                    subj2Dropdown(false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            sp_designationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int designationId = ((EmployeeDesignationModel) adapterView.getItemAtPosition(i)).getId();
                    String designationName = ((EmployeeDesignationModel) adapterView.getItemAtPosition(i)).getDesignation_Name();

//                    empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setDesignation_ID(designationId);
                    empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setNewDesignationId(designationId);
//                    empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setDesignation_Name(designationName);


                    //Setting back the prev subjects to tagging model because of subjects changed when reason id 3
                    empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setTctSubjectsModels(TCTHelperClass.getInstance(context).getTCTSubjects(designationName, schoolId));
                    List<TCTSubjectsModel> subjectsModels = empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getTctSubjectsModels();
                    if (subjectsModels != null && subjectsModels.size() > 0 && !subjectsModels.get(0).getSubject().equals(context.getString(R.string.select_subjects)))
                        subjectsModels.add(0, new TCTSubjectsModel(0, context.getString(R.string.select_subjects)));
                    //Populating subjects adapters again because of subjects changed when reason id 3
                    populateSubject1DropDown(getSubjectsForSelectedDesigation(empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getNewDesignationId(), subjectsModels), sp_sub1, getAbsoluteAdapterPosition());
                    populateSubject2DropDown(subjectsModels, sp_sub2, getAbsoluteAdapterPosition());


//                    List<TCTSubjectsModel> subjectsModels = getSubjectsForSelectedDesigation(designationId, TCTHelperClass.getInstance(context).getTCTSubjects(designationName, schoolId));
//                    if (subjectsModels != null && subjectsModels.size() > 0 && !subjectsModels.get(0).getSubject().equals(context.getString(R.string.select_subjects)))
//                        subjectsModels.add(0, new TCTSubjectsModel(0, context.getString(R.string.select_subjects)));

//                    populateSubject1DropDown(subjectsModels, sp_sub1, getAbsoluteAdapterPosition());
//                    populateSubject2DropDown(subjectsModels, sp_sub2, getAbsoluteAdapterPosition());

                    validate(empSubjectTaggingModels.get(getAdapterPosition()), card_view);
                    datasetUpdateListener.onDataSetChanged(empSubjectTaggingModels, getAbsoluteAdapterPosition());

                    subj1Dropdown(designationId > 0);
                    subj2Dropdown(designationId > 0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            sp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    int reasonId = ((TCTEmpSubjTagReasonModel) adapterView.getItemAtPosition(position)).getId();
//                    int schoolId = empSubjectTaggingModels.get(getAdapterPosition()).getSchoolID();

                    empSubjectTaggingModels.get(getAdapterPosition()).setReasonID(reasonId);

                    validate(empSubjectTaggingModels.get(getAdapterPosition()), card_view);
                    datasetUpdateListener.onDataSetChanged(empSubjectTaggingModels, getAdapterPosition());
                    if (!isFirstTime) {
                        et_cnic.requestFocus(getAdapterPosition());
                    }

                    et_cnic.setEnabled(isReasonOrAnySubjectSelected(empSubjectTaggingModels.get(getAbsoluteAdapterPosition())));
//                    else {
//                        isReasonSelected = true;
//                    }

                    performActionWhenReasonSelected(reasonId);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            et_cnic.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String cnic = s.toString();
                    if (!cnic.isEmpty()) {
                        empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setCNIC(cnic);
                    } else {
                        empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setCNIC("");
                    }

                    /*if (empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getReasonID() == 1) {  //Transferred to School ID:
                        validate(empSubjectTaggingModels.get(getAbsoluteAdapterPosition()), card_view);
                    }*/

                    datasetUpdateListener.onDataSetChanged(empSubjectTaggingModels, getAbsoluteAdapterPosition());

                }
            });

            isFirstTime = false;

        }

        private void shouldDisableAll(int regStatus) {
            itemView.setEnabled(regStatus <= 1);
        }

        public void addSectionHeaders() {
            int position = getAbsoluteAdapterPosition();
            if (position == 0) {
                isMandatory.setVisibility(View.VISIBLE);
                if (empSubjectTaggingModels.get(position).isMandatory())
                    isMandatory.setText("Mandatory Test Takers");
                else
                    isMandatory.setText("Remaining Eligible Test Takers");

            } else if (position > 0 && !empSubjectTaggingModels.get(position).isMandatory() && empSubjectTaggingModels.get(position - 1).isMandatory()) {
                isMandatory.setText("Remaining Eligible Test Takers");
                isMandatory.setVisibility(View.VISIBLE);
            } else
                isMandatory.setVisibility(View.GONE);
        }

        private boolean isReasonOrAnySubjectSelected(TCTEmpSubjectTaggingModel model) {
            return model.getReasonID() > 0 || model.getSubject1_ID() > 0 || model.getSubject2_ID() > 0;
        }

        private void hideDesDD() {
            try {
                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setNewDesignationId(0);
                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setDesignation_ID(prevSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getDesignation_ID());
                sp_designationDropdown.setVisibility(View.GONE);
                sp_designationDropdown.setSelection(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void hideLeaveDD() {
            try {
                sp_leaveType_dropdown.setVisibility(View.GONE);
                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setLeaveTypeID(0);
                sp_leaveType_dropdown.setSelection(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void performActionWhenReasonSelected(int reasonId) {

            if (reasonId != 5) {
                hideLeaveDD();
            }
            if (reasonId != 3) {
                hideDesDD();
            }

            //transferred, Resigned, Deceased, Contract Expired
            if (reasonId == 1 || reasonId == 2 || reasonId == 6 || reasonId == 8) {
                subj1Dropdown(false);
                subj2Dropdown(false);
                /*et_cnic.setFilters(new InputFilter[]{});
                et_cnic.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                et_cnic.setHint("Enter CNIC here ...");*/
            } else if (reasonId == 5) { //Long Leaves
                subj1Dropdown(false);
                subj2Dropdown(false);
                sp_leaveType_dropdown.setVisibility(View.VISIBLE);

                try {
                    int prevLeaveIdIndex = empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getLeaveTypeID();
                    if (prevLeaveIdIndex > -1)
                        sp_leaveType_dropdown.setSelection(prevLeaveIdIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*et_cnic.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_cnic.setHint("Enter New School id");
                et_cnic.setMaxLines(3);
                et_cnic.setMinLines(3);
                et_cnic.setLines(3);
                et_cnic.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});*/
            } else if (reasonId == 3) { //Designation changed
                sp_designationDropdown.setVisibility(View.VISIBLE);
                /*List<TCTSubjectsModel> subjectsModels = TCTHelperClass.getInstance(context).getTCTSubjects("", schoolId);
                if (subjectsModels != null && subjectsModels.size() > 0 && !subjectsModels.get(0).getSubject().equals(context.getString(R.string.select_subjects)))
                    subjectsModels.add(0, new TCTSubjectsModel(0, context.getString(R.string.select_subjects)));*/

                //changing subjects in model
//                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setTctSubjectsModels(subjectsModels);

                try {
                    int prevNewDesIdIndex = empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getNewDesignationId();
                    if(prevNewDesIdIndex > 0) {
                        ArrayAdapter<EmployeeDesignationModel> adapter = (ArrayAdapter<EmployeeDesignationModel>) sp_designationDropdown.getAdapter();
                        for (int i = 0; i < adapter.getCount(); i++) {
                            if (adapter.getItem(i).getId() == prevNewDesIdIndex) {
                                sp_designationDropdown.setSelection(i);
                            }
                        }
                    } else {
                        subj1Dropdown(false);
                        subj2Dropdown(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //In reason Id 3 (Designation Change), subject 1 dropdown should show all subjects like in subject 2 dropdown
//                populateSubject1DropDown(getSubjectsForSelectedDesigation(empSubjectTaggingModels.get(getAdapterPosition()).getDesignation_ID(), subjectsModels), sp_sub1, getAdapterPosition());
//                populateSubject1DropDown(subjectsModels, sp_sub1, getAbsoluteAdapterPosition());
//                populateSubject2DropDown(subjectsModels, sp_sub2, getAbsoluteAdapterPosition());

//                et_cnic.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                et_cnic.setHint("Enter CNIC here ...");
            } else if (reasonId == 9) {
                //reason id 9 = Teaching subjects of other designation
                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setTctSubjectsModels(TCTHelperClass.getInstance(context).getTCTSubjects());
                List<TCTSubjectsModel> subjectsModels = empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getTctSubjectsModels();
                if (subjectsModels != null && subjectsModels.size() > 0 && !subjectsModels.get(0).getSubject().equals(context.getString(R.string.select_subjects)))
                    subjectsModels.add(0, new TCTSubjectsModel(0, context.getString(R.string.select_subjects)));

                populateSubject1DropDown(subjectsModels, sp_sub1, getAbsoluteAdapterPosition());
                populateSubject2DropDown(subjectsModels, sp_sub2, getAbsoluteAdapterPosition());

                subj1Dropdown(true);
                subj2Dropdown(true);

            } else {
                //reason id 7 = resigned and serving notice period
                //single subject taking full course load = reason id 4
                //Setting back the prev subjects to tagging model because of subjects changed when reason id 3
                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setTctSubjectsModels(prevSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getTctSubjectsModels());
                List<TCTSubjectsModel> subjectsModels = empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getTctSubjectsModels();
                if (subjectsModels != null && subjectsModels.size() > 0 && !subjectsModels.get(0).getSubject().equals(context.getString(R.string.select_subjects)))
                    subjectsModels.add(0, new TCTSubjectsModel(0, context.getString(R.string.select_subjects)));
                //Populating subjects adapters again because of subjects changed when reason id 3
                populateSubject1DropDown(getSubjectsForSelectedDesigation(empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).getDesignation_ID(), subjectsModels), sp_sub1, getAbsoluteAdapterPosition());
                populateSubject2DropDown(subjectsModels, sp_sub2, getAbsoluteAdapterPosition());

                subj1Dropdown(true);
                subj2Dropdown(true);
//                et_cnic.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                et_cnic.setHint("Enter CNIC here ...");
            }
        }


        private void subj2Dropdown(boolean enabled) {
            if (enabled) {
                sp_sub2.setBackgroundResource(R.drawable.ic_spinner_dropdown);
            } else {
                sp_sub2.setSelection(0);
                sp_sub2.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setSubject2_ID(0);
            }
            sp_sub2.setEnabled(enabled);

        }

        private void subj1Dropdown(boolean enabled) {
            if (enabled) {
                sp_sub1.setBackgroundResource(R.drawable.ic_spinner_dropdown);
            } else {
                sp_sub1.setSelection(0);
                sp_sub1.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setSubject1_ID(0);

                empSubjectTaggingModels.get(getAbsoluteAdapterPosition()).setTctSubjectsModels(new ArrayList<TCTSubjectsModel>());
            }
            sp_sub1.setEnabled(enabled);

        }

    }

    List<TCTSubjectsModel> getSubjectsForSelectedDesigation(int designationID, List<TCTSubjectsModel> tctSubjectsModelList) {
        if (designationID == 52
            /*&& empSubjectTaggingModels.get(getAdapterPosition()).getReasonID() != 3*/) {

            //"For SST designation
            //Subject 1 should only offer the Science subjects as mentioned for selection. And for Subject 2, she must have the option to choose remaining subjects."

            return tctSubjectsModelList.stream()
                    .filter(sub -> sub.getId() == 0
                            || sub.getSubject().equalsIgnoreCase(context.getString(R.string.select_subjects))
                            || sub.getId() == 22
                            || sub.getSubject().equalsIgnoreCase("Math")
                            || sub.getId() == 23
                            || sub.getSubject().equalsIgnoreCase("Science")
                            || sub.getId() == 24
                            || sub.getId() == 25
                            || sub.getSubject().equalsIgnoreCase("Chemistry")
                            || sub.getId() == 26
                            || sub.getSubject().equalsIgnoreCase("Biology")
                            || sub.getId() == 27
                            || sub.getSubject().equalsIgnoreCase("Physics"))
                    .collect(Collectors.toList());
        } else if (designationID == 58) {
            //"FOR SCT designation
            //Subject 1 should only offer the two computer subjects for selection. And for Subject 2, she must have the option to choose remaining subjects."

            return tctSubjectsModelList.stream().filter(sub -> sub.getId() == 0
                    || sub.getSubject().equalsIgnoreCase(context.getString(R.string.select_subjects))
                    || sub.getId() == 20
                    || sub.getId() == 21
                    || sub.getSubject().equalsIgnoreCase("Computer Studies")).collect(Collectors.toList());
        } else {
            return tctSubjectsModelList;
        }
    }
}
