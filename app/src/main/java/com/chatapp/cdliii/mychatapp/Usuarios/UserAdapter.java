package com.chatapp.cdliii.mychatapp.Usuarios;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chatapp.cdliii.mychatapp.Usuarios.Amigos.FriendsFragment;
import com.chatapp.cdliii.mychatapp.Usuarios.Solicitudes.RequestsFriendsFragment;
import com.chatapp.cdliii.mychatapp.Usuarios.Buscador.UserFragment;

/**
 * Created by ricky on 06-18-18.
 */

public class UserAdapter extends FragmentPagerAdapter {

    public UserAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new FriendsFragment();
        }else if(position==1){
            return new RequestsFriendsFragment();
        }
        else if(position==2){
            return new UserFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) return "Chats";
        else if(position==1) return "Solicitudes";
        else if(position==2) return "Buscar";
        return null;
    }
}
