package com.esilv.bankapp;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * Created by Philippe on 05/03/2018. Modified in order to fit the features of this project.
 * Taken from OpenClassRooms (https://openclassrooms.com/fr/courses/4561586-manage-your-data-to-have-a-100-offline-android-app/5770926-create-a-file-in-internal-storage)
 */

public class StorageUtils {

    private static File createOrGetFile(File destination, String fileName, String folderName){
        File folder = new File(destination, folderName);
        return new File(folder, fileName);
    }

    public static List<AccountsActivity.Account> getAccountsFromStorage(File rootDestination, Context context, String fileName, String folderName) throws IOException {
        File file = createOrGetFile(rootDestination, fileName, folderName);
        return readOnFile(context, file);
    }

    public static void setAccountsInStorage(File rootDestination, Context context, String fileName, String folderName, List<AccountsActivity.Account> accounts) throws IOException {
        File file = createOrGetFile(rootDestination, fileName, folderName);
        writeOnFile(context, accounts, file);
    }

    public static File getFileFromStorage(File rootDestination, Context context, String fileName, String folderName){
        return createOrGetFile(rootDestination, fileName, folderName);
    }

    private static List<AccountsActivity.Account> readOnFile(Context context, File file) throws IOException {

        List<AccountsActivity.Account> result = null;

        if (file.exists()) {

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            try {


                try {
                    result = (List<AccountsActivity.Account>) ois.readObject();
                }
                finally {
                    ois.close();
                }
            }
            catch (IOException | ClassNotFoundException e) {
                Toast.makeText(context.getApplicationContext(),
                        "Storage error: " + e, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        return result;
    }

    private static void writeOnFile(Context context, List<AccountsActivity.Account> accounts, File file){

        try {

            file.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            try {
                oos.writeObject(accounts);
                fos.getFD().sync();
            } finally {
                oos.close();
            }

        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(),
                    "Storage error: " + e, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}