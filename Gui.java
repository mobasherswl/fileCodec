import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Gui implements ActionListener
{
	private JFrame frame;
	private JPanel panel[];
	private JButton bSrcBrowse,bDestBrowse,bPack,bUnpack;
	private JTextField txtSrc,txtDest;
	private JLabel lblSrc,lblDest;

	Gui()
	{
		frame=new JFrame("File Compressor/Decompressor");
		frame.setLayout(new FlowLayout());

		lblSrc=new JLabel("Source path");
		lblDest=new JLabel("Destination path");

		txtSrc=new JTextField(20);
		txtDest=new JTextField(20);

		bSrcBrowse=new JButton("Browse");
		bDestBrowse=new JButton("Browse");
		bPack=new JButton("Compress");
		bUnpack=new JButton("Decompress");

		bSrcBrowse.addActionListener(this);
		bDestBrowse.addActionListener(this);
		bPack.addActionListener(this);
		bUnpack.addActionListener(this);

		frame.add(lblSrc);
		frame.add(txtSrc);
		frame.add(bSrcBrowse);
		frame.add(lblDest);
		frame.add(txtDest);
		frame.add(bDestBrowse);
		frame.add(bPack);
		frame.add(bUnpack);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(450,150);
		frame.setVisible(true);
	}

	private String showFileDialog()
	{
		JFileChooser fc=new JFileChooser();
		if(fc.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION)
		{
			return fc.getSelectedFile().getPath();
		}
		return null;
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==bSrcBrowse)
			txtSrc.setText(showFileDialog());
		else if(ae.getSource()==bDestBrowse)
			txtDest.setText(showFileDialog());
		else if(ae.getSource()==bPack)
		{
			if(new Huffman(txtDest.getText(),txtSrc.getText()).encode())
				JOptionPane.showMessageDialog(frame,"Compression successful!");
		}
		else if(ae.getSource()==bUnpack)
		{
			if(new Huffman(txtDest.getText(),txtSrc.getText()).decode())
				JOptionPane.showMessageDialog(frame,"Decompression successful!");
		}
	}

	public static void main(String args[])
	{
		Gui g=new Gui();
	}
}