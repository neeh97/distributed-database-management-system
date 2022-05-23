package DML;

import Locking.Locker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

public class DMLQueryExecution {

    Locker locker = new Locker();

    public void insert(Matcher insert, String username, FileWriter eventfile, FileWriter generalfile) throws IOException {
        String TableName, first, second;
        TableName = insert.group(2);
        if (!locker.obtainLock(username, TableName)) {
            System.out.println("Failed to obtain lock on table");
            return;
        }
        File file = new File("Data/" + username + "_" + TableName + ".txt");
        boolean file_exist = file.exists();
        if (file_exist) {

            first = insert.group(3);
            second = insert.group(4);

            String[] Column = first.split("\\s*,\\s*");
            String[] Value = second.split("\\s*,\\s*");

            List<String> ListOfColums = Arrays.asList(Column);
            List<String> ListOfValues = Arrays.asList(Value);

            File f = new File("Data/UserTableDictionary.txt");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String in = br.readLine();

            while (in != null) {
                if (in.equalsIgnoreCase(username)) {
                    in = br.readLine();
                    if (in.equalsIgnoreCase(TableName)) {
                        in = br.readLine();
                        boolean bl = !in.isBlank();
                        while (bl) {
                            String[] t = in.split(";");
                            if (t.length > 2) {
                                if (t[2].equalsIgnoreCase("pk")) {
                                    File table = new File("Data/" + username + "_" + TableName + ".txt");
                                    FileReader tr = new FileReader(table);
                                    BufferedReader tablebr = new BufferedReader(tr);
                                    List<String> column1 = new ArrayList<>();
                                    String run = tablebr.readLine();
                                    while (run != null) {
                                        if (!run.isBlank()) {
                                            String[] r = run.split(" ");
                                            String tablecolumn = r[0];
                                            if (tablecolumn.equalsIgnoreCase(t[0])) {
                                                column1.add(r[1]);
                                            }
                                        }
                                        run = tablebr.readLine();
                                    }
                                    if (ListOfColums.contains(t[0])) {
                                        int index = ListOfColums.indexOf(t[0]);

                                        for (String s : column1) {
                                            if (ListOfValues.get(index).equalsIgnoreCase(s)) {
                                                System.out.println("Duplicate value exists while entering in primary key column!");
                                                if (!locker.removeLock(username, TableName)) {
                                                    System.out.println("Failed to remove lock on table");
                                                    return;
                                                }
                                                return;
                                            }
                                        }
                                        FileWriter fileWriter = new FileWriter(file, true);

                                        int length = ListOfColums.size();
                                        for (int i = 0; i < length; i++) {
                                            fileWriter.write(ListOfColums.get(i) + " " + ListOfValues.get(i) + "\n");
                                        }
                                        fileWriter.write("\n");
                                        fileWriter.close();
                                        System.out.println("Inserted Successfully");
                                    } else {
                                        FileWriter fileWriter = new FileWriter(file, true);

                                        int length = ListOfColums.size();
                                        for (int i = 0; i < length; i++) {
                                            fileWriter.write(ListOfColums.get(i) + " " + ListOfValues.get(i) + "\n");
                                        }
                                        fileWriter.write("\n");
                                        fileWriter.close();
                                        System.out.println("Inserted Successfully");

                                    }
                                    tr.close();
                                    tablebr.close();


                                }
                            }
                            if (br.readLine() == null) {
                                bl = false;
                            }

                        }
                        br.close();
                        fr.close();
                        break;
                    }
                }
            }
            
        } else {
            System.out.println("Table does not exist to perform insert.");
            eventfile.append("[").append(username).append("] Table does not exist to perform insert.").append("\n");
        }
        eventfile.close();
        generalfile.close();
        if (!locker.removeLock(username, TableName)) {
            System.out.println("Failed to remove lock on table");
            return;
        }

    }

    public void delete(Matcher delete, String username, FileWriter eventfile, FileWriter generalfile) throws IOException {

        String query = delete.group(0);
        String[] querywords = query.split(" ");
        String TableName = querywords[2];
        if (!locker.obtainLock(username, TableName)) {
            System.out.println("Failed to obtain lock on table");
            return;
        }
        File file = new File("Data/" + username + "_" + TableName + ".txt");

        boolean file_exist = file.exists();
        if (file_exist)
        {
            String condition = querywords[4];
            String[] columnandvalue = condition.split("=|;");
            String col = columnandvalue[0];
            String value = columnandvalue[1];

            Scanner scanner = new Scanner(file);
            File tempfile = new File("Data/temp.txt");
            tempfile.createNewFile();
            FileWriter fileWriter = new FileWriter(tempfile);
            File updatedtable = new File("Data/updated.txt");
            updatedtable.createNewFile();
            FileWriter fw = new FileWriter(updatedtable);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line == "")
                {
                    fw.append("\n");
                    System.out.println("");
                }
                else
                    {
                    String[] s = line.split(" ");
                    if (s[0].equalsIgnoreCase(col) && s[1].equalsIgnoreCase(value))
                    {
                        while (scanner.hasNextLine()) {
                            fileWriter.write(s[0] + " " + s[1]);
                            if (scanner.nextLine() == "")
                            {
                                break;
                            }
                        }
                    }
                    else if (s[0] != col && s[1] != value)
                    {
                        fw.write(line + "\n");
                        System.out.println(line);

                    }
                    else
                        {
                        System.out.println("record not found");
                    }

                }


            }
            System.out.println("deletion performed successfully");
            fileWriter.close();
            fw.close();
            scanner.close();
            file.delete();
            updatedtable.renameTo(file);

            if (tempfile.delete())
            {
                eventfile.append("[").append(username).append("] Temp file deleted.").append("\n");
            }

        }
        else
            {
            System.out.println("Table does not exist to perform delete.");
            eventfile.append("[").append(username).append("] Table does not exist to perform delete.").append("\n");

        }
        if (!locker.removeLock(username, TableName)) {
            System.out.println("Failed to remove lock on table");
            return;
        }
        generalfile.close();
        eventfile.close();

    }

    public void updateTable(String username, String updateOperations, String tableName, String conditions) {
        if (!locker.obtainLock(username, tableName)) {
            System.out.println("Failed to obtain lock on table");
            return;
        }
        String[] updateOperationsArray = updateOperations.trim().split("=");
        String[] conditionsArray = conditions.trim().split("=");
        try {
            File myObj = new File("Data/" + username + "_" + tableName + ".txt");
            Scanner myReader = new Scanner(myObj);
            String s = "";
            Map<String,String> keyValue = new HashMap<String,String>();
            String nextLine;
            while (myReader.hasNextLine())
            {
                nextLine = myReader.nextLine();

                while( nextLine.trim().length() > 0 ) {
                    String[] row = nextLine.trim().split(" ");
                    keyValue.put(row[0], row[1]);
                    if(myReader.hasNextLine()) {
                        nextLine = myReader.nextLine();
                    } else {
                        nextLine = "";
                    }
                }

                if(nextLine.trim().length() == 0)
                {
                    String value = keyValue.get(conditionsArray[0]);
                    if(value.trim().equalsIgnoreCase(conditionsArray[1].trim()))
                    {
                        keyValue.put(updateOperationsArray[0], updateOperationsArray[1]);
                    }
                    Iterator<String> iterator = keyValue.keySet().iterator();
                    while(iterator.hasNext())
                    {
                        String key = iterator.next();
                       s = s + key + " " + keyValue.get(key) +"\n";
                    }

                    s = s + "\n";
                }
            }
            myReader.close();
            System.out.println("Records has been updated successfully in the table !!");
            FileWriter myfile=new FileWriter("Data/" + username + "_" + tableName + ".txt");
            myfile.write(s);
            myfile.flush();
            myfile.close();
            if (!locker.removeLock(username, tableName)) {
                System.out.println("Failed to remove lock on table");
                return;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}