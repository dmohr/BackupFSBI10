package com.demo.tree.checkbox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

// Gefunden und angepasst von Peter Riehm

public class FileTreeViewer  extends JDialog {

private static final long serialVersionUID = 1L;
public static final ImageIcon ICON_COMPUTER =  new ImageIcon("");
public static final ImageIcon ICON_DISK =  new ImageIcon("");
public static final ImageIcon ICON_FOLDER =   new ImageIcon("");
public static final ImageIcon ICON_EXPANDEDFOLDER =  new ImageIcon("");

protected JTree  m_tree;
protected DefaultTreeModel m_model;

AddCheckBoxToTree AddCh = new AddCheckBoxToTree();

private AddCheckBoxToTree.CheckTreeManager checkTreeManager;


protected TreePath m_clickedPath;

private SicherungsObjekt neueSicherungQellen;

public FileTreeViewer()
{
    
}

// Konstruktor überladen, um eigene Buttons hinzufügen zu können

public FileTreeViewer(SicherungsObjekt neueSicherung)
{
    initComponents();
    
    neueSicherungQellen = neueSicherung;
    
    setTitle("Bitte wählen Sie die zu sichernden Ordner aus...");
    setResizable(false);
    setSize(400, 600);
    setAlwaysOnTop(true);
    
    SimplePanel panel = new SimplePanel();
    panel.setLayout(null);
    panel.setBackground(Color.lightGray);
    Container cp = getContentPane();
    cp.add(panel);

    WindowListener wndCloser = new WindowAdapter()
    {
        public void windowClosing(WindowEvent e){
            //setVisible(false); //you can't see me!
            dispose(); //Destroy the JFrame object
        }
    };

    addWindowListener(wndCloser);

    setVisible(true);
}

// Eventhandler fürs Fenster hinzugefügt

private void initComponents() {
    addWindowListener(new java.awt.event.WindowAdapter() {
        public void windowOpened(java.awt.event.WindowEvent evt) {
            formWindowOpened(evt);
        }
    });
}

// Eventhandler beim Öffnen des Fensters, um dieses zu zentrieren

private void formWindowOpened(java.awt.event.WindowEvent evt) {
    // TODO add your handling code here:
    // JFrame zentriert zum Parent positionieren:
    setLocationRelativeTo(getParent());
}

// Panel hinzugefügt, welches zusätzlich zum FileTree auch die Buttons aufnimmt

class SimplePanel extends JPanel 
{
    JButton abbrechen;
    JButton ok;
    
    public SimplePanel() 
    {        
        // OK - Button zum erfolgreichen Übernehmen der Selektion
        ok = new JButton("OK");
	ok.setForeground(Color.blue);
	ok.setFont(new Font("Arial", Font.BOLD, 12));
	ok.setBounds(194, 540, 100, 30);
	add(ok);
        // Event-Handler des OK Buttons
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });
        // Abbrechen Butten, zum Schließen des Fensters
        abbrechen = new JButton("Abbrechen");
	abbrechen.setForeground(Color.blue);
	abbrechen.setFont(new Font("Arial", Font.BOLD, 12));
	abbrechen.setBounds(294, 540, 100, 30);
	add(abbrechen);
        // Event-Handler des Abbrechen Buttons
        abbrechen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abbrechenActionPerformed(evt);
            }
        });
        // Der Ordnerbaum...
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(
            new IconData(ICON_COMPUTER, null, "Computer"));
    
        DefaultMutableTreeNode node;
        File[] roots = File.listRoots();
        for (int k=0; k<roots.length; k++)
        {
            node = new DefaultMutableTreeNode(new IconData(ICON_DISK, null, new FileNode(roots[k])));
            top.add(node);
            node.add(new DefaultMutableTreeNode( new Boolean(true) ));
        }

        m_model = new DefaultTreeModel(top);

        m_tree = new JTree(m_model)
        {
            public String getToolTipText(MouseEvent ev) 
            {
                if(ev == null)
                return null;
                TreePath path = m_tree.getPathForLocation(ev.getX(), ev.getY());
                if (path != null)
                {
                    FileNode fnode = getFileNode(getTreeNode(path));
                    if (fnode==null)
                        return null;
                    File f = fnode.getFile();
                    return (f==null ? null : f.getPath());
                }
                return null;
            }
   
        };

        ToolTipManager.sharedInstance().registerComponent(m_tree);

        m_tree.putClientProperty("JTree.lineStyle", "Angled");

        TreeCellRenderer renderer = new IconCellRenderer();
        m_tree.setCellRenderer(renderer);
        m_tree.addTreeExpansionListener(new  DirExpansionListener());

        m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); 
        m_tree.setShowsRootHandles(true); 
        m_tree.setEditable(false);


        checkTreeManager = AddCh.new CheckTreeManager(m_tree, null);

        JScrollPane s = new JScrollPane();
        s.getViewport().add(m_tree);
        s.setBounds(0, 0, 394, 540);
        //getContentPane().add(s, BorderLayout.CENTER);
        add(s);
    }
}

// Beim Klick auf den OK Button, die ausgewählten Ordner in das SicherungsObjekt wegschreiben

private void okActionPerformed(java.awt.event.ActionEvent evt) 
{                                
    // clear all selected path in order 
    TreePath[] paths=getCheckTreeManager().getSelectionModel().getSelectionPaths();
    
    if(paths != null){
        ArrayList<String> quellpfade = new ArrayList<>();
        for(TreePath tp : paths){
            String pfad = "";
            int x = tp.getPathCount();
            // Zerlegen eines Ordnerstrukturobjekts in die einzelnen Elemente und diese als String zusammenfügen
            for(int i=1; i < x; i++)
            {
                String pfadteilbereich = tp.getPathComponent(i).toString();
                if (pfadteilbereich.endsWith("\\"))
                {
                    pfadteilbereich = pfadteilbereich.replace("\\", "");
                }
                pfad = pfad + pfadteilbereich + "\\";
            }
            // Pfad zum Array hinzufügen
            quellpfade.add(pfad);
            getCheckTreeManager().getSelectionModel().removeSelectionPath(tp);
        }
        // Das Array der einzelnen Pfade in das SicherungsObjekt wegschreiben
        neueSicherungQellen.setQuellpfade(quellpfade);
    }
    setVisible(false); //you can't see me!
    dispose(); //Destroy the JFrame object
}  

// Fenster schließen beim Abbrechen
private void abbrechenActionPerformed(java.awt.event.ActionEvent evt) 
{                                         
    setVisible(false); //you can't see me!
    dispose(); //Destroy the JFrame object
}  

DefaultMutableTreeNode getTreeNode(TreePath path)
{
    return (DefaultMutableTreeNode)(path.getLastPathComponent());
}

FileNode getFileNode(DefaultMutableTreeNode node)
{
    if (node == null)
        return null;
    Object obj = node.getUserObject();
    if (obj instanceof IconData)
        obj = ((IconData)obj).getObject();
    if (obj instanceof FileNode)
        return (FileNode)obj;
    else
        return null;
}

public AddCheckBoxToTree.CheckTreeManager getCheckTreeManager() {
    return checkTreeManager;
}

// Make sure expansion is threaded and updating the tree model
// only occurs within the event dispatching thread.
class DirExpansionListener implements TreeExpansionListener
{
    public void treeExpanded(TreeExpansionEvent event)
    {
        final DefaultMutableTreeNode node = getTreeNode(
                event.getPath());
        final FileNode fnode = getFileNode(node);

        Thread runner = new Thread() 
        {
            public void run() 
            {
                if (fnode != null && fnode.expand(node)) 
                {
                    Runnable runnable = new Runnable() 
                    {
                        public void run() 
                        {
                            m_model.reload(node);
                        }
                    };
                    SwingUtilities.invokeLater(runnable);
                }
            }
        };
        runner.start();
    }

    public void treeCollapsed(TreeExpansionEvent event) {}
}


/* Nicht notwendig, da Main in Hauptfenster
public static void main(String argv[]) 
{
    try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception evt) {}
    new FileTreeViewer();
}*/
}

class IconCellRenderer extends JLabel implements TreeCellRenderer{
protected Color m_textSelectionColor;
protected Color m_textNonSelectionColor;
protected Color m_bkSelectionColor;
protected Color m_bkNonSelectionColor;
protected Color m_borderSelectionColor;

protected boolean m_selected;

public IconCellRenderer()
{
    super();
    m_textSelectionColor = UIManager.getColor(
            "Tree.selectionForeground");
    m_textNonSelectionColor = UIManager.getColor(
            "Tree.textForeground");
    m_bkSelectionColor = UIManager.getColor(
            "Tree.selectionBackground");
    m_bkNonSelectionColor = UIManager.getColor(
            "Tree.textBackground");
    m_borderSelectionColor = UIManager.getColor(
            "Tree.selectionBorderColor");
    setOpaque(false);
}

public Component getTreeCellRendererComponent(JTree tree, 
        Object value, boolean sel, boolean expanded, boolean leaf, 
        int row, boolean hasFocus) 

{
    DefaultMutableTreeNode node = 
            (DefaultMutableTreeNode)value;
    Object obj = node.getUserObject();
    setText(obj.toString());

    if (obj instanceof Boolean)
        setText("Retrieving data...");

    if (obj instanceof IconData)
    {
        IconData idata = (IconData)obj;
        if (expanded)
            setIcon(idata.getExpandedIcon());
        else
            setIcon(idata.getIcon());
    }
    else
        setIcon(null);

    setFont(tree.getFont());
    setForeground(sel ? m_textSelectionColor : 
        m_textNonSelectionColor);
    setBackground(sel ? m_bkSelectionColor : 
        m_bkNonSelectionColor);
    m_selected = sel;
    return this;
}

public void paintComponent(Graphics g) 
{
    Color bColor = getBackground();
    Icon icon = getIcon();

    g.setColor(bColor);
    int offset = 0;
    if(icon != null && getText() != null) 
        offset = (icon.getIconWidth() + getIconTextGap());
    g.fillRect(offset, 0, getWidth() - 1 - offset,
            getHeight() - 1);

    if (m_selected) 
    {
        g.setColor(m_borderSelectionColor);
        g.drawRect(offset, 0, getWidth()-1-offset, getHeight()-1);
    }

    super.paintComponent(g);
}
}

class IconData {
protected Icon   m_icon;
protected Icon   m_expandedIcon;
protected Object m_data;

public IconData(Icon icon, Object data)
{
    m_icon = icon;
    m_expandedIcon = null;
    m_data = data;
}

public IconData(Icon icon, Icon expandedIcon, Object data)
{
    m_icon = icon;
    m_expandedIcon = expandedIcon;
    m_data = data;
}

public Icon getIcon() 
{ 
    return m_icon;
}

public Icon getExpandedIcon() 
{ 
    return m_expandedIcon!=null ? m_expandedIcon : m_icon;
}

public Object getObject() 
{ 
    return m_data;
}

public String toString() 
{ 
    return m_data.toString();
}
}

class FileNode {
protected File m_file;

public FileNode(File file)
{
    m_file = file;
}

public File getFile() 
{ 
    return m_file;
}

public String toString() 
{ 
    return m_file.getName().length() > 0 ? m_file.getName() : 
        m_file.getPath();
}

public boolean expand(DefaultMutableTreeNode parent){
    DefaultMutableTreeNode flag = (DefaultMutableTreeNode)parent.getFirstChild();
    if (flag==null)    // No flag
        return false;
    Object obj = flag.getUserObject();
    if (!(obj instanceof Boolean))
        return false;      // Already expanded

    parent.removeAllChildren();  // Remove Flag

    File[] files = listFiles();
    if (files == null)
        return true;

    Vector<FileNode> v = new Vector<FileNode>();

    for (int k=0; k<files.length; k++){
        File f = files[k];
        if (!(f.isDirectory())) {
            continue;
        }

        FileNode newNode = new FileNode(f);

        boolean isAdded = false;
        for (int i=0; i<v.size(); i++)
        {
            FileNode nd = (FileNode)v.elementAt(i);
            if (newNode.compareTo(nd) < 0)
            {
                v.insertElementAt(newNode, i);
                isAdded = true;
                break;
            }
        }
        if (!isAdded)
            v.addElement(newNode);
    }

    for (int i=0; i<v.size(); i++){
        FileNode nd = (FileNode)v.elementAt(i);
        IconData idata = new IconData(FileTreeViewer.ICON_FOLDER, FileTreeViewer.ICON_EXPANDEDFOLDER, nd);
        DefaultMutableTreeNode node = new 
                DefaultMutableTreeNode(idata);
        parent.add(node);

        if (nd.hasSubDirs())
            node.add(new DefaultMutableTreeNode( 
                    new Boolean(true) ));
    }

    return true;
}

public boolean hasSubDirs(){
    File[] files = listFiles();
    if (files == null)
        return false;
    for (int k=0; k<files.length; k++)
    {
        if (files[k].isDirectory())
            return true;
    }
    return false;
}

public int compareTo(FileNode toCompare){ 
    return  m_file.getName().compareToIgnoreCase(
            toCompare.m_file.getName() ); 
}

protected File[] listFiles(){
    if (!m_file.isDirectory())
        return null;
    try
    {
        return m_file.listFiles();
    }
    catch (Exception ex)
    {
        JOptionPane.showMessageDialog(null, "Error reading directory "+m_file.getAbsolutePath(),"Warning", JOptionPane.WARNING_MESSAGE);
        return null;
    }
}
}