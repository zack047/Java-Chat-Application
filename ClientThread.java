import java.util.StringTokenizer;
import java.util.Vector;
import java.io.DataInputStream;



class ClientThread implements Runnable
{
    DataInputStream dis;
    MyClient client;
    
    ClientThread(final DataInputStream dis, final MyClient client) {
        this.dis = dis;
        this.client = client;
    }
    
    public void run() {
    Label_0003_Outer:
        while (true) {
            while (true) {
                try {
                    while (true) {
                        final String utf = this.dis.readUTF();
                        if (utf.startsWith("updateuserslist:")) {
                            this.updateUsersList(utf);
                        }
                        else {
                            if (utf.equals("@@logoutme@@:")) {
                                break;
                            }
                            this.client.txtBroadcast.append("\n" + utf);
                        }
                        this.client.txtBroadcast.setCaretPosition(this.client.txtBroadcast.getLineStartOffset(this.client.txtBroadcast.getLineCount() - 1));
                    }
                    break;
                }
                catch (Exception obj) {
                    this.client.txtBroadcast.append("\nClientThread run : " + obj);
                    continue Label_0003_Outer;
                }
                continue;
            }
        }
    }
    
    public void updateUsersList(String str) {
        final Vector<String> listData = new Vector<String>();
        str = str.replace("[", "");
        str = str.replace("]", "");
        str = str.replace("updateuserslist:", "");
        final StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        while (stringTokenizer.hasMoreTokens()) {
            listData.add(stringTokenizer.nextToken());
        }
        this.client.usersList.setListData(listData);
    }
}