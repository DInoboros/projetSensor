package fr.lenours.sensortracker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by potte on 09/01/2017.
 */

public class FileImportation extends Activity{

    Button buttonOpenDialog;
    Button buttonUp;
    TextView textFolder;

    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;

    public static File root ;
    public static File curFolder;
    public static File selected;

    private List<String> fileList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_import);

        buttonOpenDialog = (Button) findViewById(R.id.opendialog);
        if (buttonOpenDialog == null)
            System.out.println("C NULL FAUT FAIRE QUELQUE CHOSE");
        else{
            buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(CUSTOM_DIALOG_ID);
                }
            });
        }


        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;
    }

    @Override
    public Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(FileImportation.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Choisissez un fichier");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });

                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selected = new File(fileList.get(position));
                        if(selected.isDirectory()) {
                            ListDir(selected);
                        } else {
                            Toast.makeText(FileImportation.this, selected.toString() + " selected",Toast.LENGTH_LONG).show();
                            dismissDialog(CUSTOM_DIALOG_ID);
                            FileData.setTotalStep(selected);
                        }


                    }
                });

                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                break;
        }
    }

    void ListDir(File f) {
        if(f.equals(root)) {
            buttonUp.setEnabled(false);
        } else {
            buttonUp.setEnabled(true);
        }

        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();

        for(File file : files) {
            fileList.add(file.getPath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);
    }


}
