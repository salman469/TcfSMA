package com.tcf.sma.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.MenuItemsModel;
import com.tcf.sma.R;
import com.tcf.sma.SyncClasses.GenericAccountService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zubair Soomro on 1/9/2017.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<MenuItemsModel> parent_items;
    private List<ArrayList<String>> child_items;// header titles
    private ArrayList<String> childs;
    // child data in format of header title, child title

    /*public ExpandableListViewAdapter(Context context, List<MenuItemsModel> listDataHeader,
                                     List<ArrayList<String>> listChildData) {
        this._context = context;
        this.parent_items = listDataHeader;
        this.child_items = listChildData;
    }*/

    public ExpandableListViewAdapter(Context context, List<MenuItemsModel> listDataHeader,
                                     List<ArrayList<String>> listChildData) {
        this._context = context;
        this.parent_items = listDataHeader;
        this.child_items = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        childs = (ArrayList<String>) child_items.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, parent, false);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childs.get(childPosition));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return ((ArrayList<String>) child_items.get(groupPosition)).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.parent_items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.parent_items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, parent, false);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.iv_menu_parent_item);
        lblListHeader.setTypeface(null, Typeface.BOLD);

        if (parent_items.get(groupPosition).item_resource != null) {
            imgView.setImageDrawable(parent_items.get(groupPosition).item_resource);
        }
        lblListHeader.setText(parent_items.get(groupPosition).item_name);

        if (ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)
                || ContentResolver.isSyncPending(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
            convertView.setEnabled(false);
        } else {
            convertView.setEnabled(true);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
