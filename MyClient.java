import java.net.InetAddress;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.awt.event.ActionListener;



class MyClient implements ActionListener
{
    Socket s;
    DataInputStream dis;
    DataOutputStream dos;
    JButton sendButton;
    JButton logoutButton;
    JButton loginButton;
    JButton exitButton;
    JFrame chatWindow;
    JTextArea txtBroadcast;
    JTextArea txtMessage;
    JList usersList;
    
    public void displayGUI() {
        this.chatWindow = new JFrame();
        (this.txtBroadcast = new JTextArea(5, 30)).setEditable(false);
        this.txtMessage = new JTextArea(2, 20);
        this.usersList = new JList();
        this.sendButton = new JButton("Send");
        this.logoutButton = new JButton("Leave Chat");
        this.loginButton = new JButton("Enter Chat");
        this.exitButton = new JButton("Exit");
        final JPanel comp = new JPanel();
        comp.setLayout(new BorderLayout());
        comp.add(new JLabel("Broad Cast messages from all online users", 0), "North");
        comp.add(new JScrollPane(this.txtBroadcast), "Center");
        final JPanel comp2 = new JPanel();
        comp2.setLayout(new FlowLayout());
        comp2.add(new JScrollPane(this.txtMessage));
        comp2.add(this.sendButton);
        final JPanel comp3 = new JPanel();
        comp3.setLayout(new FlowLayout());
        comp3.add(this.loginButton);
        comp3.add(this.logoutButton);
        comp3.add(this.exitButton);
        final JPanel comp4 = new JPanel();
        comp4.setLayout(new GridLayout(2, 1));
        comp4.add(comp2);
        comp4.add(comp3);
        final JPanel comp5 = new JPanel();
        comp5.setLayout(new BorderLayout());
        comp5.add(new JLabel("Online Users", 0), "East");
        comp5.add(new JScrollPane(this.usersList), "South");
        this.chatWindow.add(comp5, "East");
        this.chatWindow.add(comp, "Center");
        this.chatWindow.add(comp4, "South");
        this.chatWindow.pack();
        this.chatWindow.setTitle("Login for Chat");
        this.chatWindow.setDefaultCloseOperation(0);
        this.chatWindow.setVisible(true);
        this.sendButton.addActionListener(this);
        this.logoutButton.addActionListener(this);
        this.loginButton.addActionListener(this);
        this.exitButton.addActionListener(this);
        this.logoutButton.setEnabled(false);
        this.loginButton.setEnabled(true);
        this.txtMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent focusEvent) {
                MyClient.this.txtMessage.selectAll();
            }
        });
        this.chatWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent windowEvent) {
                if (MyClient.this.s != null) {
                    JOptionPane.showMessageDialog(MyClient.this.chatWindow, "u r logged out right now. ", "Exit", 1);
                    MyClient.this.logoutSession();
                }
                System.exit(0);
            }
        });
    }
    
    public void actionPerformed(final ActionEvent actionEvent) {
        final JButton button = (JButton)actionEvent.getSource();
        if (button == this.sendButton) {
            if (this.s == null) {
                JOptionPane.showMessageDialog(this.chatWindow, "u r not logged in. plz login first");
                return;
            }
            try {
                this.dos.writeUTF(this.txtMessage.getText());
                this.txtMessage.setText("");
            }
            catch (Exception obj) {
                this.txtBroadcast.append("\nsend button click :" + obj);
            }
        }
        if (button == this.loginButton) {
            final String showInputDialog = JOptionPane.showInputDialog(this.chatWindow, "Enter Your lovely nick name: ");
            if (showInputDialog != null) {
                this.clientChat(showInputDialog);
            }
        }
        if (button == this.logoutButton && this.s != null) {
            this.logoutSession();
        }
        if (button == this.exitButton) {
            if (this.s != null) {
                JOptionPane.showMessageDialog(this.chatWindow, "u r logged out right now. ", "Exit", 1);
                this.logoutSession();
            }
            System.exit(0);
        }
    }
    
    public void logoutSession() {
        if (this.s == null) {
            return;
        }
        try {
            this.dos.writeUTF("@@logoutme@@:");
            Thread.sleep(500L);
            this.s = null;
        }
        catch (Exception obj) {
            this.txtBroadcast.append("\n inside logoutSession Method" + obj);
        }
        this.logoutButton.setEnabled(false);
        this.loginButton.setEnabled(true);
        this.chatWindow.setTitle("Login for Chat");
    }
    
    public void clientChat(final String s) {
        try {
            this.s = new Socket(InetAddress.getLocalHost(), 10);
            this.dis = new DataInputStream(this.s.getInputStream());
            this.dos = new DataOutputStream(this.s.getOutputStream());
            new Thread(new ClientThread(this.dis, this)).start();
            this.dos.writeUTF(s);
            this.chatWindow.setTitle(s + " Chat Window");
        }
        catch (Exception obj) {
            this.txtBroadcast.append("\nClient Constructor " + obj);
        }
        this.logoutButton.setEnabled(true);
        this.loginButton.setEnabled(false);
    }
    
    public MyClient() {
        this.displayGUI();
    }
    
    public static void main(final String[] array) {
        new MyClient();
    }
}