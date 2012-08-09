import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class UI extends JFrame implements ActionListener, MouseListener, KeyListener{

	private static final long serialVersionUID = 6148570622531132780L;
	private JButton send;
	private JButton close;
	private JTextArea contents;
	private JTextArea sendtext;
	private JScrollPane scrollPaneUP, scrollPaneDown;
	private JPanel south;
	private JPanel sofSouth;
	long currentTimeStamp ;
	String name ;
	private JPanel out;
	
	private JPanel clock ;
	
	private static JLabel clocklabel;
	volatile boolean messageMode = true;
	volatile boolean justCometoMessageMode = true;
	
	ClockThread th;
	
	int curX;
	int curY;
	
	int newX;
	int newY;
	Dimension d = new Dimension(300, 300) ;
	boolean controlled = false;
	
	public UI (String name){
		
		setUpChat (name);
	}
	
	private void setUpChat (String name){
		this.setVisible(false);
		this.setAlwaysOnTop( false );
		if (clock != null){
			clock.setVisible(false);
			this.dispose();
			this.setOpacity(1.0f);
			this.setUndecorated(false);
		}
		send = new JButton ("send");
		close = new JButton ("clock");
		changeButtonStyle (send);
		changeButtonStyle (close);
		south = new JPanel ();
		sofSouth = new JPanel ();
		sendtext = new JTextArea (3, 20);
		sendtext.setWrapStyleWord(true);
		sendtext.setLineWrap(true);
		scrollPaneDown = new JScrollPane (sendtext);
		sendtext.addKeyListener(this);

		sofSouth.setLayout(new BorderLayout());

		south.setLayout(new BorderLayout());
		sofSouth.add(send, BorderLayout.EAST);
		sofSouth.add(close, BorderLayout.WEST);
		south.add(sofSouth, BorderLayout.SOUTH);
		south.add(scrollPaneDown, BorderLayout.CENTER);
		out = new JPanel ();
		out.setLayout(new BorderLayout());
		
		send.addActionListener(this);
		close.addActionListener(this);
		contents = new JTextArea (10, 20);
		contents.setEditable(false);
		contents.setWrapStyleWord(true);
		contents.setLineWrap(true);
		scrollPaneUP = new JScrollPane (contents);
		changeScrollPaneStyle (scrollPaneUP);
		out.add(scrollPaneUP, BorderLayout.CENTER);
		out.add(south, BorderLayout.SOUTH);
		currentTimeStamp = 0;
		this.name =name;
		out.setVisible(true);
		this.add(out);
		this.setSize(d);
		setIcon();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		messageMode = true;
		justCometoMessageMode = true;
		
	} 
	
	private void changeButtonStyle(JButton button){
		 button.setForeground(Color.BLACK);
		 button.setBackground(Color.WHITE);
		 Border line = new LineBorder(Color.BLACK);
		 Border margin = new EmptyBorder(5, 15, 5, 15);
		 Border compound = new CompoundBorder(line, margin);
		 button.setBorder(compound);
	}
	private void setIcon (){
        BufferedImage image = null;
        try {
            image = ImageIO.read(
            		this.getClass().getResource("/icon.png"));
            this.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	private void changeScrollPaneStyle (JScrollPane scroll){
		scroll.setForeground(Color.BLACK);
		scroll.setBackground(Color.WHITE);
		Border line = new LineBorder(Color.BLACK);
		Border margin = new EmptyBorder(5, 5, 5, 5);
		Border compound = new CompoundBorder(line, margin);
		scroll.setBorder(compound);
		UIManager.getLookAndFeelDefaults().put( "ScrollBar.thumb", Color.blue ); 
		
		line = new LineBorder(Color.BLACK);
		margin = new EmptyBorder(1, 2, 2, 2);
		compound = new CompoundBorder(line, margin);
		JScrollBar ver = scroll.getVerticalScrollBar();
		ver.setBorder(compound);
		ScrollBarUI barUI = new BasicScrollBarUI(){
			
			protected JButton createDecreaseButton(int orientation) {
		        JButton button = super.createDecreaseButton(orientation);
		        button.setBackground(Color.GRAY);
		        return button;
		    }
			
		    protected JButton createIncreaseButton(int orientation) {
		        JButton button = super.createIncreaseButton(orientation);
		        button.setBackground(Color.GRAY);
		        return button;
		    }
		    
		    protected void configureScrollBarColors() {
		    	this.thumbColor = Color.DARK_GRAY;
		    }
			
		} ;
		
		ver.setUI(barUI);
	}
	
	private void setUpClock (){
		d = this.getSize();
		this.setVisible(false);
		this.dispose();
		this.setUndecorated(true);
		out.setVisible(false);
		clock =new JPanel();
		clocklabel = new JLabel ();
		
		clock.addMouseListener(this);
		clocklabel.addMouseListener(this);
		clock.add(clocklabel);
		Font font = new Font("Book Antiqua", Font.PLAIN, 30);
		clocklabel.setFont(font);
		clocklabel.setForeground(Color.BLACK);
		clocklabel.setHorizontalAlignment(JLabel.CENTER);
		clocklabel.setVerticalAlignment(JLabel.NORTH);
		clock.setOpaque(false);
		th = new ClockThread();
		
		this.add(clock);
		this.setSize(140, 40);
		this.setOpacity(0.5f);
		this.setAlwaysOnTop( true );
		this.setVisible(true);
		messageMode = false;
		th.start();
		clock.setBackground(Color.LIGHT_GRAY);
		
		
	}
	
	private  class ClockThread extends Thread {
		
		public void run (){
			while (!messageMode){
				Date date = new Date ();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				String dateString = format.format(date);
				clocklabel.setText(dateString);
				//System.out.println(dateString);
			}

		}
	}
	
	private void setMessage (Message []messages){
		int i = 0;
		StringBuffer sb = new StringBuffer ();
		while (i < messages.length){
			sb.append ("\n");
			sb.append("At "+messages[i].getDateString()+"\n");
			sb.append(messages[i].getName()+" said:\n");
			sb.append(messages[i].getMessage()+"\n");
			i++;
		}
		contents.setText(sb.toString());
		
		contents.setSelectionEnd(sb.length()-1);
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send){
			sendMessage();
		} else if (e.getSource() == close){
			//this.setVisible(false);
			setUpClock ();
			
		}
	}
	
	private void sendMessage (){
		String toSend = MessageHelper.packing (name,sendtext.getText());
		boolean ret = Connector.sendMessage(toSend);
		if (!ret){
			Connector.sendMessage(toSend);
		}
		sendtext.setText("");
	}
	
	public void polling (){
		
		int numberOfMiss = 0;
		while (true){
			long temp = Connector.getTimeStamp();
			if (temp != currentTimeStamp || justCometoMessageMode){
				if (messageMode){
					justCometoMessageMode = false;
					String message = Connector.getMessage();
					if (message == null){
						numberOfMiss ++;
						if (numberOfMiss > 1000){
							numberOfMiss = 0;
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						continue;
					}
					Message [] msgs = MessageHelper.resolveAllMessage(message);
					setMessage (msgs);
					currentTimeStamp = temp;
				} else {
					
					clocklabel.setForeground(Color.RED);
					currentTimeStamp = temp;
				}

			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource() == clock || arg0.getSource() == clocklabel){
			this.setUpChat(name);
			try {
				th.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		curX = arg0.getX();
		curY = arg0.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		newX = arg0.getX();
		newY = arg0.getY();
		this.setLocation(  (int) (this.getLocation().getX()+newX-curX), (int) (this.getLocation().getY()+newY-curY));
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_CONTROL){
			controlled = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_CONTROL){
			controlled = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		if (controlled){
			if ((int)arg0.getKeyChar()==19){
				sendMessage();
			} else if ((int)arg0.getKeyChar()==19 + (int)('x' - 's')){
				if (messageMode){
					setUpClock ();
				}

			}		
		}
	}
}
