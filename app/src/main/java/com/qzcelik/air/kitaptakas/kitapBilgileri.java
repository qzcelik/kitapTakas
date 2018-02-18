package com.qzcelik.air.kitaptakas;

/**
 * Created by air on 17.02.2018.
 */

public class kitapBilgileri {

    private String kitapAd;
    private String kitapTur;
    private  String kitapResim;
    private String kitapSahip;

    public  kitapBilgileri(String kitapAd,String kitapTur,String kitapResim,String kitapSahip)
    {
        this.kitapAd = kitapAd;
        this.kitapTur = kitapTur;
        this.kitapResim = kitapResim;
        this.kitapSahip = kitapSahip;
    }

    public  String getKitapAd()
    {
        return kitapAd;
    }

    public  String getKitapTur()
    {
        return kitapTur;
    }

    public  String getKitapResim()
    {
        return kitapResim;
    }

    public String getKitapSahip()
    {
        return kitapSahip;
    }
}
