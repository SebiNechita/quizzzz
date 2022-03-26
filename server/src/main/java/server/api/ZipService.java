package server.api;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ZipService {
    /**
     * Unzips uploaded.zip to the activity bank
     */
    public void unzip() throws IOException {
        String fileZip = "server/src/main/resources/uploaded.zip";
        File destDir = new File("server/src/main/resources/activity-bank");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    /**
     * Creates a new empty file in the specified directory, with the name of the given zip entry
     * @param destinationDir the directory to place the file in
     * @param zipEntry the zip entry to use the name of
     * @return the new file
     */
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    /**
     * Creates a zip file out of the given byte array
     * @param bytes the byte array to create the file out of
     */
    public void constructFile(byte[] bytes) throws IOException {
        File file = new File("server/src/main/resources/uploaded.zip");
        OutputStream
                os
                = new FileOutputStream(file);
        os.write(bytes);
        os.close();
    }
}
