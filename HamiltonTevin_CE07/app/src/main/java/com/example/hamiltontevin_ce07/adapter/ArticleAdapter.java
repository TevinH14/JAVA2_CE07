package com.example.hamiltontevin_ce07.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.hamiltontevin_ce07.R;
import com.example.hamiltontevin_ce07.database.DataBaseHelper;
import com.loopj.android.image.SmartImageView;

public class ArticleAdapter extends ResourceCursorAdapter {

    public ArticleAdapter(Context context, int layout, Cursor c) {
        super(context, layout, c, 0);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = view.findViewById(R.id.tv_title_display);
        SmartImageView si = view.findViewById(R.id.iv_article_display);

        int titleColumnIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_TITLE);
        tv.setText(cursor.getString(titleColumnIndex));

        int thumbnailColumnIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_IMAGE_LINK);
        String imageString = cursor.getString(thumbnailColumnIndex);
        si.setImageUrl(imageString);
    }
}
