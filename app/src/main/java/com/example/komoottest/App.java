package com.example.komoottest;

import android.app.Application;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
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

    /**
     * Get image files list sorted by date. The most recent - first
     *
     * @param imagesFolder
     * @return image files list sorted by date
     */
    private static List<File> getSortedImageFilesList(File imagesFolder) {
        List<File> files = new ArrayList<>();
        if (imagesFolder.isDirectory()) {
            String[] children = imagesFolder.list();
            for (String aChildren : children) {
                files.add(new File(imagesFolder, aChildren));
            }

            if (!files.isEmpty()) {
                Collections.sort(files, new LastModifiedFileComparator());
                Collections.reverse(files);
            }
        }
        return files;
    }

    /**
     * Get list of absolute path for all images as strings
     *
     * @param imagesFolder
     * @return list of absolute path for all images
     */
    public static List<String> getImageFilesPath(File imagesFolder) {
        List<String> result = new ArrayList<>();
        List<File> files = getSortedImageFilesList(imagesFolder);

        if (!files.isEmpty()) {
            for (File file : files) {
                result.add(file.getAbsolutePath());
            }
        }
        return result;
    }

    /**
     * Get file name for next file
     *
     * @param imagesFolder
     * @return file name for next file
     */
    public static String getNewFileName(File imagesFolder) {
        String result = "1.png";
        List<File> files = getSortedImageFilesList(imagesFolder);
        if (!files.isEmpty()) {
            int currentFileNum = Integer.parseInt(files.get(0).getName().split("\\.")[0]);
            result = ++currentFileNum + ".png";
        }

        return Constants.IMAGES_FOLDER_PATH + result;
    }
}
