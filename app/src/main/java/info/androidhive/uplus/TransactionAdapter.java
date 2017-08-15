package info.androidhive.uplus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import info.androidhive.uplus.activity.RecyclerAdapter;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>
{
    Context context;
    ArrayList<String> Money=new ArrayList<String>();
    ArrayList<String> TransactionTime=new ArrayList<String>();
    ArrayList<String> Status=new ArrayList<String>();
    public TransactionAdapter(Context context,ArrayList<String> Money,ArrayList<String> TransactionTime,ArrayList<String> Status)
    {
     this.context=context;
        this.Money=Money;
        this.TransactionTime=TransactionTime;
        this.Status=Status;
    }
    @Override
    public TransactionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transaction, parent, false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtTransactionTime.setText(provideCurrentDate(TransactionTime.get(position)));
        holder.Money.setText(Money.get(position)+" RWF");
        holder.SyncStatus.setText(Status.get(position));
    }

    @Override
    public int getItemCount() {
        return Money.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTransactionTime;
        public TextView Money;
        public TextView SyncStatus;
        public MyViewHolder(View itemView) {

            super(itemView);

            txtTransactionTime= (TextView) itemView.findViewById(R.id.txtTransactionTime);
            Money= (TextView) itemView.findViewById(R.id.txtMoney);
            SyncStatus= (TextView) itemView.findViewById(R.id.txtSync);
        }
    }
    public String provideCurrentDate(String data)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM  HH:mm");
        try {
            cal.setTime(sdf.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strDate = sdf.format(cal.getTime());
        return  strDate;
    }
}