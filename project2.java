import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class project2 {
	public static void main(String[] args) {
		String fileName = "P_input.txt";
		Instruction instruction = new Instruction(fileName);
		for (int i = 0 ; i <instruction.getLoginCount(); i++) {
			try {
				InetAddress address = InetAddress.getByName("ptt.cc");
				String ip = address.getHostAddress();
				System.out.println("ip = " + ip);
				ClientSocket socket = new ClientSocket(ip, 23, instruction, i);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
		}
	}
}