package info.androidhive.uplus;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by delaroy on 2/13/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Random rand = new Random();
    private ArrayList<String>data=new ArrayList<String>();
    private ArrayList<String>data1=new ArrayList<String>();
    ArrayList<String>ids=new ArrayList<String>();
    Context ctx;
RecyclerView rec;
    public MyAdapter(ArrayList<String>data,ArrayList<String>data1,Context ctx,RecyclerView rec,ArrayList<String>ids){
        this.data = data;
        this.ctx=ctx;
        this.data1=data1;
        this.rec=rec;
        this.ids=ids;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView mCardView;
        public TextView mTextView;
        public TextView moneyTextView;
        public ImageView imageView;
        public  ProgressBar progressBar;
        RelativeLayout relativeLayout;
        Context context;
        public MyViewHolder(View v, final Context context,final RecyclerView rcc,final ArrayList<String>ids,final ArrayList<String>data,final ArrayList<String>data1){
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            moneyTextView = (TextView) v.findViewById(R.id.tv_blah);
            imageView=(ImageView) v.findViewById(R.id.imgGroupProfile);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar2);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p=rcc.indexOfChild(v);
                    String id=ids.get(p);
                    //Toast.makeText(context,String.valueOf(ids.get(p)),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context,groupdetails.class);
                    intent.putExtra("Id",ids.get(p));
                    intent.putExtra("Name",data.get(p));
                    intent.putExtra("Amount",data1.get(p));
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(this.context,"CLicked",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v,this.ctx,this.rec,this.ids,this.data,this.data1);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position){
        final String url="http://104.236.26.9/groupimg/"+ids.get(position)+".jpg";
        if(isNetworkAvailable())
        {

            Picasso.with(ctx).load(url).into(holder.imageView,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Picasso.with(ctx).load(url).into(holder.imageView);
            }

            @Override
            public void onError() {
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color1 = generator.getRandomColor();

                TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted(data.get(position)), color1);
                holder.imageView.setImageDrawable(drawable);
            }
        });
        }
        else
        {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted(data.get(position)), color1);
            holder.imageView.setImageDrawable(drawable);
            holder.mTextView.setText(data.get(position));
        }

        String target = data1.get(position);
        int finalAmaunt = Integer.parseInt(target);
        //int random = rand.nextInt(finalAmaunt)+0;
        holder.progressBar.setProgress(rand.nextInt(100));
        holder.moneyTextView.setText(currencyConverter(data1.get(position)));
        holder.mTextView.setText(data.get(position));
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
    //function to check if network is avalaible or not
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String currencyConverter(String data)
    {

        Locale locale = new Locale("RWF", "RWF");
        Double value=Double.parseDouble(data);
        DecimalFormat decimalFormat=new DecimalFormat("#,###.00 RWF");
        return(decimalFormat.format(value));
    }
    @Override
    public int getItemCount() { return data.size(); }

}
