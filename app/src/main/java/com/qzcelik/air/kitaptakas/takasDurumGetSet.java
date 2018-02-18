package com.qzcelik.air.kitaptakas;

/**
 * Created by air on 18.02.2018.
 */

public class takasDurumGetSet {



    private String asilKitap;
    private String digerKitap;
    private  String kullanici;
    private String kitapResim;

    public  takasDurumGetSet(String asilKitap,String digerKitap,String kullanici,String kitapResim)
    {
        this.asilKitap = asilKitap;
        this.digerKitap = digerKitap;
        this.kitapResim = kitapResim;
        this.kullanici = kullanici;
    }

    public  String getAsilKitap()
    {
        return  asilKitap;
    }

    public  String getDigerKitap()
    {
        return  digerKitap;
    }

    public  String getKullanici()
    {
        return  kullanici;
    }
    public  String getKitapResim()
    {
        return kitapResim;
    }
}
