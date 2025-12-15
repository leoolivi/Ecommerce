package com.ecommerce.main.utility;

import java.util.Random;

public class OTPUtility {
    public static char[] generateCode(int len) {
        System.out.println("Generating OTP using random() : ");
        System.out.print("You OTP is : ");

        // Using numeric values
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++) {
            otp[i] =
             numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }
    
    public static char[] generateCode() {
        return generateCode(4);
    }
}
