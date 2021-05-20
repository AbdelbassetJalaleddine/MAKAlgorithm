package algorithm.mak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;
import org.simpleframework.xml.stream.Style;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

     Spinner editDistanceSpinner;

     EditText editTextInsert;
     EditText editTextUpdate;
     EditText editTextDelete;

     AutoCompleteTextView autoCompleteFirstRNA;
    AutoCompleteTextView autoCompleteSecondRNA;

     EditText editTextXMLName;

     SwitchCompat switchEditDistance,switchEditScript,switchPatch,switchSimilarity;

    public static double[][] arr;
    public static int insertWeight;
    public static int deleteWeight;
    public static int updateWeight;
    public static double ins, del, upd;
    public static ArrayList<editOperation> optimizedList;
    public static ArrayList<editOperation> operationsList;
    static SummarizedXML summarizedXML;
    static FullXML fullXML;

    String fullEditScript;

    static int w =0;
    static int k =0;

    String patchString;
    static String steps = "";


    public final static String valid = "GUACRMSVN";

    static boolean RNAsequence1 = false,RNAsequence2 = false;

    private static final String[] RNAs = new String[] {
            "AGAAGACAUUCGUGGARARA", "ACGCCUCCACGUAGUGUCUU", "AGAAGACAUUCGUGGAGGCGUC", "GUAACUVNGGASMVGGAUCR", "GUAACUAUGGACAAGGAUCG"
    };

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchSimilarity = findViewById(R.id.switchSimilarity);
        switchPatch = findViewById(R.id.switchPatch);
        switchEditScript = findViewById(R.id.switchEditScript);
        switchEditDistance = findViewById(R.id.switchEditDistance);

        editDistanceSpinner = findViewById(R.id.spinner);

        editTextInsert = findViewById(R.id.editTextInsert);
        editTextUpdate = findViewById(R.id.editTextUpdate);
        editTextDelete = findViewById(R.id.editTextDelete);

        autoCompleteFirstRNA = findViewById(R.id.autoCompleteFirstRNA);
        autoCompleteSecondRNA = findViewById(R.id.autoCompleteSecondRNA);

        editTextXMLName = findViewById(R.id.editTextXMLName);

        spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, RNAs);

        autoCompleteFirstRNA.setAdapter(adapter);

        autoCompleteSecondRNA.setAdapter(adapter);

        autoCompleteFirstRNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteFirstRNA.showDropDown();
            }
        });

        autoCompleteSecondRNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteSecondRNA.showDropDown();
            }
        });
        if (isStoragePermissionGranted()) {
            //File write logic here
            Toast.makeText(getApplicationContext(), "Granted", Toast.LENGTH_SHORT).show();
        }

    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void Start(View view) {

        steps ="";
        String str1 = autoCompleteFirstRNA.getText().toString().toUpperCase();
        String str2 = autoCompleteSecondRNA.getText().toString().toUpperCase();

                RNAsequence1 = validateString(str1);

                RNAsequence2 = validateString(str2);

                if(!RNAsequence2 || !RNAsequence1){
                    Toast.makeText(getApplicationContext(), "Invalid RNA Sequences Please Enter them again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spinner.getSelectedItem().equals("B -> A")){
                    String temp = str1;
                    str1 = str2;
                    str2 = temp;
                }

        summarizedXML = new SummarizedXML();
        fullXML = new FullXML();


        String nameXML = editTextXMLName.getText().toString();

        summarizedXML.setFromString(str1);
        summarizedXML.setToString(str2);

        fullXML.setFromString(str1);
        fullXML.setToString(str2);

        insertWeight = Integer.parseInt(editTextInsert.getText().toString());

        deleteWeight = Integer.parseInt(editTextInsert.getText().toString());

        updateWeight = Integer.parseInt(editTextInsert.getText().toString());


        double editDist = getDistance(str1.toUpperCase(), str2.toUpperCase());
        double similarity = 1/(1+editDist);

        summarizedXML.setEditDistance(editDist);
        summarizedXML.setSimilarity(similarity);
        summarizedXML.setWeightofInsert(insertWeight);
        summarizedXML.setWeightofDelete(deleteWeight);
        summarizedXML.setWeightofUpdate(updateWeight);

        fullXML.setEditDistance(editDist);
        fullXML.setSimilarity(similarity);
        fullXML.setWeightofInsert(insertWeight);
        fullXML.setWeightofDelete(deleteWeight);
        fullXML.setWeightofUpdate(updateWeight);

        System.out.println("The Edit Distance is: " + editDist);
        System.out.println("The Similarity measure is: " + similarity);

        optimizedList = new ArrayList<>();
        operationsList = new ArrayList<>();

        getEditString(str1, str2, str1.length(), str2.length());

        System.out.print("The Edit Script is: \n<");


        Collections.reverse(operationsList);

        int s =0;
        for (Object o: operationsList){
            s++;
            if(operationsList.size() - s != 0){
                System.out.print(o + ", ");
            }
            else{
                System.out.print(o);
            }

        }

        System.out.print(">");

        //Get Optimal Edit Script
        System.out.print("\nThe Optimal Edit Script is: \n< ");

        Collections.reverse(optimizedList);
        fullEditScript = "";

        fullEditScript = "<";
        int i =0;
        for (Object o: optimizedList) {
            i++;
            if(optimizedList.size() - i != 0){
                fullEditScript = fullEditScript + o + ",";
                System.out.print(o + ", ");
            }
            else{
                fullEditScript = fullEditScript + o ;
                System.out.print(o);
            }

        }
        System.out.println(">");
        fullEditScript = fullEditScript + ">";
        patchString = patching(str1);
        System.out.println(patchString);


        while(!optimizedList.isEmpty()){ // 1 = Insert , 2 = Delete , 3 = Update
            w = w + 1;
            if(optimizedList.get(0).operation == 1 ) {
                Insert insert = new Insert();
                insert.setValue(optimizedList.get(0).value + "");
                insert.setOrder(w);
                insert.setDestination(optimizedList.get(0).destination);
                insert.setSource(optimizedList.get(0).source);
                summarizedXML.addInsert(insert);
            }
            if(optimizedList.get(0).operation == 2){
                Delete delete = new Delete();
                delete.setOrder(w);
                delete.setDestination(optimizedList.get(0).destination);
                delete.setValue(optimizedList.get(0).value + "");
                summarizedXML.addDelete(delete);
            }
            if(optimizedList.get(0).operation == 3){
                Update update = new Update();
                update.setOrder(w);
                update.setSource(optimizedList.get(0).source);
                update.setDestination(optimizedList.get(0).destination);
                update.setValue(optimizedList.get(0).value + "");
                summarizedXML.addUpdate(update);
            }

            optimizedList.remove(0);
        }
        while(!operationsList.isEmpty()){ // 1 = Insert , 2 = Delete , 3 = Update
            k = k + 1;
            if(operationsList.get(0).operation == 1 ) {
                Insert insert = new Insert();
                insert.setValue(operationsList.get(0).value + "");
                insert.setOrder(k);
                insert.setDestination(operationsList.get(0).destination);
                insert.setSource(operationsList.get(0).source);
                fullXML.addInsert(insert);
            }
            if(operationsList.get(0).operation == 2){
                Delete delete = new Delete();
                delete.setOrder(k);
                delete.setDestination(operationsList.get(0).destination);
                delete.setValue(operationsList.get(0).value + "");
                fullXML.addDelete(delete);
            }
            if(operationsList.get(0).operation == 3){
                Update update = new Update();
                update.setOrder(k);
                update.setSource(operationsList.get(0).source);
                update.setDestination(operationsList.get(0).destination);
                update.setValue(operationsList.get(0).value + "");
                fullXML.addUpdate(update);
            }

            operationsList.remove(0);
        }
        try {
            marshalSummarized(nameXML);
            marshalFull(nameXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getBaseContext(), InfoActivity.class);

        if(switchEditDistance.isChecked()){
            intent.putExtra("editDistance", editDist);
        }
        if(switchEditScript.isChecked()){
            intent.putExtra("editScript", fullEditScript);
        }
        if(switchPatch.isChecked()){
            intent.putExtra("patch", steps);
        }
        if(switchSimilarity.isChecked()){
            intent.putExtra("similarity", similarity);
        }
        intent.putExtra("str1",str1);
        intent.putExtra("str2",str2);


        startActivity(intent);
    }

    public static boolean validateString(String s1) {

        for (int i = 0; i < s1.length(); i++) {
            if (valid.indexOf(s1.charAt(i)) != -1)
                continue;
            else
                return false;
        }
        return true;
    }


    private void marshalFull(String nameXML) throws Exception {

        File result = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/Full-"+ nameXML + ".xml");

        Style style = new HyphenStyle();
        Format format = new Format(style);
        Serializer serializer = new Persister(format);

        serializer.write(fullXML, result);
    }

    private void marshalSummarized(String nameXML) throws Exception {

        File result = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/Summarized-"+ nameXML + ".xml");

        Style style = new HyphenStyle();
        Format format = new Format(style);
        Serializer serializer = new Persister(format);

        serializer.write(summarizedXML, result);

        Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_SHORT).show();

    }


    public static double getDistance(String str1, String str2) {

        //Getting the length of each string
        int l1 = str1.length();
        int l2 = str2.length();

        //If one of the strings is empty, return the length of the other
        if (l1 == 0) return l2;
        if (l2 == 0) return l1;

        //Creating the "table"
        arr = new double[l1 + 1][l2 + 1];

        for (int i = 0; i <= l1; i++) arr[i][0] = i*insertWeight; //Insertion at row 0
        for (int i = 0; i <= l2; i++) arr[0][i] = i*deleteWeight; //Deletion at column 0

        //Operations
        for (int i = 1; i <= l1; i++) {
            char ch1 = str1.charAt(i - 1);

            for (int j = 1; j <= l2; j++) {
                char ch2 = str2.charAt(j - 1);

                switch (ch1) {
                    case 'R':
                        if (ch2 == 'G' || ch2 == 'A' || ch2 == 'R') {
                            calculate(0.5, i, j); // Expected distance of 0.5
                        } else if (ch2 == 'M' || ch2 == 'S' || ch2 == 'N') {
                            calculate(0.75, i, j); // Expected distance of 0.75
                        } else if (ch2 == 'V') {
                            calculate(0.67, i, j);// Expected distance of 2/3
                        } else calculate(1, i, j); // Default case of 1
                        break;

                    case 'M':
                        if (ch2 == 'C' || ch2 == 'A' || ch2 == 'm' || ch2 == 'M') {
                            calculate(0.5, i, j);// Expected distance of 0.5
                        } else if (ch2 == 'r' || ch2 == 'R' || ch2 == 's' || ch2 == 'S' || ch2 == 'n' || ch2 == 'N')
                            calculate(0.75, i, j);// Expected distance of 0.75

                        else if (ch2 == 'v' || ch2 == 'V') {
                            calculate(0.67, i, j);// Expected distance of 2/3
                        } else calculate(1, i, j);// Default case of 1
                        break;

                    case 'S':
                        if (ch2 == 'G' || ch2 == 'S' || ch2 == 'C') {
                            calculate(0.5, i, j);// Expected distance of 0.5
                        } else if (ch2 == 'R' || ch2 == 'M' || ch2 == 'N')
                            calculate(0.75, i, j);// Expected distance of 0.75
                        else if (ch2 == 'V') {
                            calculate(0.67, i, j);// Expected distance of 2/3
                        } else calculate(1, i, j);// Default case of 1

                        break;

                    case 'V':
                        if (ch2 == 'S' || ch2 == 'R' || ch2 == 'M' || ch2 == 'G' || ch2 == 'A' || ch2 == 'N' || ch2 == 'C') {
                            calculate(0.67, i, j); // Expected distance of 2/3
                        } else if (ch2 == 'V') {
                            calculate(0.75, i, j); // Expected distance of 0.75
                        } else {
                            calculate(1, i, j); // Default case of 1
                        }
                        break;

                    case 'N':
                        if (ch2 == 'G' || ch2 == 'A' || ch2 == 'S' || ch2 == 'C' || ch2 == 'N' || ch2 == 'U' || ch2 == 'R' ||
                                ch2 == 'M' || ch2 == 'V') {
                            calculate(0.75, i, j);// Expected distance of 0.75
                        } else {
                            calculate(1, i, j);// Default case of 1
                        }
                        break;

                    default: // If none of the characters above are mentioned, go through the normal case

                        switch (ch2) {
                            case 'R':
                                if (ch1 == 'G' || ch1 == 'A' || ch1 == 'R') {
                                    calculate(0.5, i, j); // Expected distance of 0.5
                                } else if (ch1 == 'M' || ch1 == 'S' || ch1 == 'N') {
                                    calculate(0.75, i, j); // Expected distance of 0.75
                                } else if (ch1 == 'V') {
                                    calculate(0.67, i, j);// Expected distance of 2/3
                                } else calculate(1, i, j); // Default case of 1
                                break;

                            case 'M':
                                if (ch1 == 'C' || ch1 == 'A' || ch1 == 'm' || ch1 == 'M') {
                                    calculate(0.5, i, j);// Expected distance of 0.5
                                } else if (ch1 == 'r' || ch1 == 'R' || ch1 == 's' || ch2 == 'S' || ch1 == 'n' || ch1 == 'N')
                                    calculate(0.75, i, j);// Expected distance of 0.75

                                else if (ch1 == 'v' || ch1 == 'V') {
                                    calculate(0.67, i, j);// Expected distance of 2/3
                                } else calculate(1, i, j);// Default case of 1
                                break;

                            case 'S':
                                if (ch1 == 'G' || ch1 == 'S' || ch1 == 'C') {
                                    calculate(0.5, i, j);// Expected distance of 0.5
                                } else if (ch1	 == 'R' || ch1 == 'M' || ch1 == 'N')
                                    calculate(0.75, i, j);// Expected distance of 0.75
                                else if (ch2 == 'V') {
                                    calculate(0.67, i, j);// Expected distance of 2/3
                                } else calculate(1, i, j);// Default case of 1

                                break;

                            case 'V':
                                if (ch1 == 'S' || ch1 == 'R' || ch1 == 'M' || ch1 == 'G' || ch1 == 'A' || ch1 == 'N' || ch1 == 'C') {
                                    calculate(0.67, i, j); // Expected distance of 2/3
                                } else if (ch1 == 'V') {
                                    calculate(0.75, i, j); // Expected distance of 0.75
                                } else {
                                    calculate(1, i, j); // Default case of 1
                                }
                                break;

                            case 'N':
                                if (ch1 == 'G' || ch1 == 'A' || ch1 == 'S' || ch1 == 'C' || ch1 == 'N' || ch1 == 'U' || ch1 == 'R' ||
                                        ch1 == 'M' || ch1 == 'V') {
                                    calculate(0.75, i, j);// Expected distance of 0.75
                                } else {
                                    calculate(1, i, j);// Default case of 1
                                }
                                break;

                            default:
                                if (ch1 == ch2)
                                    calculate(0, i, j);
                                else calculate(1, i, j);
                        }
                }
            }
        }

        // converting each row to a string
        // and then printing in a separate line
        for (double[] row : arr) System.out.println(Arrays.toString(row));
        return arr[l1][l2];

    }

    public static void calculate(double weight, int i, int j) {
        //In order to avoid decimals in the array, we work with the LCM of 3 and 4, which is 12
        // 12 -> full weight

        if(weight == 0){
            ins = (arr[i][j-1] + insertWeight * 12);
            del = (arr[i-1][j] + deleteWeight * 12);
            upd = (arr[i - 1][j - 1]);
        }
        else {
            ins = (arr[i - 1][j] + insertWeight * weight);
            del = (arr[i][j - 1] + deleteWeight * weight);
            upd = (arr[i - 1][j - 1] + weight * updateWeight);
        }


        //Insert
        if (ins < del && ins < upd) {
            arr[i][j] = ins;
        }

        //Delete
        else if (del < upd) {
            arr[i][j] = del;
        }

        //Update
        else {
            arr[i][j] = upd;
        }
    }

    public static void getEditString(String A, String B, int row, int column){

        double a = arr[row][column];

        //Base Case
        if (row == 0 && column == 0)
            return;

        //When reaching row = 0, but column is still not 0
        if(row == 0){

            //Check if Insert operation
            if(arr[row][column-1]<a)
            {
                optimizedList.add(new editOperation(1, column, row + 1, B.charAt(column-1)));
                operationsList.add(new editOperation(1, column, row + 1, B.charAt(column-1)));
                //Move horizontally
                getEditString(A, B, row, column - 1);
            }
            return;
        }

        //When reaching column = 0, but row is still not 0
        if(column == 0){

            //Check if Delete operation
            if (arr[row-1][column]<a)
            {
                optimizedList.add(new editOperation(row, A.charAt(row-1)));
                operationsList.add(new editOperation(row, A.charAt(row-1)));
                //Move vertically
                getEditString(A,B,row-1,column);
            }
            return;
        }

        double updateCost = arr[row-1][column-1];
        double deleteCost = arr[row-1][column];
        double insertCost = arr[row][column-1];

        //Check if delete
        if (deleteCost < insertCost && deleteCost< updateCost)
        {
            optimizedList.add(new editOperation(row, A.charAt(row-1)));
            operationsList.add(new editOperation(row, A.charAt(row-1)));
            //Move vertically
            getEditString(A,B,row-1,column);
        }


        //Check if Insert
        else if(insertCost<updateCost)
        {
            optimizedList.add(new editOperation(1, column, row + 1, B.charAt(column-1)));
            operationsList.add(new editOperation(1, column, row + 1, B.charAt(column-1)));
            //Move horizontally
            getEditString(A, B, row, column - 1);
        }

        //Check if Update
        else
        {
            //Either update to a new character or same character
            //Only add the character that is changing to the optimal edit script
            if (A.charAt(row-1) != B.charAt(column-1)) {
                optimizedList.add(new editOperation(3, column, row, B.charAt(column-1)));

            }
            operationsList.add(new editOperation(3, column, row, B.charAt(column-1)));
            getEditString(A,B,row-1,column-1);
        }
    }
    public static String patching(String str1) {
        StringBuilder sb = new StringBuilder (str1);
        steps += sb.toString() + "\n";
        int offset = 0;// use offset to keep tabs on insertion and deletion of characters

        //Loop through the optimizedList
        for(int i = 0; i<optimizedList.size(); i++) {

            editOperation temp = optimizedList.get(i);
            int location = temp.destination - 1 + offset; // index where the operation is going to happen

            if(temp.operation == 2) { // Delete

                sb.deleteCharAt(location);
                steps += sb.toString() + "\n";
                --offset;//decrement offset to account for removed character
            }

            else if(temp.operation == 1) {// Insert

                sb.insert(location, temp.value);
                steps += sb.toString() + "\n";
                ++offset;// increment offset to account for added character
            }
            else if (temp.operation == 3){ //Update
                sb.setCharAt(location, temp.value);
                steps += sb.toString() + "\n";
            }
        }

        return sb.toString();
    }

}