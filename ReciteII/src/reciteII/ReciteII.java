package reciteII;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*; 
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

class ReciteII extends JFrame{ 
	JLabel lblWord = new JLabel("問題"); 
	JButton [] btn = new JButton[4];//Show choices.
	JButton btnStart = new JButton("开始游戏");
	List<String> words = new ArrayList<>();
	List<String> meanings = new ArrayList<>();
	List <Integer> randAns = new ArrayList<>();//Store randomly select meanings.
	int ans = 0;//Store answer
	int num = 0;//Store numbers of questions have been answer.
	int record = 0;//Store numbers of questions have been answer correctly. 
	
	public ReciteII() {
		setTitle( "背單字遊戲");
		setSize( 300, 350 );
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel pnlHead = new JPanel();//Show Question
		JPanel pnlBody = new JPanel();//Show Answers
		JPanel pnlFoot = new JPanel();//Show Start button
		pnlHead.add(lblWord);
		pnlBody.setLayout( new BoxLayout(pnlBody, BoxLayout.Y_AXIS));
		pnlFoot.add( btnStart );
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add(pnlHead, BorderLayout.NORTH );
		getContentPane().add(pnlBody, BorderLayout.CENTER );
		getContentPane().add(pnlFoot, BorderLayout.SOUTH );
		Font font = new Font("Times New Rome", 0, 24 );
		try{
			readAll();
		}catch(IOException ex){}
	    btnStart.addActionListener( new ActionListener(){
		  public void actionPerformed( ActionEvent e){
			btnStart_Click();
			}});
	    	  for(int j=0;j<4;j++) {//Initiate buttons 
	    		  btn[j] = new JButton();
	    		  pnlBody.add(btn[j]);
	    		  btn[j].setAlignmentX(CENTER_ALIGNMENT);
	    		  btn[j].setVisible(false);
	    	  }
	      for( int i=0; i<4; i++ ){//Choices been clicked
		   	  btn[i].addActionListener( e->{
					  for( int p=0; p<4; p++ )
					  if( (JButton)e.getSource() == btn[p] )
					    checkAns(p);
			  });
		  } 
	    }
	public void btnStart_Click() {
		randQuestion q = new randQuestion();
		q.run();
	}
	
	
	public class randQuestion extends TimerTask{
		int size = words.size();
		int randQ = (int)(Math.random()*size);//Randomly select a question.
		int randChioce = (int)(Math.random()*4);//Put the answer in a random order.
		int rand = 0;
		public void run(){
		if (num == 10) {
			checkResult() ;
			num = 0;
			setDefult();
		    return;}
		randAns.add(randQ);//Store the question.
		lblWord.setText(words.get(randQ) );
		for(int i = 0;i<4;i++) {
			if(i == randChioce) {// Check if it's the answer's order.
				btn[i].setVisible(true);
				btn[i].setText(meanings.get(randQ));
				btn[i].setOpaque(false);
				btn[i].setBorderPainted(true);
				
				continue;
			}
			while(true) {
				rand =  (int)(Math.random()*size);//Randomly select other meanings.
				if(randAns.contains(rand)) continue;//Check if it has been selected.
				randAns.add(rand);
				break;
			}
			btn[i].setVisible(true);
			btn[i].setText(meanings.get(rand));
			btn[i].setOpaque(false);
			btn[i].setBorderPainted(true);
			
		}
		ans = randChioce;
	}
	}
	
	public void checkAns(int i){
		num++;
		if (i == ans) {//Correct answer,show green button.
			btn[i].setBackground(Color.green);
			btn[i].setOpaque(true);
			btn[i].setBorderPainted(false);
			record++;
		}
		else {//Wrong answer,show red button and show correct answer with green button
			btn[i].setBackground(Color.red);
			btn[i].setOpaque(true);
			btn[i].setBorderPainted(false);
			btn[ans].setBackground(Color.green);
			btn[ans].setOpaque(true);
			btn[ans].setBorderPainted(false);
		}
		Timer timer = new Timer();
	    randQuestion q = new randQuestion();
	    timer.schedule(q,2000);
	}
	public void readAll( ) throws IOException{
		File fileName = new File("src//College_Grade4.txt");
		String charset = "GB2312";
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				new FileInputStream(fileName),charset)); 
		String line; 
		while ((line = reader.readLine()) != null) { 
			line = line.trim();
			if( line.length() == 0 ) continue;
			int idx = line.indexOf("\t");
			words.add( line.substring(0, idx ));
			meanings.add( line.substring(idx+1));
		} 
		reader.close();
	}
	public void checkResult(){//Show the numbers of questions answered
		
		if(record ==10) { 
			ColorIcon c = new ColorIcon(30,Color.green);
			JOptionPane.showMessageDialog(this,"恭喜答對十題","",
					JOptionPane.PLAIN_MESSAGE,c);
         }
		else if (record>7 && record<10) {
			ColorIcon i = new ColorIcon(30,Color.yellow);
		    JOptionPane.showMessageDialog(this,
					"答對"+record+"題，很不錯","",JOptionPane.PLAIN_MESSAGE,i);
		}
		else {
			ColorIcon r = new ColorIcon(30,Color.red);
			JOptionPane.showMessageDialog(this,
				"答對"+record+"題，再接再厲","",JOptionPane.PLAIN_MESSAGE,r);
		}
		record = 0;
		
	}
	public void setDefult() {//Reset after 10 questions
		for(int i=0; i<4;i++) {
			btn[i].setVisible(false);
		}
		lblWord.setText("問題"); 
	}
	private static class ColorIcon implements Icon {//Draw a circle.
        private int size;
        private Color color;
        public ColorIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillOval(x, y, size, size);
        }
        @Override
        public int getIconWidth() {
            return size;
        }
        @Override
        public int getIconHeight() {
            return size;
        }
	}
	public static void main( String[] args){
		SwingUtilities.invokeLater(()->{
			new ReciteII().setVisible(true);
		});
    }
} 
