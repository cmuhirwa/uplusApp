package info.androidhive.uplus.activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import info.androidhive.uplus.R;

/**
 * Created by delaroy on 2/13/17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<String> mDataset=new ArrayList<String>();
    private ArrayList<String> mAmount=new ArrayList<String>();
    private ArrayList<String> mType=new ArrayList<String>();
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView mTextView;
        public TextView moneyTextView;
        public TextView textView2;
        public ImageView imageView;
        public MyViewHolder(View v){
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card1);
            mTextView = (TextView) v.findViewById(R.id.txtMember);
            moneyTextView = (TextView) v.findViewById(R.id.textView3);
            textView2=(TextView)v.findViewById(R.id.textView2);
            imageView=(ImageView)v.findViewById(R.id.imageView);

        }

    }

    public RecyclerAdapter(ArrayList<String> mDataset,ArrayList<String> mAmount,ArrayList<String> mType){
        this.mDataset=mDataset;
        this.mAmount=mAmount;
        this.mType=mType;
    }
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        holder.mTextView.setText(mDataset.get(position));
        holder.moneyTextView.setText(currencyConverter(mAmount.get(position)));
        holder.textView2.setText(mType.get(position));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(textTobeConverted(mDataset.get(position)), color1);
        holder.imageView.setImageDrawable(drawable);
    }
    public String currencyConverter(String data)
    {
        Locale locale = new Locale("RWF", "RWF");
        Double value=Double.parseDouble(data);
        DecimalFormat decimalFormat=new DecimalFormat("#,###.00 RWF");
        return(decimalFormat.format(value));
    }
    public String textTobeConverted(String data)
    {
        String newData="D";
        int length=data.length();
        if(length>0)
        {
            newData=data.substring(0,1);

        }
        return newData;
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
