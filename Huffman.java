import java.io.*;

public class Huffman
{
	private HuffmanNode leaf[],tree[];
	private HuffmanNodeFreq ftree[];
	private String dest, src;

	Huffman(String dest,String src)
	{
		init();
		try
		{
			this.dest=dest;
			this.src=src;
		}
		catch(Exception e)
		{
			System.out.println("Constr: "+e);
		}
	}

	private void init()
	{
		leaf=new HuffmanNode[256];
		tree=new HuffmanNode[256];
		ftree=new HuffmanNodeFreq[256];
		int i=-1;
		while(++i<256)
		{
			leaf[i]=new HuffmanNode();
			leaf[i].c=(byte)i;
			tree[i]=leaf[i];
			ftree[i]=new HuffmanNodeFreq();
		}
	}

	private void load()
	{
		String str;
		int i,ch;
		try
		{
			FileInputStream fis=new FileInputStream(src);
			while((ch=fis.read())!=-1)
				ftree[ch].freq=++leaf[ch].freq.freq;
			fis.close();
		}
		catch(Exception e)
		{
			System.out.println("load: "+e);
		}
	}
	private void sort(int sz)
	{
		int i,j;
		HuffmanNode tmp;
		for(i=0;i<sz-1;i++)
		{
			for(j=i+1;j<sz;j++)
				if(tree[i].freq.freq<tree[j].freq.freq)
				{
					tmp=tree[i];
					tree[i]=tree[j];
					tree[j]=tmp;
				}
		}
	}
	private int getidx()
	{
		int i;
		for(i=0;i<256&&tree[i].freq.freq>0;i++);
		return i-1;
	}
	private void makeTree()
	{
		int i;
		HuffmanNode tmp;
		sort(256);
		i=getidx();
		if(i==0&&tree[1].freq.freq==0)
		{
			tmp=new HuffmanNode();
			tmp.freq.freq=tree[0].freq.freq;
			tree[0].up=tmp;
			tmp.left=tree[0];
			tree[0]=tmp;
		}
		while(i>0)
		{
			tmp=new HuffmanNode();
			tmp.freq.freq=tree[i].freq.freq+tree[i-1].freq.freq;
			tmp.left=tree[i-1];
			tmp.right=tree[i];
			tree[i].up=tree[i-1].up=tmp;
			tree[i-1]=tmp;
			sort(i--);
		}
	}
	public boolean encode()
	{
		ObjectOutputStream oos=null;
		FileInputStream fis=null;
		HuffmanNode tmp;
		int i,j,k,buf1[]=new int[50],buf2[]=new int[50],bc=0;
		char ch;
		boolean status=false;
		load();
		makeTree();
		try
		{
			oos=new ObjectOutputStream(new FileOutputStream(dest));
			fis=new FileInputStream(src);
			for(i=0;i<256;i++)
				oos.writeInt(ftree[i].freq);
			while((i=fis.read())!=-1)
			{
				tmp=leaf[i];
				j=0;
				while(tmp.up!=null)
				{
					if(tmp.up.left==tmp)
						buf1[j++]=0;
					else if(tmp.up.right==tmp)
						buf1[j++]=1;
					tmp=tmp.up;
				}
				for(i=0,k=j-1;i<j;i++,k--)
					buf2[bc++]=buf1[k];
				for(i=0,k=bc;k>7;i+=8,k-=8)
				{
					ch=(char)(128*buf2[i]+64*buf2[i+1]+32*buf2[i+2]+16*buf2[i+3]+8*buf2[i+4]+4*buf2[i+5]+2*buf2[i+6]+1*buf2[i+7]);
					for(j=i;j<8;j++)
						buf2[j]=0;
					oos.write(ch);
				}
				if(i>0)
				{
					for(k=0;i<bc;k++,i++)
						buf2[k]=buf2[i];
					bc=k;
				}
			}
			ch=(char)(128*buf2[0]+64*buf2[1]+32*buf2[2]+16*buf2[3]+8*buf2[4]+4*buf2[5]+2*buf2[6]+1*buf2[7]);
			oos.write(ch);
			ch=(char)bc;
			oos.write(ch);
			oos.close();
			fis.close();
			status=true;
		}
		catch(Exception e)
		{
			System.out.println("Encode: "+e);
		}
		finally
		{
			return status;
		}
	}
	public boolean decode()
	{
		ObjectInputStream ois=null,check=null;
		FileOutputStream fos=null;
		int i,j=0,buf[]=new int[8];
		HuffmanNode tmp=null;
		boolean status=false;
		init();
		try
		{
			ois=new ObjectInputStream(new FileInputStream(src));
			check=new ObjectInputStream(new FileInputStream(src));
			fos=new FileOutputStream(dest);
			for(i=0;i<256;i++)
			{
				leaf[i].freq.freq=ois.readInt();
				check.readInt();
			}
			makeTree();
			tmp=tree[0];
			check.read();
			check.read();
			while((i=ois.read())!=-1)
			{
				for(j=7;j>-1;j--,i/=2)
					buf[j]=i%2;
				j=8;
				if(check.read()==-1)
					j=ois.read();
				for(i=0;i<j;i++)
				{
					if(buf[i]==0)
						tmp=tmp.left;
					else
						tmp=tmp.right;
					if(tmp.left==null&&tmp.right==null)
					{
						fos.write(tmp.c);
						tmp=tree[0];
					}
				}
			}
			check.close();
			ois.close();
			fos.close();
			status=true;
		}
		catch(Exception e)
		{
			System.out.println("Decode: "+e);
		}
		finally
		{
			return status;
		}
	}
/*	public static void main(String args[])throws Exception
	{
		String src="proposal.doc",dest="zip.txt";
		Huffman h=new Huffman(dest,src);
		h.encode();
		h=new Huffman("proposal1.doc",dest);
		h.decode();
	}*/
}