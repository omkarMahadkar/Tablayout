package com.example.mystorysaver.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mystorysaver.R;
import com.example.mystorysaver.adapter.WhatsappAdapter;
import com.example.mystorysaver.databinding.FragmentImageBinding;
import com.example.mystorysaver.model.WhatsappStatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class ImageFragment extends Fragment {

   private FragmentImageBinding binding;
   private ArrayList<WhatsappStatusModel> list;
   private WhatsappAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);

        list = new ArrayList<>();
        getData();

        binding.refresh.setOnRefreshListener(() -> {
            list = new ArrayList<>();
            getData();
            binding.refresh.setRefreshing(false);
        });

        return binding.getRoot();
    }

    private void getData() {
        WhatsappStatusModel model;

        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"/WhatsApp/Media/.Statuses";
        File targetDirector = new File(targetPath);
        File[] allFiles = targetDirector.listFiles();

//        if (allFiles == null){
//            targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
//                    File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
//            targetDirector = new File(targetPath);
//            allFiles = targetDirector.listFiles();
//        }

//        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath()+
//                File.separator + "/WhatsApp Business/Media/.Statuses";
//        File targetDirectorBusiness = new File(targetPathBusiness);
//        File[] allFilesBusiness = targetDirectorBusiness.listFiles();
//
//        if (allFilesBusiness == null){
//            targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() +
//                    File.separator + "Android/media/com.whatsapp/WhatsApp Business/Media/.Statuses";
//            targetDirectorBusiness = new File(targetPathBusiness);
//            allFilesBusiness = targetDirectorBusiness.listFiles();
//        }

        Arrays.sort(allFiles, ((o1, o2) -> {
            if (o1.lastModified() > o2.lastModified()) return  -1;
            else if (o1.lastModified() < o2.lastModified()) return +1;
            else return 0;
        }));

        for (int i=0; i < allFiles.length; i++){
            File file = allFiles[i];
            if (Uri.fromFile(file).toString().endsWith(".png") ||
                    Uri.fromFile(file).toString().endsWith(".jpg")){
                model = new WhatsappStatusModel("Whats"+i,
                        Uri.fromFile(file),
                        allFiles[i].getAbsolutePath(),
                        file.getName());
                list.add(model);
            }
        }

//        Arrays.sort(allFilesBusiness, ((o1, o2) -> {
//            if (o1.lastModified() > o2.lastModified()) return  -1;
//            else if (o1.lastModified() < o2.lastModified()) return +1;
//            else return 0;
//        }));
//
//        for (int i=0; i < allFilesBusiness.length; i++){
//            File file = allFilesBusiness[i];
//            if (Uri.fromFile(file).toString().endsWith(".png") ||
//                    Uri.fromFile(file).toString().endsWith(".jpg")){
//                model = new WhatsappStatusModel("WhatsBusiness"+i,
//                        Uri.fromFile(file),
//                        allFilesBusiness[i].getAbsolutePath(),
//                        file.getName());
//                list.add(model);
//            }
//        }
        adapter = new WhatsappAdapter(list, getActivity());
        binding.whatsappRecycler.setAdapter(adapter);

    }
}