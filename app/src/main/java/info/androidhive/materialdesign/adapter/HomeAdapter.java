package info.androidhive.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.model.HomeBean;
import info.androidhive.materialdesign.view.CircleImageView;

public class HomeAdapter extends BaseAdapter
{
    Context context;
    ArrayList<HomeBean> rowItems;
     
    public HomeAdapter(Context context, ArrayList<HomeBean> items) {
        this.context = context;
        this.rowItems = items;
    }

    private class ViewHolder {
        CircleImageView mProfileIv;
        TextView mNameTv;
        TextView mReletionTv;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
         
        LayoutInflater mInflater = (LayoutInflater)
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_list_item, null);
            holder = new ViewHolder();

            holder.mNameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.mReletionTv = (TextView) convertView.findViewById(R.id.reletion_tv);
            holder.mProfileIv = (CircleImageView) convertView.findViewById(R.id.profile_iv);


            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        HomeBean bean=rowItems.get(position);

        holder.mReletionTv.setText(bean.getmReletionship());
        holder.mNameTv.setText(bean.getmName());


        ImageLoader.getInstance().displayImage(AppConfig.IMAGE_PATH+bean.getmPhoto(),holder.mProfileIv);
      //  holder.imageView.setImageResource(rowItem.getImageId());
         
        return convertView;
    }
 
    @Override
    public int getCount() {     
        return rowItems.size();
    }
 
    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}