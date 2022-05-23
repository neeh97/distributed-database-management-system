import java.io.File;
import java.io.IOException;

public class LogfileGeneration {
    public void generatelog() throws IOException {
        File eventlogfile = new File("EventLogs.txt");
        File generallogfile = new File("GeneralLogs.txt");
        if (eventlogfile.createNewFile())
        {
            System.out.println("Event Logs generated.");
        }
        if (generallogfile.createNewFile())
        {
            System.out.println("General Logs generated.");
        }
    }
}
