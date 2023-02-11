package com.example.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.chat.R;
import com.example.chat.pojo.Post;
import com.example.chat.utils.Font_Util;
import com.example.chat.utils.Time_Util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PostPageAdapter extends PagerAdapter {
    private Context context;
    private List<Post> list;
    private boolean isUpdate = false;

    private LinkedList<View> recycledViews = new LinkedList<View>();

    public PostPageAdapter(Context context, List<Post> list) {
        this.context = context;
        this.list = list;
    }

    public void update(List<Post> list) {
        this.list = list;
        isUpdate = true;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder holder = null;
        View postContainer = null;
        if (recycledViews != null && recycledViews.size() > 0) {
            postContainer = recycledViews.getFirst();
            holder = (ViewHolder) postContainer.getTag();
            recycledViews.removeFirst();
        } else {
            postContainer = View.inflate(context, R.layout.b_tab1_post_item, null);
            holder = new ViewHolder();
            holder.cardText = postContainer.findViewById(R.id.cardText);
            holder.labelText = postContainer.findViewById(R.id.label);
            holder.peopleNum = postContainer.findViewById(R.id.peopleNumber);
            holder.postTime = postContainer.findViewById(R.id.postTime);
            Font_Util.setFont(holder.cardText, 3, (Activity) context);
            Font_Util.setFont(holder.labelText, 2, (Activity) context);
            Font_Util.setFont(holder.peopleNum, 0, (Activity) context);
            Font_Util.setFont(holder.postTime, 0, (Activity) context);
            postContainer.setTag(holder);
        }

        Post postDetail = list.get(position);

        holder.cardText.setText(postDetail.getBody());
        holder.labelText.setText("# " + postDetail.getLabel());
        holder.peopleNum.setText("已有 " + postDetail.getPeopleNum().toString() + " 人参与聊天");
        holder.postTime.setText("发布于 " + Time_Util.setTime(new Date(), postDetail.getTime()));

        container.addView(postContainer);
        return postContainer;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        if (object != null) {
            recycledViews.addLast((View) object); // 缓存
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (isUpdate) {
            isUpdate = false;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    static class ViewHolder {
        TextView cardText;
        TextView labelText;
        TextView peopleNum;
        TextView postTime;
    }
}
