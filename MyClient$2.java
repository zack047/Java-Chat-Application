import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;



class MyClient$2 extends WindowAdapter {
    @Override
    public void windowClosing(final WindowEvent windowEvent) {
        if (this.this$0.s != null) {
            JOptionPane.showMessageDialog(this.this$0.chatWindow, "u r logged out right now. ", "Exit", 1);
            this.this$0.logoutSession();
        }
        System.exit(0);
    }
}