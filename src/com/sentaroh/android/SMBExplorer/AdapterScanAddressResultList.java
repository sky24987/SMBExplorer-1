package com.sentaroh.android.SMBExplorer;

/*
The MIT License (MIT)
Copyright (c) 2011-2013 Sentaroh

Permission is hereby granted, free of charge, to any person obtaining a copy of 
this software and associated documentation files (the "Software"), to deal 
in the Software without restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to 
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or 
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.sentaroh.android.Utilities.NotifyEvent;

public class AdapterScanAddressResultList extends ArrayAdapter<ScanAddressResultListItem>{

	private ArrayList<ScanAddressResultListItem>mResultList=null;
	private int mResourceId=0;
	private Context mContext;
	private NotifyEvent mNtfyEvent=null;
	private boolean mButtonEnabled=true;
	
	public AdapterScanAddressResultList(Context context, int resource,
			ArrayList<ScanAddressResultListItem> objects, NotifyEvent ntfy) {
		super(context, resource, objects);
		mResultList=objects;
		mResourceId=resource;
		mContext=context;
		mNtfyEvent=ntfy;
	}
	
	public void setButtonEnabled(boolean p) {
		mButtonEnabled=p;
		notifyDataSetChanged();
	};
	
	public void sort() {
		Collections.sort(mResultList, new Comparator<ScanAddressResultListItem>(){
			@Override
			public int compare(ScanAddressResultListItem lhs,
					ScanAddressResultListItem rhs) {
				return lhs.server_address.compareTo(rhs.server_address);
			}
		});
		notifyDataSetChanged();
	};

	@Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
		final ViewHolder holder;
		final ScanAddressResultListItem o = getItem(position);
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(mResourceId, null);
            holder=new ViewHolder();
            holder.tv_name=(Button)v.findViewById(R.id.scan_result_list_item_server_name);
            holder.tv_addr=(Button)v.findViewById(R.id.scan_result_list_item_server_addr);
            v.setTag(holder);
        } else {
        	holder= (ViewHolder)v.getTag();
        }
        if (o != null) {
        	holder.tv_name.setText(o.server_name);
        	holder.tv_addr.setText(o.server_address);
        	if (o.server_name.startsWith("---")) {
        		holder.tv_addr.setVisibility(Button.GONE);
        	} else {
        		holder.tv_addr.setVisibility(Button.VISIBLE);
        	}
        	if (o.server_name.equals("")) holder.tv_name.setEnabled(false);
        	holder.tv_name.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					if (o.server_name.startsWith("---") || !mButtonEnabled)  return;
					mNtfyEvent.notifyToListener(true, new String[]{"N",o.server_name});
				}
        	});

        	holder.tv_addr.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					if (o.server_name.startsWith("---") || !mButtonEnabled)  return;
					mNtfyEvent.notifyToListener(true, new String[]{"A",o.server_address});
				}
        	});
        }
        return v;
	};
	
	class ViewHolder {
		Button tv_name, tv_addr;
	};
}
class ScanAddressResultListItem {
	public String server_name="";
	public String server_address="";
}
