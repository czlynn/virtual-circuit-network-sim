import java.util.Arrays;
import java.util.Hashtable;
import java.util.spi.CurrencyNameProvider;

/*
 * Deliver packets and establish virtual circuits based on the information from the input files
 */
public class Deliver {

	//return the number of the switch and the interface to which a host is connected
	static int[] getSwitch(int host) {
		int res[] = new int[2];
		for (int i = 0; i < ReadInput.switches; i++)
			for (int j = 0; j < 4; j++) {
				if (ReadInput.SW[i][j].contains("H")) {
					String sub = ReadInput.SW[i][j].substring(ReadInput.SW[i][j].indexOf("H") + 1);
					if (Integer.parseInt(sub) == host) {
						res[0] = i;
						res[1] = j;
						return res;
					}
				}
			}
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ReadInput.main(args);

			// sending packets
			int currSwitch = 0, srcHost = 0, destHost = 0, nextSwitch = 0;
			for (int currPacket = 0; ReadInput.P[currPacket] != null; currPacket++) {
				String[] tokens = ReadInput.P[currPacket].split("-");
				String sub = tokens[0].substring(tokens[0].indexOf("P") + 1);
				srcHost = Integer.parseInt(sub);
				destHost = Integer.parseInt(tokens[1]);
				System.out.println("Packet starting at Host " + srcHost);
				currSwitch = getSwitch(srcHost)[0];

				while (!ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][destHost]].equals("H" + destHost)) {
					String sub1 = ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][destHost]].substring(
							ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][destHost]].indexOf("W") + 1);
					nextSwitch = Integer.parseInt(sub1);
					System.out.println("Switch " + currSwitch + " forwarding packet to Switch " + nextSwitch
							+ " on interface " + ReadInput.forwarding[currSwitch][destHost]);
					currSwitch = nextSwitch;
				}
				System.out.println("Switch " + currSwitch + " : Received packet for Host " + destHost
						+ " forwarding out interface " + ReadInput.forwarding[currSwitch][destHost]);
				System.out.println("Packet delivered to Host " + destHost);
				System.out.println("*********");

			}

			// establish virtual circuit
			currSwitch = 0;
			srcHost = 0;
			destHost = 0;
			nextSwitch = 0;
			int VC[][] = new int[ReadInput.switches][4];
			int VChost[] = new int[ReadInput.hosts];
			for (int i = 0; i < ReadInput.switches; i++)
				for (int j = 0; j < 4; j++) {
					VC[i][j] = -1;
				}
			for (int i = 0; i < ReadInput.hosts; i++) {
				VChost[i] = -1;
			}
			for (int currVc = 0; ReadInput.V[currVc] != null; currVc++) {
				String[] tokens = ReadInput.V[currVc].split("-");
				String sub = tokens[0].substring(tokens[0].indexOf("V") + 1);
				srcHost = Integer.parseInt(sub);
				destHost = Integer.parseInt(tokens[1]);
				currSwitch = getSwitch(srcHost)[0];
				int lastInt = getSwitch(srcHost)[1];
				VC[currSwitch][getSwitch(srcHost)[1]]++;
				VC[currSwitch][ReadInput.forwarding[currSwitch][destHost]]++;
				System.out.println("Circuit requested by Host " + srcHost);

				//send the setup message from source host to destination host 
				while (!ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][destHost]].equals("H" + destHost)) {
					String sub1 = ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][destHost]].substring(
							ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][destHost]].indexOf("W") + 1);
					nextSwitch = Integer.parseInt(sub1);
					System.out.println("Switch " + currSwitch + " creating circuit with incoming interface " + lastInt
							+ " and out going interface " + ReadInput.forwarding[currSwitch][destHost]);
					lastInt = ReadInput.forwarding[nextSwitch][srcHost];
					currSwitch = nextSwitch;
					VC[currSwitch][ReadInput.forwarding[currSwitch][srcHost]]++;
					VC[currSwitch][ReadInput.forwarding[currSwitch][destHost]]++;
				}

				VChost[destHost]++;
				System.out.println("Switch " + currSwitch + " creating circuit with incoming interface " + lastInt
						+ " and out going interface " + ReadInput.forwarding[currSwitch][destHost]);
				System.out.println("Host " + destHost + " requests incoming VCI " + VChost[destHost]);

				
				//send the acknowledge back with the VCI value each chooses for connection to complete the VC table entries
				while (!ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][srcHost]].equals("H" + srcHost)) {
					String sub2 = ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][srcHost]].substring(
							ReadInput.SW[currSwitch][ReadInput.forwarding[currSwitch][srcHost]].indexOf("W") + 1);
					nextSwitch = Integer.parseInt(sub2);
					System.out.println("Switch " + currSwitch + " requests incoming VCI "
							+ VC[currSwitch][ReadInput.forwarding[currSwitch][srcHost]]);
					currSwitch = nextSwitch;
				}
				System.out.println("Switch " + currSwitch + " requests incoming VCI "
						+ VC[getSwitch(srcHost)[0]][getSwitch(srcHost)[1]]);
				System.out.println("Circuit established.");
				System.out.println("*******");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
