package com.dmr;

public class DMRData {
	private String display[]=new String[3];
	
	// The header decode method
	public String[] decodeHeader (boolean bits[])	{
		int dpf;
		// Data Packet Format
		if (bits[4]==true) dpf=8;
		else dpf=0;
		if (bits[5]==true) dpf=dpf+4;
		if (bits[6]==true) dpf=dpf+2;
		if (bits[7]==true) dpf++;
		// Types
		if (dpf==0) udt(bits);
		else if (dpf==1) responsePacket(bits);
		else if (dpf==2) unconfirmedData(bits);
		else if (dpf==3) confirmedData(bits);
		else if (dpf==13) definedShortData(bits);
		else if (dpf==14) rawShortData(bits);
		else if (dpf==15) propData(bits);
		else unknownData(bits,dpf);
		return display;
	}
	
	// Unified Data Transport
	void udt (boolean bits[])	{
		display[0]="<b>Unified Data Transport</b>";
	}
	
	// Response Packet
	void responsePacket (boolean bits[])	{
		int blocks,dclass,status,type;
		display[0]="<b>Response Packet</b>";
		// Destination LLID
		int dllid=retAddress(bits,16);
		// Source LLID
		int sllid=retAddress(bits,40);
		display[1]="<b>Destination Logical Link ID : "+Integer.toString(dllid);
		display[1]=display[1]+" Source Logical Link ID : "+Integer.toString(sllid)+"</b>";
		// Bit 64 is 0
		// Blocks to follow
		if (bits[65]==true) blocks=64;
		else blocks=0;
		if (bits[66]==true) blocks=blocks+32;
		if (bits[67]==true) blocks=blocks+16;
		if (bits[68]==true) blocks=blocks+8;
		if (bits[69]==true) blocks=blocks+4;
		if (bits[70]==true) blocks=blocks+2;
		if (bits[71]==true) blocks++;
		// Class
		if (bits[72]==true) dclass=2;
		else dclass=0;
		if (bits[73]==true) dclass++;
		// Type
		if (bits[74]==true) type=4;
		else type=0;
		if (bits[75]==true) type=type+2;
		if (bits[76]==true) type++;
		// Status
		if (bits[77]==true) status=4;
		else status=0;
		if (bits[78]==true) status=status+2;
		if (bits[79]==true) status++;
		// Display this
		display[2]="<b>"+Integer.toString(blocks)+" blocks follow : ";
		if ((dclass==0)&&(type==1)) display[2]=display[2]+"ACK";
		else if ((dclass==1)&&(type==0)) display[2]=display[2]+"NACK (Illegal Format)";
		else if ((dclass==1)&&(type==1)) display[2]=display[2]+"NACK (CRC Failed)";
		else if ((dclass==1)&&(type==2)) display[2]=display[2]+"NACK (Memory Full)";
		else if ((dclass==1)&&(type==4)) display[2]=display[2]+"NACK (Undeliverable)";
		else if ((dclass==2)&&(type==0)) display[2]=display[2]+"SACK";
		else display[2]=display[2]+" Unknown C="+Integer.toString(dclass)+" T="+Integer.toString(type)+" S="+Integer.toString(status);
		display[2]=display[2]+"</b>";
	}
	
	// Unconfirmed Data
	void unconfirmedData (boolean bits[])	{
		display[0]="<b>Unconfirmed Data</b>";
	}
	
	// Confirmed Data
	void confirmedData (boolean bits[])	{
		display[0]="<b>Confirmed Data</b>";
	}
	
	// Defined Short Data
	void definedShortData (boolean bits[])	{
		display[0]="<b>Defined Short Data</b>";
	}
	
	// Raw Short Data
	void rawShortData (boolean bits[])	{
		display[0]="<b>Raw or Status Short Data</b>";
	}
	
	// Proprietary Data Packet
	void propData (boolean bits[])	{
		display[0]="<b>Proprietary Data Packet</b>";
	}
	
	// Unknown Data
	void unknownData (boolean bits[],int dpf)	{
		display[0]="<b>Unknown Data : DPF="+Integer.toString(dpf)+"</b>";
	}
	
	// Return a 24 bit address 
	private int retAddress (boolean bits[],int offset)	{
		int addr=0,a,b,c;
		for (a=0;a<24;a++)	{
			b=(24-a)-1;
			c=(int)Math.pow(2.0,b);
			if (bits[a+offset]==true) addr=addr+c;
		}
		return addr;
	}
	
	
	

}