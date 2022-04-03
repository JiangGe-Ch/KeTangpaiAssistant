package us.ch.jiangge.ketangpaiAssistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import us.ch.jiangge.ketangpaiAssistant.R;
import us.ch.jiangge.ketangpaiAssistant.emuns.ContentType;
import us.ch.jiangge.ketangpaiAssistant.entity.ContentItem;

public class ContentListAdapter extends ArrayAdapter<ContentItem> {
    private int resourceId;
    public ContentListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<ContentItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContentItem item=getItem(position);
        View view;

        ViewHolder holder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            holder=new ViewHolder();
            holder.typeImageView=(ImageView) view.findViewById(R.id.content_item_type_image);
            holder.typeTextView=(TextView) view.findViewById(R.id.content_item_type_text);
            holder.titleTextView=(TextView) view.findViewById(R.id.content_item_title);
            holder.endTimeTextView=(TextView) view.findViewById(R.id.content_item_endtime);
            holder.mstatusTextView=(TextView) view.findViewById(R.id.content_item_mstatus);
            holder.courseName=(TextView) view.findViewById(R.id.content_item_course_info_name);
            holder.teacher=(TextView) view.findViewById(R.id.content_item_course_info_teacher);
            holder.className=(TextView) view.findViewById(R.id.content_item_course_info_classname);
            view.setTag(holder);
        }else {
            view=convertView;
            holder=(ViewHolder) view.getTag();
        }
        holder.typeImageView.setImageResource(item.getTypeImage());
        holder.typeTextView.setText(item.getContentType());
        holder.titleTextView.setText(item.getTitle());
        holder.endTimeTextView.setText("截止时间:"+item.getEndtime());
        holder.mstatusTextView.setText(item.getMstatus());
        holder.courseName.setText(item.getCourseName());
        holder.teacher.setText(item.getTeacher());
        holder.className.setText(item.getClassName());
        return view;
    }

    class ViewHolder{
        ImageView typeImageView;
        TextView typeTextView;
        TextView titleTextView;
        TextView endTimeTextView;
        TextView mstatusTextView;
        TextView courseName;
        TextView teacher;
        TextView className;
    }

}
