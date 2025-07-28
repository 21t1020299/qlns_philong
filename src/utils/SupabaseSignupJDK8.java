package utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SupabaseSignupJDK8 {
    public static void main(String[] args) throws Exception {
        String supabaseUrl = "https://wsisepsdhioaikdzrnmd.supabase.co";
        String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndzaXNlcHNkaGlvYWlrZHp" +
                "ybm1kIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM2NjM3MDIsImV4cCI6MjA2OTIzOTcwMn0.kwveMFa2IIwT519B0D1eQr9ZASQm" +
                "-TmQk90u68kUqQo";
        String jsonInputString = "{ \"email\": \"21t1020299@husc.edu.vn\", \"password\": \"03022003Aa\" }";

        URL url = new URL(supabaseUrl + "/auth/v1/signup");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("apikey", apiKey);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        System.out.println("Status Code: " + code);
    }
}
