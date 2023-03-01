package ik1332;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class DBHandler
{
    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("packages_of_greater_importance");

    public static void sendToDatabase(List<HashMap<String, String>> data) { dbRef.child(dbRef.push().getKey()).setValueAsync(data); }
}
