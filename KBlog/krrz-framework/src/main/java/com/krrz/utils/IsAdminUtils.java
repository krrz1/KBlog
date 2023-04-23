package com.krrz.utils;

public class IsAdminUtils {
    public static boolean isAdmin(Long id){
        if(id!=null && id.equals(1L)) return true;
        return false;
    }
}
