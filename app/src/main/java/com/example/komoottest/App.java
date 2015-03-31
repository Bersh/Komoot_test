package com.example.komoottest;

import android.app.Application;
import android.os.Environment;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 31.03.2015
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!Constants.IMAGES_FOLDER.isDirectory()) {
            Constants.IMAGES_FOLDER.mkdirs();
        }
    }

    public static List<String> getImageFileNames(File imagesFolder) {
        List<String> result = new ArrayList<>();
        if (imagesFolder.isDirectory()) {
            String[] children = imagesFolder.list();
            for (String aChildren : children) {
                result.add(new File(imagesFolder, aChildren).getAbsolutePath());
            }
        }
        return result;
    }

    public static String getNewFileName(File imagesFolder) {
        List<File> files = new ArrayList<>();
        String result = "1.png";
        if (imagesFolder.isDirectory()) {
            String[] children = imagesFolder.list();
            for (String aChildren : children) {
                files.add(new File(imagesFolder, aChildren));
            }
            if(!files.isEmpty()) {
                Collections.sort(files, new LastModifiedFileComparator());
                Collections.reverse(files);
                int currentFileNum = Integer.parseInt(files.get(0).getName().split("\\.")[0]);
                result = ++currentFileNum + ".png";
            }
        }
        return Constants.IMAGES_FOLDER_PATH + result;
    }
}
