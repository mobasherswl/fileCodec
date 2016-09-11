public class HuffmanNode
{
	byte c;
	HuffmanNodeFreq freq;
	HuffmanNode up,left,right;
	HuffmanNode()
	{
		freq=new HuffmanNodeFreq();
		c=(byte)0;
		up=left=right=null;
	}
}

class HuffmanNodeFreq
{
	int freq;
	HuffmanNodeFreq()
	{
		freq=0;
	}
}