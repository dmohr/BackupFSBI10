/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author FSBI10
 */
import com.demo.tree.checkbox.SicherungsObjekt;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author FSBI10
 */
public class Zippen {
    protected int nFiles;
    protected int nDirectories;
    protected File fLog;
    protected FileWriter writerLog;
    protected String strLog;
        
    
    
    public void macheZip(SicherungsObjekt neueSicherungQuellen) throws IOException {
        ZeigeLog zeigeLog = new ZeigeLog();
        nFiles = 0;
        nDirectories = 0;
        strLog = "";

        String[] sicherungsQuellen = neueSicherungQuellen.getQuellpfade().toArray(new String[neueSicherungQuellen.getQuellpfade().size()]);
        
        String zipName = neueSicherungQuellen.getZielpfad() + "\\" + System.currentTimeMillis() + ".zip"; //"d:\\zweitezip";
        
        try {
            strLog += zipName;
            // Platformunabhängiger Zeilenumbruch wird in den Stream geschrieben
            strLog += System.getProperty("line.separator");
            
            File f = new File(zipName);
            System.out.println("Erzeuge Archiv " + f.getCanonicalPath());
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
                    f.getCanonicalPath()));
            zos.setLevel(neueSicherungQuellen.getKompression());

            for( String strQuelle: sicherungsQuellen )
            {
                File dirToZipFile = new File(strQuelle);            

                zipDir(zipName, strQuelle, dirToZipFile, zos);

            }

            strLog += System.getProperty("line.separator");
            strLog += System.getProperty("line.separator");
            
            // Zahlen in Log schreiben
            //writerLog.write("Gesichert " + nFiles + "Dateien  " + nDirectories + "Unterordner");
            strLog += "Gesichert " + nFiles + "Dateien  " + nDirectories + "Unterordner";
            fLog = new File(zipName + ".log");
            // fLog = File.createTempFile("Log", ".txt");
           
            writerLog = new FileWriter(fLog, false);
            writerLog.write(strLog);
            
            writerLog.flush();
            writerLog.close();
            
            zos.close();
            
            // Log anzeigen
            zeigeLog.LeseLog(zipName + ".log");
            zeigeLog.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zipDir(String zipName, String dirToZip, File dirToZipFile,
            ZipOutputStream zos) {
        if (zipName == null || dirToZip == null || dirToZipFile == null
                || zos == null || !dirToZipFile.isDirectory())
            return;
        

        FileInputStream fis = null;
        try {
            File[] fileArr = dirToZipFile.listFiles();
            String path;
            for (File f : fileArr) {
                if (f.isDirectory()) {
                    nDirectories++;
                    zipDir(zipName, dirToZip, f, zos);
                    continue;
                }
                fis = new FileInputStream(f);
                path = f.getCanonicalPath();
                // path = f.getPath();
                
                // String name = path.substring(dirToZip.length(), path.length());
               
                String name = path.substring(path.indexOf(f.separator)+1 , path.length());
                System.out.println("Packe " + name);
                
                // Eintrag in Logg
                //writerLog.write(name);
                strLog += name;
                // Platformunabhängiger Zeilenumbruch wird in den Stream geschrieben
                //writerLog.write(System.getProperty("line.separator"));
                strLog += System.getProperty("line.separator");
                
                zipFile(zos, f, name);
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    private void zipFile(ZipOutputStream zos, File f, String name)
    {        
        try {
            FileInputStream fis = new FileInputStream(f);

            zos.putNextEntry(new ZipEntry(name));
            nFiles++;
            int len;
            byte[] buffer = new byte[2048];
            while ((len = fis.read(buffer, 0, buffer.length)) > 0) {
                zos.write(buffer, 0, len);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void extractArchive(String strArchive, List<String> strFileList, String strDestDir) throws Exception {
        File archive = new File(strArchive);
        File destDir = new File(strDestDir);
        
        // System.currentTimeMillis() + ".zip"
        
        if (!destDir.exists()) {
            destDir.mkdir();
        }
 
        ZipFile zipFile = new ZipFile(archive);
        Enumeration entries = zipFile.entries();
 
        byte[] buffer = new byte[2048];
        int len;
        
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
 
            String entryFileName = entry.getName();
            
            if (strFileList.contains(entryFileName))
            {            
                if ("Log.log".equals(entryFileName))
                {
                    continue;
                }

                File dir = buildDirectoryHierarchyFor(entryFileName, destDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                if (!entry.isDirectory()) {
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(new File(destDir, entryFileName)));

                    BufferedInputStream bis = new BufferedInputStream(zipFile
                            .getInputStream(entry));

                    while ((len = bis.read(buffer)) > 0) {
                        bos.write(buffer, 0, len);
                    }

                    bos.flush();
                    bos.close();
                    bis.close();
                }
            }
        }
        
        zipFile.close();
    }

    public String[] leseZipinhalt(String strArchiv) throws Exception {
        File archive = new File(strArchiv);
        
       ArrayList<String> listStr = new ArrayList<String>();
 
        ZipFile zipFile = new ZipFile(archive);
        Enumeration entries = zipFile.entries();
         
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
 
            String entryFileName = entry.getName();
            
            if ("Log.log".equals(entryFileName))
            {
                continue;
            }
            
            String str = new String(entryFileName);
            listStr.add(str);

        }
        
        zipFile.close();
        String[] strArray = listStr.toArray(new String[listStr.size()]);
        
        return strArray;
    }
    
    private File buildDirectoryHierarchyFor(String entryName, File destDir) {
        //int lastIndex = entryName.lastIndexOf('/'); // Original
        int lastIndex = entryName.lastIndexOf(File.separator); //         int lastIndex = entryName.lastIndexOf("path.separator"); // '\\');

       // String entryFileName = entryName.substring(lastIndex + 1);
        String internalPathToEntry = entryName.substring(0, lastIndex + 1);
        return new File(destDir, internalPathToEntry);
    }

}
