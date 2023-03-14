package jwallet.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//import javax.json.JsonArray;

import org.json.*;

/**
 * Application de gestion de mot de passe pour applications Web.
 * 
 * @author Nicolas Le Sommer
 * @version 1.0
 */

public class JWalletFrame extends javax.swing.JFrame {

    private javax.swing.JMenuItem addMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenuItem removeMenuItem;
    private javax.swing.JMenuItem updateMenuItem;
    private javax.swing.JTable table;
    private javax.swing.table.DefaultTableModel model;
    private Vector<String> data;

    /**
     * Creates new form MainFrame
     */
    public JWalletFrame() {
        initComponents();
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        quitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        addMenuItem = new javax.swing.JMenuItem();
        removeMenuItem = new javax.swing.JMenuItem();
        updateMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.data = new Vector<>();
        Vector<String> headers = new Vector<>();
        headers.add("URL");
        headers.add("Identifiant");
        headers.add("Mot de passe");
        headers.add("Commentaire");
        this.model = new javax.swing.table.DefaultTableModel(headers, 0);

        table.setModel(this.model);

        /////////////////////////////////////////////////////////////////////////////////////////////
        // Ajouté par Yoann Dewilde
        // Charger JSON dès le lancement
        try {
            File f = new File("data/save.json");
            this.readJsonFile(f);
        } catch (IOException e) {
            System.err.println(e);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////

        jScrollPane1.setViewportView(table);

        fileMenu.setText("Fichier");

        quitMenuItem.setText("Quitter");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edition");

        addMenuItem.setText("Ajouter");
        addMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(addMenuItem);

        updateMenuItem.setText("Modifier");
        updateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(updateMenuItem);

        removeMenuItem.setText("Supprimer");
        removeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(removeMenuItem);

        menuBar.add(editMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 287,
                Short.MAX_VALUE));

        pack();
    }

    // --------------------------------------------------------------------------
    // Action listeners
    // --------------------------------------------------------------------------

    private void addMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        model.addRow(new String[] { null, null, null, null });

        File f = new File("data/save.json");
        try {
            this.writeJsonFile(f);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void updateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        File f = new File("data/save.json");
        try {
            this.writeJsonFile(f);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void removeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO : compléter le code de la méthode pour supprimer un élément.
        // persister les données dans le fichier JSON
        int selectedRow = table.getSelectedRow();
        model.removeRow(selectedRow);

        File f = new File("data/save.json");
        try {
            this.writeJsonFile(f);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void clearTable() {
        int originalLen = this.model.getRowCount();
        for (int i = 0; i < originalLen; i++) {
            this.model.removeRow(0);
        }
    }

    // --------------------------------------------------------------------------
    // Gestion de la persistence des données dans un fichier JSON
    // --------------------------------------------------------------------------
    private String readFileToString(File f) throws IOException {
        Path path = Paths.get(f.getPath());

        return Files.readString(path);
    }

    private void readJsonFile(File f) throws IOException {
        String content = this.readFileToString(f);
        JSONArray array = new JSONArray(content);

        this.clearTable();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonRow = array.getJSONObject(i);
            String url = (String) jsonRow.get("url");
            String login = (String) jsonRow.get("login");
            String pass = (String) jsonRow.get("pass");
            String comment = (String) jsonRow.get("comment");

            this.model.addRow(new Object[] { url, login, pass, comment });
        }
    }

    private JSONArray convertToJson() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < this.model.getRowCount(); i++) {
            String url = (String) this.model.getValueAt(i, 0);
            String login = (String) this.model.getValueAt(i, 1);
            String pass = (String) this.model.getValueAt(i, 2);
            String comment = (String) this.model.getValueAt(i, 3);

            HashMap<String, String> rowContent = new HashMap<>();
            rowContent.put("url", url);
            rowContent.put("login", login);
            rowContent.put("pass", pass);
            rowContent.put("comment", comment);

            JSONObject jsonRow = new JSONObject(rowContent);
            array.put(jsonRow);
        }

        return array;
    }

    private void writeJsonFile(File f) throws IOException {
        JSONArray jsonData = this.convertToJson();

        try (FileWriter fw = new FileWriter(f)) {
            fw.write(jsonData.toString());
        } catch (IOException e) {
            throw e;
        }
    }

    // --------------------------------------------------------------------------
    // Méthode main
    // --------------------------------------------------------------------------

    public static void main(String args[]) {
        // JSONArray json = new JSONArray("[\"hello\", 5]");
        // System.out.println(json);

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JWalletFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JWalletFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JWalletFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JWalletFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JWalletFrame().setVisible(true);
            }
        });
    }
}
