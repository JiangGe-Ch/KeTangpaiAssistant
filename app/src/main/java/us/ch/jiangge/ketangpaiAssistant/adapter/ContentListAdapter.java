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
import us.ch.jiangge.ketangpaiAssistant.entity.ContentItem;

public class ContentListAdapter extends ArrayAdapter<ContentItem> {

    private static final String TAG="ContentListAdapter";

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
        if(item.getMstatus().equals("未完成")){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.bgcolor_mstatus_no));
        }else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.bgcolor_mstatus_default));
        }
        holder.courseName.setText(item.getCourseName());
        holder.teacher.setText(item.getTeacher());
        holder.className.setText(item.getClassName());
        return view;
    }

    class ViewHolder{
        /**
         * 内容类型 图标
         */
        ImageView typeImageView;
        /**
         * 内容类型 文字
         */
        TextView typeTextView;
        /**
         * 内容标题
         */
        TextView titleTextView;
        /**
         * 截止时间
         */
        TextView endTimeTextView;
        /**
         * 提交状态
         */
        TextView mstatusTextView;
        /**
         * 内容对应课程名称
         */
        TextView courseName;
        /**
         * 课程老师
         */
        TextView teacher;
        /**
         * 课程班级
         */
        TextView className;
    }

}
