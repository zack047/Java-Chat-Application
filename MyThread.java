import java.util.Iterator;
import java.io.DataOutputStream;
import java.util.Date;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.net.Socket;


class MyThread implements Runnable
{
    Socket s;
    ArrayList al;
    ArrayList users;
    String username;
    
    MyThread(final Socket socket, final ArrayList al, final ArrayList users) {
        this.s = socket;
        this.al = al;
        this.users = users;
        try {
            this.username = new DataInputStream(socket.getInputStream()).readUTF();
            al.add(socket);
            users.add(this.username);
            this.tellEveryOne("****** " + this.username + " Logged in at " + new Date() + " ******");
            this.sendNewUserList();
        }
        catch (Exception obj) {
            System.err.println("MyThread constructor  " + obj);
        }
    }
    
    public void run() {
        try {
            final DataInputStream dataInputStream = new DataInputStream(this.s.getInputStream());
            while (true) {
                final String utf = dataInputStream.readUTF();
                if (utf.toLowerCase().equals("@@logoutme@@:")) {
                    break;
                }
                this.tellEveryOne(this.username + " said: " + " : " + utf);
            }
            final DataOutputStream dataOutputStream = new DataOutputStream(this.s.getOutputStream());
            dataOutputStream.writeUTF("@@logoutme@@:");
            dataOutputStream.flush();
            this.users.remove(this.username);
            this.tellEveryOne("****** " + this.username + " Logged out at " + new Date() + " ******");
            this.sendNewUserList();
            this.al.remove(this.s);
            this.s.close();
        }
        catch (Exception obj) {
            System.out.println("MyThread Run" + obj);
        }
    }
    
    public void sendNewUserList() {
        this.tellEveryOne("updateuserslist:" + this.users.toString());
    }
    
    public void tellEveryOne(final String str) {
        final Iterator<Socket> iterator = this.al.iterator();
        while (iterator.hasNext()) {
            try {
                final DataOutputStream dataOutputStream = new DataOutputStream(iterator.next().getOutputStream());
                dataOutputStream.writeUTF(str);
                dataOutputStream.flush();
            }
            catch (Exception obj) {
                System.err.println("TellEveryOne " + obj);
            }
        }
    }
}