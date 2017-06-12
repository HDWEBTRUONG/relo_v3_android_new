package main.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

import main.R;
import main.model.HistoryPushDTO;

/**
 * Created by tonkhanh on 6/8/17.
 */

public class HistoryPushAdapter extends RecyclerView.Adapter<HistoryPushAdapter.HisViewHolder>{
    ArrayList<HistoryPushDTO> listData;
    Context mContext;

    public HistoryPushAdapter(Context mContext,ArrayList<HistoryPushDTO> listData) {
        this.listData = listData;
        this.mContext = mContext;
    }

    @Override
    public HisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_push_row, parent, false);

        return new HisViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HisViewHolder holder, int position) {
        HistoryPushDTO data = listData.get(position);
        holder.tvHistoryTime.setText(data.getTimeHis());
        holder.tvHistoryContent.setText(data.getContentHis());
        holder.tvHistoryLink.setText(data.getUrlHis());
        String str_text = "<a href={0}><span>{1}</span></a>";
        holder.tvHistoryLink.setLinkTextColor(Color.BLUE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvHistoryLink.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tvHistoryLink.setText(Html.fromHtml(MessageFormat.format(str_text,
                    data.getUrlHis(),data.getLinkHis()), Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            holder.tvHistoryLink.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tvHistoryLink.setText(Html.fromHtml(MessageFormat.format(str_text,data.getUrlHis(),data.getLinkHis()).toString()));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HisViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHistoryTime, tvHistoryContent, tvHistoryLink;

        public HisViewHolder(View view) {
            super(view);
            tvHistoryTime = (TextView) view.findViewById(R.id.tvHistoryTime);
            tvHistoryContent = (TextView) view.findViewById(R.id.tvHistoryContent);
            tvHistoryLink = (TextView) view.findViewById(R.id.tvHistoryLink);
        }
    }
}
