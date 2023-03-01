package ik1332;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.*;

public class Parser
{
    private static String regex = "(?:[\r\n]*)Packet[\\s#]*(\\d+)[\\s|]+Node[\\s]+(\\d+)[\\s|]+TX\\s*ID\\s*(?:(\\d+|---))[\\s|]+" +
            "Temp *(?<temp>\\d+\\.\\d)[F\\s|]+Light\\s*(?<light>\\d+)\\s*lx(?:[\r\n\\s])*Time\\s*(\\d\\d:\\d\\d:\\d\\d)[\\s|]+dT " +
            "\\s*(\\d\\d:\\d\\d)[\\s|]+RSSI\\s*(\\d+\\.\\d+)mW[\\s|]+Humidity\\s*(?<hum>\\d+)\\s*%[\\s|]+Exte?rna?l\\s*(\\d+).*";
    private static Pattern p = Pattern.compile(regex);
    public static HashMap<String, String> parseText(String text) throws ParserException
    {
        HashMap<String, String> data = new HashMap<>();
        Matcher m = p.matcher(text);
        if(m.find())
        {
            data.put("Time", String.valueOf(System.currentTimeMillis()));
            data.put("Temp",        m.group("temp"));
            data.put("Light",       m.group("light"));
            data.put("Humidity",       m.group("hum"));
            return data;
        }
        throw new ParserException("No match in input text: " + text);
    }
}
