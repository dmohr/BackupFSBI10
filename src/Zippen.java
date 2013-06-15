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
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Dirk Mohr
 */
public class Zippen {
    protected int nFiles;
    protected int nDirectories;
    protected File fLog;
    protected FileWriter writerLog;
    protected String strLog;
        
    
    // ZIP-Datei gemäß Voreinstellungen und gewähltem Ordner erstellen
    public void macheZip(SicherungsObjekt neueSicherungQuellen, JProgressBar Fortschritt) throws IOException {
        ZeigeLog zeigeLog = new ZeigeLog(); // Instanz zum Anzeigen der Log-Datei
        int nFileCount = 0; // Zähler für Dateianzahl und zur Anzeige der Progressbar
        nFiles = 0;
        nDirectories = 0;
        strLog = "";

        // zu sichernde Ordner in String-Array
        String[] sicherungsQuellen = neueSicherungQuellen.getQuellpfade().toArray(new String[neueSicherungQuellen.getQuellpfade().size()]);
        
        // Zip-Pfad und Zip-Name zusammenbauen (mit Timestamp für eindeutigen Namen)
        String zipName = neueSicherungQuellen.getZielpfad() + "\\" + neueSicherungQuellen.getZieldatei() + System.currentTimeMillis() + ".zip";
        
        try {
            // String für Log-Datei 
            strLog += "Zip-Datei:";
            strLog += System.getProperty("line.separator");
            strLog += zipName;
            // Platformunabhängiger Zeilenumbruch wird in den Stream geschrieben
            strLog += System.getProperty("line.separator");
            strLog += System.getProperty("line.separator");
            strLog += "Inhalt:";
            strLog += System.getProperty("line.separator");
            
            // Anzahl Files bestimmen, um Maximum der Progressbar zu definieren
            for( String strQuelle: sicherungsQuellen )
            {
                File dirToZipFile = new File(strQuelle);
                nFileCount += getDirectoryFileCount(dirToZipFile);
            }
            
            Fortschritt.setMaximum(nFileCount);
            
            File f = new File(zipName); // Zip-File erzeugen
            System.out.println("Erzeuge Archiv " + f.getCanonicalPath());
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
                    f.getCanonicalPath()));
            zos.setLevel(neueSicherungQuellen.getKompression()); // Zip-Stream erzeugen

            for( String strQuelle: sicherungsQuellen ) // zu sichernde Ordner durchgehen
            {
                File dirToZipFile = new File(strQuelle); // Aus String mit Pfadangabe File-Objekt erzeugen
                
                zipDir(zipName, strQuelle, dirToZipFile, zos, Fortschritt); // Fille Objekt zum Zippen geben

            }

            // Log-String fertigstellen
            strLog += System.getProperty("line.separator");
            strLog += System.getProperty("line.separator");
            
            // Zahlen in Log schreiben
            strLog += "Gesichert: " + nFiles + " Dateien  " + nDirectories + " Unterordner";
            fLog = new File(zipName + ".log"); // Logdatei erzeugen
           
            writerLog = new FileWriter(fLog, false); // Filewriter zum Schreiben der Log-Datei
            writerLog.write(strLog); // Logstring in Filewriter schreiben
            
            writerLog.flush(); // Log-Datei physikalisch auf Datenträger schreiben
            writerLog.close(); // Log-Datei schließen
            
            zos.close(); // Zip-Stream schließen
            
            // Log anzeigen
            zeigeLog.LeseLog(zipName + ".log");
            zeigeLog.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zipDir(String zipName, String dirToZip, File dirToZipFile,
            ZipOutputStream zos, JProgressBar Fortschritt) {
        if (zipName == null || dirToZip == null || dirToZipFile == null
                || zos == null || !dirToZipFile.isDirectory())
            return;
        
        // Inhalt des Übergebenen Ordner durchgehen; wenn File => in Zip-Datei hinzupacken
        // Wenn es ein Ordner ist, ruft sich die Methode selbst auf, um alle Unterordner mit
        // Inhalt sichern zu können
        //FileInputStream fis = null;
        try {
            File[] fileArr = dirToZipFile.listFiles();
            String path;
            for (File f : fileArr) {
                if (f.isDirectory()) { // Ordner gefunden => Rekursion
                    nDirectories++;
                    zipDir(zipName, dirToZip, f, zos, Fortschritt);
                    continue;
                }
                // File => zippen
                path = f.getCanonicalPath(); // Pfad besorgen
                
                // Laufwerksbezeichnung aus Pfad abschneiden
                String name = path.substring(path.indexOf(f.separator)+1 , path.length());
                System.out.println("Packe " + name);
                
                // Eintrag in Logg
                strLog += name;
                // Platformunabhängiger Zeilenumbruch wird in den Stream geschrieben
                strLog += System.getProperty("line.separator");
                
                zipFile(zos, f, name, Fortschritt); // eigentlicher Zip-Vorgang aufrufen
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    private void zipFile(ZipOutputStream zos, File f, String name, JProgressBar Fortschritt)
    {        
        try {
            FileInputStream fis = new FileInputStream(f); // FileinputStream zum Schreiben

            zos.putNextEntry(new ZipEntry(name)); // Eintrag in Zip-Erzeugen
            nFiles++;
            Fortschritt.setValue(nFiles); // Prograssbar 1 weiter
            Fortschritt.paintImmediately(Fortschritt.getVisibleRect()); // Progressbar neu zeichnen
            
            // Datei in Zip-Datei packen
            int len;
            byte[] buffer = new byte[2048];
            while ((len = fis.read(buffer, 0, buffer.length)) > 0) {
                zos.write(buffer, 0, len);
            }
            fis.close(); // Inputstream schließen
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Methode zum entpacken
    public void extractArchive(String strArchive, List<String> strFileList, String strDestDir) throws Exception {
        File archive = new File(strArchive); // File-Objekt für Archivdatei erzeugen
        File destDir = new File(strDestDir); // File-Objekt für Zielordner
                
        // Wenn Zielornder noch nicht existiert = Anlegen
        if (!destDir.exists()) {
            destDir.mkdir();
        }
 
        ZipFile zipFile = new ZipFile(archive); 
        Enumeration entries = zipFile.entries();
 
        byte[] buffer = new byte[2048];
        int len;
        
        // Zip-File durchgehen
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
 
            String entryFileName = entry.getName();
            
            // Ist aktueller Eintrag der Zip-Datei in der Liste der zu entpackenden Dateien enthalten
            if (strFileList.contains(entryFileName))
            {            
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

        // Erfolgsmeldung
        JOptionPane.showMessageDialog(null, "Daten erfolgreich zurückgespielt.", "Restore erfolgreich", JOptionPane.INFORMATION_MESSAGE);
    }

    // Inhalt eines ZIP-Archievs auslesen und als String-Array zurückgeben
    public String[] leseZipinhalt(String strArchiv) throws Exception {
        File archive = new File(strArchiv);
        
       ArrayList<String> listStr = new ArrayList<String>();
 
        ZipFile zipFile = new ZipFile(archive);
        Enumeration entries = zipFile.entries();
         
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
 
            String entryFileName = entry.getName();
            
            String str = new String(entryFileName);
            listStr.add(str);

        }
        
        zipFile.close();
        String[] strArray = listStr.toArray(new String[listStr.size()]);
        
        return strArray;
    }
    
    private File buildDirectoryHierarchyFor(String entryName, File destDir) {
        int lastIndex = entryName.lastIndexOf(File.separator); //         int lastIndex = entryName.lastIndexOf("path.separator"); // '\\');

        String internalPathToEntry = entryName.substring(0, lastIndex + 1);
        return new File(destDir, internalPathToEntry);
    }
    
    // Anzahl der Files in einem Ordner, auch Dateien in Unterordnern auslesen
    public int getDirectoryFileCount(File dir)
    {
        int nFileCount = 0;
        
        File[] files = dir.listFiles();
        if (files != null) {
          for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
              nFileCount += getDirectoryFileCount(files[i]); // Filezahl summieren
            }
            else {
              nFileCount++;
            }
          }
        }
        return nFileCount;
    }

}
