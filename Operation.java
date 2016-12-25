import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Operation {
    private DataOutputStream writer;
    private DataInputStream reader;

    private void showToConsole() {
        try {
            Thread.sleep(1000);
            byte[] recvMsg = new byte[1000000];
            if (reader.available() > 0) {
                reader.read(recvMsg);
                System.out.print(new String(recvMsg, "BIG5").substring(0, 10000));
                System.out.println("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void writeThings(String s) {
        try {
            writer.write(s.getBytes("Big5"));
            writer.flush();
            Thread.sleep(300);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public Operation(DataOutputStream writer, DataInputStream reader) {
        this.writer = writer;
        this.reader = reader;
        showToConsole();

    }
    public void login(String[] IdAndPass) {
        String ID = IdAndPass[0], password = IdAndPass[1];
        try {
            writeThings(ID);
            writeThings("\r");
            writeThings(password);
            writeThings("\r");
            Thread.sleep(1000);
            writeThings("\r");
            showToConsole();
            System.out.println("send ID and Pass success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void exit() {
        try {
            byte[] recvMsg = new byte[1000000];
            while(reader.available() == 1) {
                reader.read(recvMsg);
            }
            while (true) {
                writeThings("\033[D");
                if (reader.available() == 0) break;
                reader.read(recvMsg);
            }
            writeThings("\r");
            writeThings("y\r");
            writeThings("\r");
            showToConsole();
            System.out.println("logout success");
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
    }

    public void findBoard(String boardName) {
        try {
            writeThings("s");
            writeThings(boardName);
            writeThings("\r");
            writeThings("\003");
            showToConsole();
            System.out.println("find Board success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeTitle(String titleName) {
        try {
            writeThings("\020\r");
            writeThings(titleName);
            writeThings("\r");
            showToConsole();
            System.out.println("write Title success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeContent(String content) {
        for (int i = 0; i < content.length(); i++)
            if (content.charAt(i) == '\n')
                content = content.substring(0, i) + '\r' + content.substring(i+1, content.length());
        try {
            writeThings(content);
            writeThings("\030s\r\r");
            System.out.println("write content success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}