package ik1332;

import gnu.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TooManyListenersException;

import static ik1332.DBHandler.sendToDatabase;
import static ik1332.Main.*;
import static ik1332.Parser.parseText;
import static java.lang.System.out;

public class SerialListener implements SerialPortEventListener
{
    private static SerialPort serialPort;
    private static int index = 0;
    private static final int buffSize = 256;
    private static char[] buffer = new char[buffSize];
    private static HashMap<String, String> data;
    private static List<HashMap<String, String>> dataList = new ArrayList<>();
    public static void initSerialListener(String path, int baud) throws NoSuchPortException, PortInUseException, TooManyListenersException, UnsupportedCommOperationException
    {
        // Returns the port identifier for the specified port.
        // Throws: NoSuchPortException
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(path);
        // Opens port with fd, number is the timeout
        // Throws: PortInUseException
        CommPort commPort = portIdentifier.open(SerialListener.class.getName(), 2000);
        // set class field to point to 'commPort' reference
        serialPort = (SerialPort) commPort;
        // Adds new class instance to be event-listener, only one per port, so no need to keep track of it I guess
        // Throws: TooManyListenerException
        serialPort.addEventListener((SerialPortEventListener) new SerialListener());
        serialPort.notifyOnDataAvailable(true);
        // set RXTX spec
        // Throws: UnsupportedCommOperationException
        serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

    public void serialEvent(SerialPortEvent serialPortEvent)
    {
        try
        {
            InputStreamReader reader = new InputStreamReader(serialPort.getInputStream());
            // Wait until whole msg is in the serial port register/buffer (The baud is low)
            Thread.sleep(100);
            reader.read(buffer);
            try
            {
                data = parseText(new String(buffer));
                out.println("data: " + data);
                dataList.add(data);
                index++;
            }
            catch (ParserException e) { System.out.println(e.getMessage()); return;}
            if (index % CARDINALITY == 0)
            {
                sendToDatabase(dataList);
                index = 0;
                dataList.clear();
                out.println("Sending to firebase");
            }
        }
        catch (IOException e)          { throw new RuntimeException(e); }
        catch (InterruptedException e) { throw new RuntimeException(e); }
    }
}
