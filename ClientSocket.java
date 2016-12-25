import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSocket {
	private Socket client;
	private DataOutputStream writer;
	private DataInputStream reader;
	private Instruction instructionToOp;

	public ClientSocket(String ip, int port, Instruction instructions, int num) {
		try {
			client = new Socket(ip, port);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			this.writer = new DataOutputStream(client.getOutputStream());
			this.reader = new DataInputStream(client.getInputStream());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		this.instructionToOp = instructions;
		Operation op = new Operation(writer, reader);
		op.login(instructionToOp.getLogin(num));
		try {
			int articleCount = instructionToOp.getArticleNum(num);
			Thread.sleep(1000);
			for (int i = 0; i < articleCount; i++) {
				op.findBoard(instructionToOp.getBoard(num,i));
				Thread.sleep(1000);
				op.writeTitle(instructionToOp.getTitle(num,i));
				Thread.sleep(1000);
				op.writeContent(instructionToOp.getContent(num,i));
				Thread.sleep(1000);
			}
			op.exit();

			this.writer.close();
			this.reader.close();
			client.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}