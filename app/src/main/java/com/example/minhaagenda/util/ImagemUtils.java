package com.example.minhaagenda.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import androidx.multidex.MultiDexApplication;

public class ImagemUtils extends MultiDexApplication {
    public static void setImagem(ImageView view, String caminho){
        setImagem(view, caminho, 512, 512);
    }

    public static void setImagem(ImageView imagem, String caminho, int width, int height) {
        if (caminho != null && !caminho.isEmpty()){
            Bitmap bitmap = BitmapFactory.decodeFile(caminho);
            if (bitmap != null){
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, width, height, true);
                imagem.setImageBitmap(bitmapReduzido);
                imagem.setBackgroundColor(Color.TRANSPARENT);
                imagem.setTag(caminho);
                imagem.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
    }
}