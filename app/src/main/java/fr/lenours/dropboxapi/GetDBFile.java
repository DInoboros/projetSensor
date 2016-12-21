package fr.lenours.dropboxapi;


import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Permet de recuperer des fichiers présent sur Dropbox
 */

public class GetDBFile extends AsyncTask{

    private DropboxAPI dropboxAPI;
    private String path;
    private File file;

    /**
     *
     * @param dropboxAPI
     * @param file fichier de destination
     * @param path nom du fichier présent sur Dropbox
     */
    public GetDBFile(DropboxAPI dropboxAPI, File file, String path) {
        super();
        this.dropboxAPI = dropboxAPI;
        this.file = file;
        this.path = path;

    }

    @Override
    protected Object doInBackground(Object[] params) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            DropboxAPI.DropboxFileInfo dropboxFileInfo = dropboxAPI.getFile(file.getName(),null,fileOutputStream,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }


        return null;
    }

    public File getFile() {
        return file;
    }
}
