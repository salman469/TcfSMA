package com.tcf.sma.Managers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ortiz.touchview.TouchImageView;
import com.tcf.sma.Activities.CameraViewActivity;
import com.tcf.sma.R;

/**
 * Created by Mohammad.Haseeb on 1/6/2017.
 */

public class ImagePreviewDialog extends Dialog implements View.OnClickListener {

    TouchImageView ivPic_preview;

    Button iv_close;
    Button saveBtn;
    LinearLayout btnsLayout;
    Context context;
    Drawable drawable;
    Boolean showBtns;

    public ImagePreviewDialog(Context context, Drawable drawable, boolean showBtns) {
        super(context);

        this.context = context;
        this.drawable = drawable;
        this.showBtns = showBtns;
    }
    public ImagePreviewDialog(Context context, Drawable drawable) {
        super(context);

        this.context = context;
        this.drawable = drawable;
        this.showBtns = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pic_preview);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setAttributes(layoutParams);

        init();
        ivPic_preview.setImageDrawable(drawable);
    }


    void init() {

        iv_close = findViewById(R.id.iv_close);
        saveBtn = findViewById(R.id.saveBtn);
        ivPic_preview = findViewById(R.id.ivPic_preview);
        btnsLayout = findViewById(R.id.ll_btns);

        iv_close.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        if(context instanceof CameraViewActivity) {
            saveBtn.setVisibility(View.VISIBLE);
        }

        if(showBtns)
            saveBtn.setVisibility(View.VISIBLE);
        else
            saveBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;

            case R.id.saveBtn:
                cancel();
                break;

        }
    }

}
