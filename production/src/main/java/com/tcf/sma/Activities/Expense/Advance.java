package com.tcf.sma.Activities.Expense;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.Expense.ExpenseAttachmentsAdapter;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class Advance extends DrawerActivity {

    View view;
    private AppCompatEditText et_expensehead,et_jvno,et_schoolname,et_head,et_amount,et_subhead,
            et_transactiontype,et_bucket,et_inout,et_checkno,et_balance;
    private LinearLayout llcheckno, ll_checkimages_heading,ll_receiptimages_heading;
    private RecyclerView rv_check_images,rv_receipt_images;
    private ExpenseAttachmentsAdapter expenseAttachmentsAdapter;
    private List<String> attachments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_advance);
        setToolbar("Transaction Detail", this, false);
        init();
        getBundle();
    }

    private void init(){
        et_expensehead = view.findViewById(R.id.tv_expensehead);
        et_jvno = view.findViewById(R.id.tv_jvno);
        et_schoolname = view.findViewById(R.id.tv_schoolname);
        et_head = view.findViewById(R.id.tv_head);
        et_amount = view.findViewById(R.id.tv_amount);
        et_subhead = view.findViewById(R.id.et_subhead);
        et_transactiontype = view.findViewById(R.id.et_transactiontype);
        et_bucket = view.findViewById(R.id.tv_bucket);
        et_inout = view.findViewById(R.id.tv_inout);
        et_checkno = view.findViewById(R.id.tv_checkno);
        et_balance = view.findViewById(R.id.et_balance);
        llcheckno = view.findViewById(R.id.llcheckno);
        ll_checkimages_heading = view.findViewById(R.id.ll_checkimages_heading);
        rv_check_images = view.findViewById(R.id.rv_check_images);
        ll_receiptimages_heading = view.findViewById(R.id.ll_receiptimages_heading);

        rv_receipt_images = view.findViewById(R.id.rv_receipt_images);
        rv_check_images = view.findViewById(R.id.rv_check_images);
    }

    private void getBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            et_expensehead.setText(bundle.getString("expensehead")!=null&&!bundle.getString("expensehead").equals("")?bundle.getString("expensehead"):"");
            et_jvno.setText(bundle.getString("jvno")!=null&&!bundle.getString("jvno").equals("")?bundle.getString("jvno"):"");
            et_schoolname.setText(bundle.getString("school")!=null&&!bundle.getString("school").equals("")?bundle.getString("school"):"");
            et_head.setText(bundle.getString("Head")!=null&&!bundle.getString("Head").equals("")?bundle.getString("Head"):"");
            et_subhead.setText(bundle.getString("SubHead")!=null&&!bundle.getString("SubHead").equals("")?bundle.getString("SubHead"):"");
            et_transactiontype.setText(bundle.getString("TransactionType")!=null&&!bundle.getString("TransactionType").equals("")?bundle.getString("TransactionType"):"");
            et_bucket.setText(bundle.getString("Bucket")!=null&&!bundle.getString("Bucket").equals("")?bundle.getString("Bucket"):"");
            et_inout.setText(bundle.getString("IO")!=null&&!bundle.getString("IO").equals("")?bundle.getString("IO"):"");
            et_amount.setText(bundle.getInt("Amount")>=0?String.format("%,d",bundle.getString("Amount")):"");
            et_balance.setText(bundle.getString("Balance")!=null&&!bundle.getString("Balance").equals("")?bundle.getString("Balance"):"");
            if(bundle.getString("Checkno")!=null&&!bundle.getString("Checkno").equals("")){
                et_checkno.setText(bundle.getString("Checkno"));
            }

            attachments.add("https://24e289a2-a-62cb3a1a-s-sites.googlegroups.com/site/halebands/writing-a-check/Screen%20Shot%202019-11-30%20at%207.59.48%20AM.png?attachauth=ANoY7cqugWuUMzJMrUgK6UiPBCT06GJa6Xq4_a_Snl7IVc1P3Jjw8NlG-drJkUUgX1NahHDq2Fxkk19bw5Df-V24Jd19IGki-afO7Q6RaOf6hLaaMpJH95hbFTd02ma7DquLlbYvXGz7tR3A9ORK9DdHU78d8SjH5CD9GKrx6SyUTezEGDt3W9521-7dZIcZJ7adUe8yCN37dAD_NF0Dbnvm__FguaLxJiYDdY29NdT3HbFeHn_seumkN7_pHYmcoTMgfq-pLblK2v4O89geOHV3mXa-0ruGwQ%3D%3D&attredirects=0");
            attachments.add("https://www.handsonbanking.org/financial-education/wp-content/uploads/2012/11/YA_01_05_05_en.jpg");

            if(bundle.getString("Bucket")!=null&&!bundle.getString("Bucket").equals("")){
                if(bundle.getString("Bucket").equalsIgnoreCase("bank")){

                    ll_receiptimages_heading.setVisibility(View.GONE);
                    rv_receipt_images.setVisibility(View.GONE);

                    ExpenseAttachmentsAdapter.paths.clear();
                    expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(true,this,attachments);
                    rv_check_images.setNestedScrollingEnabled(true);
                    rv_check_images.setHasFixedSize(true);
                    rv_check_images.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                    rv_check_images.setAdapter(expenseAttachmentsAdapter);

                }
                else if(bundle.getString("Bucket").equalsIgnoreCase("cash")){
                    llcheckno.setVisibility(View.GONE);
                    ll_checkimages_heading.setVisibility(View.GONE);
                    rv_check_images.setVisibility(View.GONE);

                    rv_receipt_images.setNestedScrollingEnabled(false);
                    rv_receipt_images.setHasFixedSize(true);
                    rv_receipt_images.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                    ExpenseAttachmentsAdapter.paths.clear();
                    expenseAttachmentsAdapter = new ExpenseAttachmentsAdapter(true,this,attachments);
                    rv_receipt_images.setAdapter(expenseAttachmentsAdapter);
                }
            }

        }
    }

}
