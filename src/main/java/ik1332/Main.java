package ik1332;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TooManyListenersException;
import static ik1332.SerialListener.initSerialListener;

public class Main
{
    public final static int CARDINALITY = 3;
    public static void main(String[] args)
    {
        // Connect to the Firebase Real-time DB
        try
        {
            FileInputStream firebaseFile = new FileInputStream("wpt-project-52151-firebase-adminsdk-s0qsd-9795877985.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(firebaseFile))
                    .setDatabaseUrl("https://wpt-project-52151-default-rtdb.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        }
        catch (IOException e) { throw new RuntimeException(e); }

        // Set up listener for serial port
        try { initSerialListener("/dev/tty.usbmodem14101", 19200); }
        catch (NoSuchPortException e) { throw new RuntimeException(e); }
        catch (PortInUseException e) { throw new RuntimeException(e); }
        catch (TooManyListenersException e) { throw new RuntimeException(e); }
        catch (UnsupportedCommOperationException e) { throw new RuntimeException(e); }

    }
}