package com.bbcow.test;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.jetty.util.security.Credential.MD5;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;





import com.google.common.io.Files;

public class TestImage {
	
	public static String md5(String title) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(title.getBytes());
        
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
     
        System.out.println("Digest(in hex format):: " + sb.toString());
        
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	return hexString.toString();
	}
	public static void main(String[] args) {
		
		Connection con = Jsoup.connect("http://img.plures.net/live/screenshots/15556/4.jpg");
		try {
			Response res = con.ignoreContentType(true).execute();
			byte[] bs = res.bodyAsBytes();
			Files.write(bs, new File("E:\\bbcow\\bbhtml_2\\img\\"+TestImage.md5("http://www.douyutv.com/cms/zt/nyc.html")+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
