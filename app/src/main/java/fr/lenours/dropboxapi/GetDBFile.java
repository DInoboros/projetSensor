package fr.lenours.dropboxapi;


import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Permet de recuperer des fichiers présent sur Dropbox
 * Modified on 12/01/2016
 * @author mvalier
 */

public class GetDBFile extends AsyncTask{

    private DropboxAPI<AndroidAuthSession> dropboxAPI;
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
    protected Object doInBackground(Object[] objects) {

        FileOutputStream fileOutputStream;
        DropboxAPI.DropboxFileInfo dropboxFile;
        try {
            fileOutputStream = new FileOutputStream(file);
            dropboxFile = dropboxAPI.getFile(file.getName(),null,fileOutputStream,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        System.out.println(file.getName());
        return null;
    }

    public File getFile() {
        return file;
    }


}
