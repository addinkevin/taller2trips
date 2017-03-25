package com.example.pc.myapplication.ciudadTools;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.atraccionesTools.AtraccionItem;
import com.example.pc.myapplication.atraccionesTools.AtraccionesListAdp;

import java.util.ArrayList;
import java.util.List;

public class AtraccionesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private  List<AtraccionItem> atraccionItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_atracciones, container, false);

       atraccionItems = new ArrayList<>();
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.df_image);
        atraccionItems.add(new AtraccionItem("Obelisco",largeIcon, ""));
        atraccionItems.add(new AtraccionItem("Obelisco",largeIcon, ""));
        atraccionItems.add(new AtraccionItem("Obelisco",largeIcon, ""));
        atraccionItems.add(new AtraccionItem("Obelisco",largeIcon, ""));
        atraccionItems.add(new AtraccionItem("Obelisco",largeIcon, ""));
        atraccionItems.add(new AtraccionItem("Obelisco",largeIcon, ""));
        atraccionItems.add(new AtraccionItem("Obelisco",largeIcon, ""));
        atraccionItems.add(new AtraccionItem("Obelisco",largeIcon, ""));

        AtraccionesListAdp atraccionesAdp = new AtraccionesListAdp(this,atraccionItems);

        ListView atraccList = (ListView) myFragmentView.findViewById(R.id.atraccList);
        atraccList.setAdapter(atraccionesAdp);
        atraccList.setOnItemClickListener(this);
        return myFragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent chat = new Intent(getActivity(), AtraccionActivity.class);
        chat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chat.putExtra("_id", atraccionItems.get(position)._id);
        this.startActivity(chat);
    }
}