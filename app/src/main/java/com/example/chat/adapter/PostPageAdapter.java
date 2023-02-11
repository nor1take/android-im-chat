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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostPageAdapter extends PagerAdapter {
    private Context context;
    private List<Post> list;

    public PostPageAdapter(Context context, List<Post> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View postContainer = View.inflate(context, R.layout.b_tab1_post_item, null);

        TextView cardText = postContainer.findViewById(R.id.cardText);
        TextView labelText = postContainer.findViewById(R.id.label);
        TextView peopleNum = postContainer.findViewById(R.id.peopleNumber);
        TextView postTime = postContainer.findViewById(R.id.postTime);

//        Font_Util.setFont(cardText, 3, (Activity) context);
//        Font_Util.setFont(labelText, 2, (Activity) context);
//        Font_Util.setFont(peopleNum, 0, (Activity) context);
//        Font_Util.setFont(postTime, 0, (Activity) context);

        Post postDetail = list.get(position);

        cardText.setText(postDetail.getBody());
        labelText.setText("# " + postDetail.getLabel());
        peopleNum.setText("已有 " + postDetail.getPeopleNum().toString() + " 人参与聊天");
        postTime.setText("发布于 " + Time_Util.setTime(new Date(), postDetail.getTime()));

        container.addView(postContainer);
        return postContainer;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object); // 这一句要删除，否则报错
        // container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
