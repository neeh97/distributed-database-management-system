package Locking;

import java.io.*;

public class Locker {

    private static String lockFile = "Data/Locks.txt";

    public Boolean obtainLock(String username, String tableName) {
        Boolean lockNotFound = true;
        try {
            FileReader fileReader = new FileReader(lockFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null)
            {
                String [] line = currentLine.split(";");
                if (line[0].equals(username) && line[1].equals(tableName)) {
                    lockNotFound = false;
                    break;
                }
            }
            bufferedReader.close();
            fileReader.close();
            if (lockNotFound) {
                FileWriter fileWriter = new FileWriter(lockFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(username+";"+tableName);
                bufferedWriter.write("\n");
                bufferedWriter.close();
                fileWriter.close();
            }
        } catch (Exception e) {
            System.out.println("Exception in obtaining lock for table! "+e.getMessage());
            lockNotFound = false;
        }
        return lockNotFound;
    }

    public Boolean removeLock(String username, String tableName) {
        try {
            File lockingFile = new File(lockFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(lockingFile));
            File file = new File("Data/lockTemp.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                String [] line = currentLine.split(";");
                if (!(line[0].equals(username) && line[1].equals(tableName))) {
                    bufferedWriter.write(currentLine);
                    bufferedWriter.write("\n");
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            lockingFile.delete();
            file.renameTo(lockingFile);
        } catch (Exception e) {
            System.out.println("Exception in removing lock! "+e.getMessage());
            return false;
        }
        return true;
    }

}
