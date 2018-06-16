package com.chatapp.cdliii.mychatapp.Mensajes;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chatapp.cdliii.mychatapp.R;

import java.util.List;

/**
 * Created by ricky on 06-04-18.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MensajesViewHolder> {

    private List<TextMsg> mensajeTexto;
    private Context context;

    public MsgAdapter(List<TextMsg> mensajeTexto, Context context) {

        this.mensajeTexto = mensajeTexto;
        this.context = context;
    }

    @Override
    public MensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mensajes, parent,false);
        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MensajesViewHolder holder, int position) {
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) holder.mensajeBG.getLayoutParams();

        LinearLayout.LayoutParams llHora = (LinearLayout.LayoutParams) holder.tvHora.getLayoutParams();
        LinearLayout.LayoutParams llMensaje = (LinearLayout.LayoutParams) holder.tvMsg.getLayoutParams();

        if(mensajeTexto.get(position).getTipoMensaje()==1){//EMISOR
            holder.mensajeBG.setBackgroundResource(R.drawable.in_message_bg);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            fl.gravity = Gravity.RIGHT;
            llHora.gravity = Gravity.RIGHT;
            llMensaje.gravity = Gravity.RIGHT;
            holder.tvMsg.setGravity(Gravity.RIGHT);
        }else if(mensajeTexto.get(position).getTipoMensaje()==2){//RECEPTOR
            holder.mensajeBG.setBackgroundResource(R.drawable.out_message_bg);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            fl.gravity = Gravity.LEFT;
            llHora.gravity = Gravity.LEFT;
            llMensaje.gravity = Gravity.LEFT;
            holder.tvMsg.setGravity(Gravity.LEFT);

        }
        holder.cardView.setLayoutParams(rl);
        holder.mensajeBG.setLayoutParams(fl);
        holder.tvHora.setLayoutParams(llHora);
        holder.tvHora.setLayoutParams(llMensaje);

        holder.tvMsg.setText(mensajeTexto.get(position).getMsg());
        holder.tvHora.setText(mensajeTexto.get(position).getHora());

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) holder.cardView.getBackground().setAlpha(0);
        else holder.cardView.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));



    }

    @Override
    public int getItemCount() {
        return mensajeTexto.size();
    }

    static class MensajesViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView tvMsg;
        TextView tvHora;
        LinearLayout mensajeBG;

        MensajesViewHolder(View view){
            super(view);
            cardView = (CardView) view.findViewById(R.id.cvMsg);
            tvMsg = (TextView) view.findViewById(R.id.tvTexto);
            tvHora = (TextView) view.findViewById(R.id.tvHora);
            mensajeBG = (LinearLayout) view.findViewById(R.id.mensajeBG);
        }
    }

}
