package server.api;

import commons.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ZipService {
    /**
     * Unzips uploaded.zip to the activity bank
     * @param byteArray byte array of the uploaded zip
     */
    public void unzip(byte[] byteArray) throws IOException {
//        File fileZip = null;
        File destDir = null;
        try {
//            fileZip = new File(
//                    Objects.requireNonNull(getClass().getClassLoader().getResource("uploaded.zip")).toURI()
//            );
            Path destParentPath = Path.of(Objects.requireNonNull(getClass().getClassLoader().getResource("")).toURI());
            destDir = new File(
                     destParentPath.toString()
            );
            if (destDir.mkdir()) {
                LoggerUtil.infoInline("Created Directory: " + destDir.getAbsolutePath());
            }
            LoggerUtil.infoInline("Storing the activity-bank in: " + destDir.getAbsolutePath());
            byte[] buffer = new byte[1024];
//            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(byteArray));
            ZipEntry zipEntry = zis.getNextEntry();
            Path toBeRenamed = Path.of(newFile(destDir, zipEntry).toURI());
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
            if (renameFile(new File(toBeRenamed.toUri()))) {
                LoggerUtil.infoInline("Renamed file to activity-bank");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renames the file name to "activity-bank"
     * This is required because the application searches for activity in a directory named "activity-bank"
     * @param file file to be renamed
     * @return true if rename was successful; false otherwise
     */
    private boolean renameFile(File file) {
        File renamedFile = new File(file.getParent() + "/activity-bank");
        return file.renameTo(renamedFile);
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
        File file = null;
        try {
            Path path = Path.of(Objects.requireNonNull(getClass().getClassLoader().getResource("")).toURI());
            file = new File(
                    path.toString(), "uploaded.zip"
            );
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }
}
