package fr.lenours.dropboxapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

public class UploadFile extends AsyncTask<Void, Void, Boolean>{

    private DropboxAPI dropboxApi;
    private File file;
    private Context context;

    public UploadFile(Context context, DropboxAPI dropboxApi, File file) {
        super();
        this.dropboxApi = dropboxApi;
        this.file = file;
        this.context = context;
    }



    @Override
    protected Boolean doInBackground(Void... params) {

        final File tempDropboxDirectory = context.getCacheDir();
        File tempFileToUpload;
        FileWriter fileWriter = null;

        try{
            dropboxApi.delete(file.getName());
        } catch (DropboxException de) {
            Log.i("e",file.getName()  + " erreur de suppression " + de.getMessage());
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            dropboxApi.putFile(file.getName(), fileInputStream,
                  file.length(), null, null);

            return true;
        } catch (IOException ioe) {

        } catch (DropboxException de) {
            // TODO: handle exception
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result) {
            Toast.makeText(context, file.getName() + " a été enregistré",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, file.getName() + " n'a pas été enregistré",
                    Toast.LENGTH_LONG).show();
        }
    }
}
