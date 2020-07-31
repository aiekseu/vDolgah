package com.zlodeuscomp.vdolgah.screens.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.zlodeuscomp.vdolgah.App;
import com.zlodeuscomp.vdolgah.R;
import com.zlodeuscomp.vdolgah.details.AddDebtor;
import com.zlodeuscomp.vdolgah.model.Debtor;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.DebtorViewHolder> {

    private SortedList<Debtor> sortedList;
    public Adapter() {
        sortedList = new SortedList<>(Debtor.class, new SortedList.Callback<Debtor>() {
            @Override
            public int compare(Debtor o1, Debtor o2) {
                return (int) (o2.timestamp - o1.timestamp);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Debtor oldItem, Debtor newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Debtor item1, Debtor item2) {
                return item1.uid == item2.uid;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @NonNull
    @Override
    public DebtorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DebtorViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_debtor_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DebtorViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void setItems(List<Debtor> debtors) {
        sortedList.replaceAll(debtors);
    }

    static class DebtorViewHolder extends RecyclerView.ViewHolder {

        TextView debtor_name;
        TextView debtor_sum;
        TextView debtor_email;
        TextView debtor_date;

        LinearLayout debtor_item;

        Debtor debtor;



        public DebtorViewHolder(@NonNull final View itemView) {
            super(itemView);

            debtor_name = itemView.findViewById(R.id.debtor_name);
            debtor_sum = itemView.findViewById(R.id.debtor_sum);
            debtor_email = itemView.findViewById(R.id.debtor_email);
            debtor_date = itemView.findViewById(R.id.debtor_date);

            debtor_item = itemView.findViewById(R.id.debtor_item);
            debtor_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.isOpenedToUpdate = true;
                    AddDebtor.start((Activity) itemView.getContext(), debtor);
                }
            });
            debtor_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    App.getInstance().getDebtorDao().delete(debtor);
                    return false;
                }
            });
        }

        public void bind(Debtor debtor) {
            this.debtor = debtor;
            debtor_name.setText(debtor.name);
            debtor_date.setText(debtor.date);
            debtor_sum.setText(debtor.sum);
            debtor_email.setText(debtor.email);
        }

    }
}
