package com.example.user.android_assignment_16_1;
//Package objects contain version information about the implementation and specification of a Java package.
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //public is a method and fields can be accessed by the members of any class.
    //class is a collections of objects.
    //created MainActivity and extends with AppCompatActivity which is Parent class.

    private static final int WRITE_REQUEST_CODE = 50;
    EditText text;
    TextView content;
    Button add, delete;
    static String FILENAME = "test.txt";
    //creating a file
    File file;
    //declaring the variables.

    @RequiresApi(api = Build.VERSION_CODES.M)
    //requires the api version
    //Denotes that the annotated element should only be called on the given API level or higher.

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    protected void onCreate(Bundle savedInstanceState) {
        //protected can be accessed by within the package and class and subclasses
        //The Void class is an uninstantiable placeholder class to hold a reference to the Class object
        //representing the Java keyword void.
        //The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity
        // Activities have the ability, under special circumstances, to restore themselves to a previous state using the data stored in this bundle.
        super.onCreate(savedInstanceState);
        //Android class works in same.You are extending the Activity class which have onCreate(Bundle bundle) method in which meaningful code is written
        //So to execute that code in our defined activity. You have to use super.onCreate(bundle)
        setContentView(R.layout.activity_main);
        //R means Resource
        //layout means design
        //main is the xml you have created under res->layout->main.xml
        //Whenever you want to change your current Look of an Activity or when you move from one Activity to another .
        //he other Activity must have a design to show . So we call this method in onCreate and this is the second statement to set
        //the design.

        text = (EditText) findViewById(R.id.enter_data);
        content = (TextView) findViewById(R.id.show_data);
        add = (Button) findViewById(R.id.btn_add);
        delete = (Button) findViewById(R.id.btn_delete);
        //giving id of text content and buttons from the layout.

       if( ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
           // requestPermissions(permissions, WRITE_REQUEST_CODE);
       }
        file = new File(Environment.getExternalStorageDirectory(), FILENAME);
        // File creation
        try {
            if (file.createNewFile()){
                //creates the new file
                //A toast provides simple feedback about an operation in a small popup
                //Make a standard toast that just contains a text view with the text from a resource.

                //  Parameters
                //context	Context: The context to use. Usually your Application or Activity object.
                //resId	int: The resource id of the string resource to use. Can be formatted text.
                //duration	int: How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
//show() methid:will shows the msg
                Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            //if the file is not created
            //Prints this throwable and its backtrace to the standard error stream.
        }
        //update data to File
        //Register a callback to be invoked when this view is clicked. If this view is not clickable, it becomes clickable.
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            //we use override to tells the compiler that the following method overrides a method of its superclass.
            public void onClick(View view) {
                String string = text.getText().toString();
                text.setText("");
                //it will take the string value and read the filr
                ReadFile readFile = new ReadFile(file);
                readFile.execute(string);
                //executrs the string
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            //deleting the file
            @Override
            //we use override to tells the compiler that the following method overrides a method of its superclass.
            public void onClick(View view) {
                file = new File(Environment.getExternalStorageDirectory(), FILENAME);
                //creating new file that gets the data from external storage.
                boolean del = file.delete();
                if(del){
                    Toast.makeText(MainActivity.this, "file deleted...", Toast.LENGTH_SHORT).show();
                    //showing the message as the file is deleted.
                   content.setText("");
                }
            }
        });

    }
    public class ReadFile extends AsyncTask<String, Integer, String> {
        //this class is to read th file which is created
        File fileRead;

        private ReadFile(File fileRead) {
            super();
            this.fileRead=fileRead;

        }
        //update data to file
        @Override
        //we use override to tells the compiler that the following method overrides a method of its superclass.
        ////A user interface element that indicates the progress of an operation.
        //This method can be invoked from doInBackground(Params...) to publish updates on the UI thread while the background computation is still running.

        protected String doInBackground(String... strings) {
            String enter="\n";
            FileWriter filewriter=null;
            //he constructors of this class assume that the default character encoding and the default byte-buffer size are acceptable.
            try {
                filewriter=new FileWriter(fileRead,true);
                filewriter.append(strings[0].toString());
                //Constructs a FileWriter object given a file name with a boolean indicating whether or not to append the data written.
                filewriter.append(enter);
                filewriter.flush();
                filewriter.close();
                //it deletes the file
            } catch (IOException e) {
                e.printStackTrace();
            }
            return enter;
        }
        //read data from file
        @Override
        //we use override to tells the compiler that the following method overrides a method of its superclass.
        //Runs on the UI thread after doInBackground(Params...). The specified result is the value returned by doInBackground(Params...).
        protected void onPostExecute(String s) {
            //This method won't be invoked if the task was cancelled.
            super.onPostExecute(s);
            String name = s;
            StringBuilder stringBuilder = new StringBuilder();
            //class is used to create mutable (modifiable) string
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(fileRead);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                //Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
                while ((name = bufferedReader.readLine()) != null) {
                    //reads the line until it is not equal to null
                    stringBuilder.append(name);
                    stringBuilder.append("\n");

                }
                bufferedReader.close();
                fileReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            content.setText(stringBuilder.toString());
        }
    }
    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    //This interface is the contract for receiving the results for permission requests.
    //Parameters
    // requestCode	int: The request code passed in requestPermissions(android.app.Activity, String[], int)
    //permissions	String: The requested permissions. Never null.
    //grantResults	int: The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //giving the method of request permission.
        switch (requestCode) {
            //switching the case
            case 1:
                if(requestCode==1&&grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Class for retrieving various kinds of information related to the application packages that are currently installed on the device.
                    // You can find this class through getPackageManager().
                    //Granted.
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                    //giving a message that permission is granted.
                }
                else{
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    //giving a message as permission is denied.
                }
                break;
        }
    }
}
