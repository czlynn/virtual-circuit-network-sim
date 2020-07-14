import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;

/*
 * Parse the input file to get network topology, 
 * sending packets instructions, and virtual circuit building instructions
 * 
 * Parse the forwarding table file to get the forwarding table of the switched network
 */
public class ReadInput {

	static int hosts=0;
	static int switches=0;
	static String SW[][];	//store the topology of the network
	static int forwarding[][];	//store the forwarding table
	static String P[]=new String[100];	//store the packets sending instructions
	static String V[]= new String[100];	//store the virtual circuit building instructions
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int index1=0,index2=0;
			System.out.println("Enter the input file name with extension:");
			Scanner input=new Scanner(System.in);
			File file=new File(input.nextLine());
			input=new Scanner(file);
			while(input.hasNextLine()) {
				String line=input.nextLine();
				String[] tokens=line.split(" ");
				if(tokens[0].equals("hosts")) {
					hosts=Integer.parseInt(tokens[2]);
				}
				if(tokens[0].equals("switches")) {
					switches=Integer.parseInt(tokens[2]);
					SW= new String[switches][4];
					forwarding=new int[switches+1][hosts+1];
				}
				if(tokens[0].contains("[")) {
					
					//sub
					int left=tokens[0].indexOf("W");
					int right=tokens[0].indexOf("[");
					String sub=tokens[0].substring(left+1, right);
					char c=tokens[0].charAt(right+1);
					SW[Integer.parseInt(sub)][Character.getNumericValue(c)]=tokens[2];
					
				}
				if(tokens[0].contains("P")) {
					P[index1++]=tokens[0];
					
				}
				if(tokens[0].contains("V")) {
					V[index2++]=tokens[0];
				}
				
			}
			int sw=0;
			System.out.println("Enter the forwarding table file name with extension:");
			Scanner fwdTable=new Scanner(System.in);
			File fwd=new File(fwdTable.nextLine());
			fwdTable=new Scanner(fwd);
			while(fwdTable.hasNextLine()) {
				String line=fwdTable.nextLine();
				if(line.contains("#")) {
					
				}else if(line.contains("SW")) {
					//sub
					String substr=line.substring(line.indexOf("W")+1);
					sw=Integer.parseInt(substr);
				}
				
				else if(line.contains(",")){
					String[] tokens=line.split(",");
					forwarding[sw][Integer.parseInt(tokens[0])]=Integer.parseInt(tokens[1]);
				}
				
			}
			input.close();
			fwdTable.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
