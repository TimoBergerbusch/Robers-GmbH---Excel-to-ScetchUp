/*
 * Copyright (c) 2014, Timo Bergerbusch
 * Alle Rechte vorbehalten.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * Der <code>FileHandler</code> dient zum schreiben und lesen aus Dateien. Es
 * wird gespeichert in dem <code>key=value</code> Format mit einem Prefix.
 *
 * @author Timo Bergerbusch
 * @version v1.0
 */
public class FileHandler {

    /**
     * Die Datei
     */
    private File file;
    /**
     * Die Daten des <code>Files</code>
     *
     * @see file
     */
    private Properties p;
    /**
     * Der Header der in die Datei geschrieben wird.
     */
    private String prefix = "Don't touch this file, else the application might break!";

    /**
     * Der Konstruktor erzeugt eine gültige Instanz.
     *
     * @param file Der Pfad des zu öffnenden <code>File</code>'s
     */
    public FileHandler(String file) {
        p = new Properties();
        this.file = new File(file);
        try {
            p.load(new FileInputStream(file));
        } catch (IOException ex) {
            try {
                p.store(new FileOutputStream(file), prefix);
            } catch (IOException ex1) {
                JOptionPane.showMessageDialog(null, ex.toString(), ex.getLocalizedMessage(), JOptionPane.WARNING_MESSAGE);
                System.exit(1);
            }
        }
    }

    /**
     * Der Konstruktor erzeugt eine gültige Instanz.
     *
     * @param file Das zu öffnende <code>File</code>
     */
    public FileHandler(File f) {
        p = new Properties();
        this.file = f;
        try {
            p.load(new FileInputStream(file));
        } catch (IOException ex) {
            try {
                p.store(new FileOutputStream(file), prefix);
            } catch (IOException ex1) {
                JOptionPane.showMessageDialog(null, ex.toString(), ex.getLocalizedMessage(), JOptionPane.WARNING_MESSAGE);
                System.exit(1);
            }
        }
    }

    /**
     * Der Key wirdmit samt <code>Value</code> gelöscht.
     *
     * @param key Der zu löschende <code>Key</code>
     */
    public void remove(String key) {
        try {
            p.remove(key);
        } catch (NullPointerException e) {
            System.out.println("Key: " + key);
        }
    }

    /**
     * Ein Datensatz wird in das File geschrieben.
     *
     * @param key   Der unique <code>Key</code>
     * @param value Der <code>Wert</code> zum <code>Key</code>
     * @see File
     */
    public void set(String key, String value) {
        try {
            p.setProperty(key, value);
            //this.saveINI();
        } catch (NullPointerException e) {
            System.out.println("Key: " + key + "\n" + "Value: " + value);
        }
    }

    /**
     * Ein Wert wird aus dem <code>File gelesen</code>.
     *
     * @param key Der <code>Key</code>, dessen <code>Wert</code> gelesen werden
     *            soll
     * @return Der Wert des <code>Key</code>'s
     */
    public String read(String key) {
        return p.getProperty(key);
    }

    /**
     * Zum ändern des Prefix
     *
     * @param prefix Der neue Prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Gibt alle Keys wieder
     *
     * @return Eine ArrayList mit allen Keys
     * @see read(String key)
     * @see set(String key,String value)
     */
    public ArrayList getKeys() {
        return Collections.list(p.keys());
    }

    /**
     * Gibt die Datei zurück
     *
     * @return Die aktuelle Datei
     */
    public File getFile() {
        return file;
    }

    /**
     * Speichert die Datei
     */
    public void save() {
        try {
            p.store(new FileOutputStream(file), "Don't touch this file, else the application might break!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), ex.getLocalizedMessage(), JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Beendet die Nutzung eines Streams und lässt den GarbageCollector
     * aufräumen.
     */
    public void end() {
        this.save();
        p = null;
        Runtime.getRuntime().gc();
    }
}
