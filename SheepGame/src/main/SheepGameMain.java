package main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import javazoom.jl.player.Player;

public class SheepGameMain {
	
	public static int width  = 600;
	public static int height = 1000;
	public static JFrame jf;
	public static Container c;
	
	public static ArrayList<Card> card_list;
	
	public static ReStartButton rsb;
	public static Store store;
	public static Background bg;
	
	public static int level = 1;
	
	//第一关
	public static void level_1() {
		for(int i=0;i<21;i++) {
			Random random = new Random();
			int card_type = random.nextInt(13) + 1;
			Card card = new Card(1);
			card.depth = i;
			card.setLocation(100+random.nextInt(450), 100+random.nextInt(600));
			c.add(card);
			card_list.add(card);
			
		}
	}
	
	//随机放置卡片
	public static void random_level(int card_num,int card_type_num) {
		Random random = new Random();
		ArrayList<Integer> type_list = new ArrayList<Integer>();
		ArrayList<Integer> type_remain = new ArrayList<Integer>();
		for(int i=1;i<=card_type_num;i++) {
			type_remain.add(i);
		}
		for(int i=0;i<card_num/3;i++) {
			if(type_remain.size() == 0) {
				for(int j=1;j<=card_type_num;j++) {
					type_remain.add(j);
				}
			}
			int type_index = random.nextInt(type_remain.size());
			int type = type_remain.get(type_index);
			type_remain.remove(type_index);
			for(int j=0;j<3;j++) {
				type_list.add(type);
			}
		}
		int left_offset = 0;
		int right_offset = 0;
		for(int i=0;i<card_num;i++) {
			
			//左下角堆叠
			if(i>=10 && i<20) {
				int _rand = random.nextInt(type_list.size());
				int type = type_list.get(_rand);
				type_list.remove(_rand);
				
				Card card = new Card(type);
				card.depth = i;
				card.setLocation(220+left_offset, 700);
				c.add(card);
				card_list.add(card);
				left_offset-=10;
				continue;
			}
			//右下角堆叠
			if(i>=20 && i<30) {
				int _rand = random.nextInt(type_list.size());
				int type = type_list.get(_rand);
				type_list.remove(_rand);
				
				Card card = new Card(type);
				card.depth = i;
				card.setLocation(340+right_offset, 700);
				c.add(card);
				card_list.add(card);
				right_offset+=10;
				continue;
			}
			//左下角堆叠
			if(i>=50 && i<60) {
				int _rand = random.nextInt(type_list.size());
				int type = type_list.get(_rand);
				type_list.remove(_rand);
				
				Card card = new Card(type);
				card.depth = i;
				card.setLocation(220+left_offset, 700);
				c.add(card);
				card_list.add(card);
				left_offset-=10;
				continue;
			}
			//右下角堆叠
			if(i>=60 && i<70) {
				int _rand = random.nextInt(type_list.size());
				int type = type_list.get(_rand);
				type_list.remove(_rand);
				
				Card card = new Card(type);
				card.depth = i;
				card.setLocation(340+right_offset, 700);
				c.add(card);
				card_list.add(card);
				right_offset+=10;
				continue;
			}
			
			
			int _rand = random.nextInt(type_list.size());
			int type = type_list.get(_rand);
			type_list.remove(_rand);
			
			Card card = new Card(type);
			card.depth = i;
			card.setLocation(100+random.nextInt(14)*32, 100+random.nextInt(11)*47);
			c.add(card);
			card_list.add(card);
		}
		
		
	}
	
	//开始游戏
	public static void start_game() {
		String title = "羊了个羊 第"+level+"关";
		jf.setTitle(title);
		switch(level) {
		case 1:
			random_level(15,3);
		break;
		case 2:
			random_level(30,6);
		break;
		case 3:
			random_level(45,8);
		break;
		case 4:
			random_level(60,10);
		break;
		case 5:
			random_level(75,12);
		break;
		case 6:
			random_level(90,13);
		break;
		default:
			JOptionPane.showMessageDialog(null, "您已通关！");
			return ;
		}
		
		
		update_card();
		
		store = new Store();
		c.add(store);
		
		bg = new Background();
		c.add(bg);
		
		jf.repaint();
	}
	//游戏结束
	public static void end_game() {
		for(int i=0;i<card_list.size();i++) {
			c.remove(card_list.get(i));
		}
		card_list = new ArrayList<Card>();
		if(store!=null) {
			c.remove(store);
		}
		c.remove(bg);
	}
	//更新card的可见性
	public static void update_card() {
		for(int i=0;i<card_list.size();i++) {
			Card card = card_list.get(i);
			if(card.store_status == 1) {
				card.setEnabled(true);
				continue;
			}
			if(card.is_removed == 1) {
				continue;
			}
			int is_hide = 0;
			//寻找比当前card浅的
			for(int j=0;j<card_list.size();j++) {
				Card card2 = card_list.get(j);
				if(card2.store_status == 1) {
					card.setEnabled(true);
					continue;
				}
				if(card2.is_removed == 1) {
					continue;
				}
				if(card2.depth < card.depth) {
					//检测是否重叠 检测4个点
					int card2_x = card2.getX();
					int card2_y = card2.getY();
					int card2_x2 = card2.getX() + card.card_width;
					int card2_y2 = card2.getY() + card.card_height;
					int card_x = card.getX();
					int card_y = card.getY();
					int card_x2 = card.getX() + card.card_width;
					int card_y2 = card.getY() + card.card_height;
					
					//左上角
					if(card2_x >= card_x && card2_x <= card_x2 && card2_y >= card_y && card2_y <= card_y2) {
						int s_width = card_x2 - card2_x;
						int s_height = card_y2 - card2_y;
						int s = s_width * s_height;
						if(s>0) {
							card.setEnabled(false);
							is_hide = 1;
							break;
						}
					}
					//右上角
					if(card2_x2 >= card_x && card2_x2 <= card_x2 && card2_y >= card_y && card2_y <= card_y2) {
						int s_width = card2_x2 - card_x;
						int s_height = card_y2 - card2_y;
						int s = s_width * s_height;
						if(s>0) {
							card.setEnabled(false);
							is_hide = 1;
							break;
						}
					}
					//左下角
					if(card2_x >= card_x && card2_x <= card_x2 && card2_y2 >= card_y && card2_y2 <= card_y2) {
						int s_width = card_x2 - card2_x;
						int s_height = card2_y2 - card_y;
						int s = s_width * s_height;
						if(s>0) {
							card.setEnabled(false);
							is_hide = 1;
							break;
						}
					}
					//右下角
					if(card2_x2 >= card_x && card2_x2 <= card_x2 && card2_y2 >= card_y && card2_y2 <= card_y2) {
						int s_width = card2_x2 - card_x;
						int s_height = card2_y2 - card_y;
						int s = s_width * s_height;
						if(s>0) {
							card.setEnabled(false);
							is_hide = 1;
							break;
						}
					}
				}
			}
			if(is_hide == 0) {
				card.setEnabled(true);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		card_list = new ArrayList<Card>();
		
		jf = new JFrame("羊了个羊");
		jf.setVisible(true);
		jf.setSize(width,height);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(null);
		
		c = jf.getContentPane();

		rsb = new ReStartButton();
		c.add(rsb);
		
		bg = new Background();
		c.add(bg);
		
		
		
		
		
		jf.repaint();
		
		new Thread() {
			public void run() {
				while(true) {
					try {
						File file = new File("music.mp3");
						Player player = new Player(new FileInputStream(file));
						player.play();//播放音乐
					} catch (Exception e) {
						// TODO Auto-generated catch block
						
					}
					try {
						sleep(1000);
					}catch(Exception e) {
						
					}
				}
				
			}
		}.start();
		
		
		
	}

}

class Store extends JLabel{
	public int width  = 600;
	public int height = 200;
	
	public int pos_x[] = {30,108,188,268,348,428,508};
	public int pos_y[] = {850,850,850,850,850,850,850};

	public ArrayList<Card> card_list;
	
	public Store() {
		card_list = new ArrayList<Card>();
		setBounds(0,SheepGameMain.height-height,width,height);
		ImageIcon image = new ImageIcon(SheepGameMain.class.getResource("store.jpg"));
		image.setImage(image.getImage().getScaledInstance(width, height,Image.SCALE_DEFAULT));
		setIcon(image);
	}
	//返回0表示没有消除的，返回1表示有3个相同的，返回2表示已满
	public int check_store() {
		int ret = 0;
		int card_type_sum[] =new int[16];
		for(int i=0;i<card_list.size();i++) {
			int type = card_list.get(i).card_type;
			card_type_sum[type] += 1;
		}
		int target_type = 0;
		for(int i=1;i<=13;i++) {
			if(card_type_sum[i] == 3) {
				target_type = i;
				ret = 1;
				break;
			}
		}
		//删除3个card
		for(int i=0;i<3;i++) {
			for(int j=0;j<card_list.size();j++) {
				Card card = card_list.get(j);
				if(card.card_type == target_type) {
					card_list.remove(j);
					SheepGameMain.jf.getContentPane().remove(card);
					card.is_removed = 1;
					break;
				}
			}
		}
		//将剩下的重新移动位置
		for(int i=0;i<card_list.size();i++) {
			Card card = card_list.get(i);
			card.setLocation(pos_x[i],pos_y[i]);
		}
		
		SheepGameMain.jf.repaint();
		if(card_list.size() == 7) {
			return 2;
		}
		return ret;
	}
}

class Background extends JLabel{
	public Background() {
		setBounds(0,0,SheepGameMain.width,SheepGameMain.height);
		ImageIcon image = new ImageIcon(SheepGameMain.class.getResource("bkg.jpg"));
		image.setImage(image.getImage().getScaledInstance(SheepGameMain.width, SheepGameMain.height,Image.SCALE_DEFAULT));
		setIcon(image);
		
	}
}

class ReStartButton extends JButton{
	public static int width = 60;
	public static int height = 60;
	public ReStartButton() {
		ImageIcon image = new ImageIcon(SheepGameMain.class.getResource("re_start.png"));
		image.setImage(image.getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT));
		setIcon(image);
		setSize(width,height);
		setLocation(10,10);
		
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SheepGameMain.end_game();
				SheepGameMain.start_game();
			}
			
		});
	}
}

class Card extends JButton{
	public static int card_width = 64;
	public static int card_height = 94;
	public int store_status = 0;
	public int card_type = 0;
	public int depth = 0;
	public int is_removed = 0;
	
	class MoveThread extends Thread{
		double x,y,x2,y2;
		Card card;
		public void run() {
			int total = 100;
			for(int index=0;index<=total;index++) {
				setLocation((int)(x+(x2-x)/total*index),(int)(y+(y2-y)/total*index));
				SheepGameMain.jf.repaint();
				try {
					sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public Card(int card_type) {
		ImageIcon image = null;
		
		this.card_type = card_type;
		switch(card_type) {
		case 1:
			image = new ImageIcon(SheepGameMain.class.getResource("card_1.png"));
		break;
		case 2:
			image = new ImageIcon(SheepGameMain.class.getResource("card_2.png"));
		break;
		case 3:
			image = new ImageIcon(SheepGameMain.class.getResource("card_3.png"));
		break;
		case 4:
			image = new ImageIcon(SheepGameMain.class.getResource("card_4.png"));
		break;
		case 5:
			image = new ImageIcon(SheepGameMain.class.getResource("card_5.png"));
		break;
		case 6:
			image = new ImageIcon(SheepGameMain.class.getResource("card_6.png"));
		break;
		case 7:
			image = new ImageIcon(SheepGameMain.class.getResource("card_7.png"));
		break;
		case 8:
			image = new ImageIcon(SheepGameMain.class.getResource("card_8.png"));
		break;
		case 9:
			image = new ImageIcon(SheepGameMain.class.getResource("card_9.png"));
		break;
		case 10:
			image = new ImageIcon(SheepGameMain.class.getResource("card_10.png"));
		break;
		case 11:
			image = new ImageIcon(SheepGameMain.class.getResource("card_11.png"));
		break;
		case 12:
			image = new ImageIcon(SheepGameMain.class.getResource("card_12.png"));
		break;
		case 13:
			image = new ImageIcon(SheepGameMain.class.getResource("card_13.png"));
		break;
		}
		image.setImage(image.getImage().getScaledInstance(card_width, card_height,Image.SCALE_DEFAULT));
		setIcon(image);
		setSize(card_width,card_height);
		
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Card card = (Card)e.getSource();
				if(SheepGameMain.store.card_list.size() <7 && card.store_status == 0) {
					card.store_status = 1;
					double x = getX();
					double y = getY();
					double x2 = SheepGameMain.store.pos_x[SheepGameMain.store.card_list.size()];
					double y2 = SheepGameMain.store.pos_y[SheepGameMain.store.card_list.size()];
					MoveThread mt = new MoveThread();
					mt.x=x;mt.y=y;
					mt.x2=x2;mt.y2=y2;
					mt.card=card;
					SheepGameMain.store.card_list.add(card);
					mt.start();
					int check_ret = SheepGameMain.store.check_store();
					if(check_ret == 2 && SheepGameMain.store.card_list.size() == 7) { //游戏失败
						JOptionPane.showMessageDialog(null, "游戏结束");
						SheepGameMain.end_game();
						SheepGameMain.start_game();
					}
					//检查剩余card数量是否为0
					int remain = 0;
					for(int i=0;i<SheepGameMain.card_list.size();i++) {
						if(SheepGameMain.card_list.get(i).is_removed == 0) {
							remain += 1;
						}
					}
					if(remain == 0) { //胜利
						JOptionPane.showMessageDialog(null, "游戏胜利");
						SheepGameMain.end_game();
						SheepGameMain.level += 1;
						SheepGameMain.start_game();
					}
					SheepGameMain.update_card();
				}
				
			}
			
		});
	}
}
