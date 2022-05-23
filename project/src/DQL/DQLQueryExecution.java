package DQL;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;


public class DQLQueryExecution {

    private String tableName="";

    public void selectTable(String username,String operation,String columns,String tableName,String conditions)
    {
        try
        {
            String[] conditionsArray = conditions.trim().split("=");
            File myObj = new File("Data/" + username + "_" + tableName + ".txt");
            Scanner myReader = new Scanner(myObj);
            if(conditionsArray.length != 0)
            {
                String s = "";
                Map<String, String> readMap = new HashMap<String, String>();
                String nextLine;
                while (myReader.hasNextLine())
                {
                    nextLine = myReader.nextLine();

                    while (nextLine.trim().length() > 0) {
                        String[] row = nextLine.trim().split(" ");
                        readMap.put(row[0], row[1]);
                        if (myReader.hasNextLine()) {
                            nextLine = myReader.nextLine();
                        } else {
                            nextLine = "";
                        }
                    }

                    if (nextLine.trim().length() == 0) {
                        String value = readMap.get(conditionsArray[0]);
                        if (value.trim().equalsIgnoreCase(conditionsArray[1].trim())) {
                            readMap.put(conditionsArray[0], conditionsArray[1]);
                            Iterator<String> iterator = readMap.keySet().iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                s = s + key + " " + readMap.get(key) + "\n";

                            }
                            s = s + "\n";
                        }
                    }
                }
                System.out.println(s);
            }else{
                while (myReader.hasNextLine())
                {
                    String data = myReader.nextLine();
                    System.out.println(data);
                }
            }
            myReader.close();

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }



    public void dump(String username) {

        try {
            File dataDictionary = new File("Data/UserTableDictionary.txt");
            FileReader readDictionary = new FileReader(dataDictionary);
            BufferedReader bufferedReader = new BufferedReader(readDictionary);
            File dumpfile = new File(username + "_data_dump.txt");
            FileWriter writeFile = new FileWriter(dumpfile, true);
            String readLine = bufferedReader.readLine();
            if (readLine.equalsIgnoreCase(username)) {
                readLine = bufferedReader.readLine();
                while (!readLine.isBlank()) {
                    writeFile.write(readLine);
                    writeFile.write("\n");
                    readLine = bufferedReader.readLine();
                }
                writeFile.write("\n");
            }
            bufferedReader.close();
            writeFile.close();
            readDictionary.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void generateERD(String username) {

        try {
            FileReader fileReader = new FileReader("Data/UserTableDictionary.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> dataColumns = new ArrayList<String>();
            String s = "";
            String line="";
            while( (line=bufferedReader.readLine())!=null) {
                dataColumns.add(line);
            }
            for( int i=0; i<dataColumns.size(); i++)
                {
                if (dataColumns.get(i).contains("fk"))
                {
                    s = s + dataColumns.get(i) +"\n";
                }
                }
            bufferedReader.close();
            System.out.println("ERD has been generated successfully !!");
            FileWriter myfile=new FileWriter("Data/" + username +"_ERD.txt");
            myfile.write(s);
            myfile.flush();
            myfile.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}