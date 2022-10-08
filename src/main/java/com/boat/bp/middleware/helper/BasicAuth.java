package com.boat.bp.middleware.helper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class BasicAuth {

    public static String encode(final String data)
    {
       final  String auth = data.concat(":").concat(data);
      // System.out.println( "Basic "+(new String( Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8)) )));
       return  "Basic "+(new String( Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8)) ));
    }   
    
}
