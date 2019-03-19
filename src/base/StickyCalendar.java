package base;


import base.Date;
import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StickyCalendar extends JFrame {
	
	class ImageButton extends JButton{
		
		ImageButton(ImageIcon icon){

			super(icon);
			this.setBorderPainted(false);
			this.setFocusPainted(false);
			this.setContentAreaFilled(false);
			this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
			
		}
		
		void Set(ImageIcon icon) {
			this.setIcon(icon);
			this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		}
	}
	
	class PlSpace extends JPanel{
		
		PlSpace(){
			this.setPreferredSize(new Dimension(25,10));
			this.setBackground(Color.white);
		}
	}
	
	class PlCanMoveFrame extends JPanel {	//â �̵��� �� �ִ� �гε� 

		class Motion{
			int x, y;
			
			void SavePoint(int x, int y) {
				this.x = x;
				this.y = y;
			}
			void MovePoint(int x, int y) {
				
				getthis().setLocation(x - this.x , y - this.y);
				DB.getUserSetting().Set(DB.getUserSetting().LOCATION, x - this.x, y - this.y);
			}
		}
		
		Motion m = new Motion();
		
		JPanel leftside = new JPanel();
		JPanel rightside = new JPanel();
		JPanel center = new JPanel();
		
		PlCanMoveFrame(){

			this.setBackcolor(new Color(255, 218, 185));
			this.setBackground(new Color(255, 218, 185));
			this.setPreferredSize(new Dimension(10, 50));
			this.setLayout(new BorderLayout());
			
			Dimension sidesize = new Dimension(120,10);
			leftside.setPreferredSize(sidesize);
			rightside.setPreferredSize(sidesize);
			
			MakeMotion();
			Makeleft();
			Makeright();
			Makecenter();
			
			this.add(leftside, BorderLayout.WEST);
			this.add(rightside, BorderLayout.EAST);
			this.add(center, BorderLayout.CENTER);
			
			
		}
		private void MakeMotion() {
			this.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					m.SavePoint(e.getX(), e.getY());
				}
				
			});
			this.addMouseMotionListener(new MouseMotionAdapter() {

				@Override
				public void mouseDragged(MouseEvent arg0) {
					m.MovePoint(arg0.getXOnScreen(), arg0.getYOnScreen());
				}
			});
		}
		
		private void Makeleft() {
			leftside.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 8));
		}
		private void Makeright() {
			rightside.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 8));
			
		}
		private void Makecenter() {
			center.setLayout(new FlowLayout(FlowLayout.CENTER, 18, 8));
		}
	
		void addleft(Component c) {
			leftside.add(c);
		}
		void addright(Component c) {
			rightside.add(c);
		}
		
		void addcenter(Component c) {
			center.add(c);
		}
	
		void setBackcolor(Color c) {
			leftside.setBackground(c);
			rightside.setBackground(c);
			center.setBackground(c);
		}
	}
	
	class PlCalendarScreen extends JPanel {	//Ķ���� ȭ�� Ŭ����
		
		class PlTopBar extends PlCanMoveFrame{
			/*
			 * 	PlTopBar
			 * 		-left
			 * 			-year
			 * 		-right
			 * 			-close
			 * 		-center
			 * 			-prev
			 * 			-month
			 * 			-next
			 */
			JLabel year = new JLabel();
			JButton prev = new ImageButton(DB.getImage().Lefticon);
			JLabel month = new JLabel();
			JButton next = new ImageButton(DB.getImage().Righticon);
			JButton close = new ImageButton(DB.getImage().Xicon);
			
			PlTopBar(){
				makeButton();	//prev, next, close
				
				Font font = new Font("Gulim", Font.BOLD, 15);
				year.setPreferredSize(new Dimension(60,35));
				year.setHorizontalAlignment(SwingConstants.CENTER);
				year.setVerticalAlignment(SwingConstants.CENTER);
				year.setFont(font);
				month.setFont(font);
				
				
				this.addleft(year);
				this.addcenter(prev);
				this.addcenter(month);
				this.addcenter(next);
				this.addright(close);
			}
			
			void makeButton() {
				
				prev.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						MainScreen.PrevMonth();
					}
				});
				
				next.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						MainScreen.NextMonth();
					}
				});
				
				close.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						getthis().ProgramClose();
					}
				});
				
			}
			
			void setTime(Date c) {
				
				year.setText(String.valueOf(c.get(c.YEAR)) + "��");
				
				month.setText(String.valueOf(c.get(Date.MONTH) + 1) + "��");
				
			}
		}
		
		class PlMainScreen extends JPanel{

			class PlMonth extends JPanel {

				class PlDate extends JPanel{
					
					class PlScheduleList extends JPanel{
						
						class PlSchedule extends JPanel{
							/*
							 *  PlSchedule
							 *  	-Explanation
							 */
							
							JLabel Explanation = new JLabel();
							boolean getschedule = false;
							Schedule s;
							
							PlSchedule(){
								this.setBackground(Color.WHITE);
								this.setLayout(new GridLayout(0,1,0,0));
								
								Explanation.setBorder(new EmptyBorder(0,0,0,0));
								this.add(Explanation);
								
							}
							
							void Set(Schedule s) {
								this.setBackground(DB.getColor().get(s.getColor()).color);
								this.setVisible(true);
								getschedule = true;
								//this.s = s;
							}
							void Reset() {
								this.setBackground(Color.WHITE);
								this.setVisible(false);
								getschedule = false;
								if(s != null)
									s.viewno = -1;
								Explanation.setText("");
							}

							void setText(Schedule s) {
								this.s=s;
								Explanation.setText(s.getExplanation());
							}
							

							void NotCurrentMonth() {
								if(!getschedule)
									this.setBackground(notcurrentcolor);
							}
						}
						
						/*
						 * 	PlScheduleList
						 * 		-PlSchedule * 3
						 */
						
						PlSchedule[] Schedule = new PlSchedule[3];
						
						PlScheduleList(){
							this.setLayout(new GridLayout(0,1,0,1));
							for(int i = 0 ; i< 3; i++) {
								Schedule[i] = new PlSchedule();
								this.add(Schedule[i]);
							}
						}
						
						void Set(ArrayList<Schedule> slist) {
							
							this.setVisible(true);
							if(slist == null) return;
							PriorityQueue<Schedule> que = new PriorityQueue<Schedule>();
							for(int i = 0; i < slist.size(); i++) {
								
								if(slist.get(i) instanceof BasisSchedule) {
									BasisSchedule s = (BasisSchedule)slist.get(i);
									if(s.holiday) {
										isholiday();
									}
								}
								
								if(slist.get(i).viewno != -1) {
									Schedule[slist.get(i).viewno].Set(slist.get(i));
									
								}
								else {
									que.add(slist.get(i));
								}
								
							}
							
							while(!que.isEmpty()) {
								Schedule s = que.poll();
								for(int i =0 ; i < 3; i++) {
									if(!Schedule[i].getschedule) {
										Schedule[i].Set(s);
										Schedule[i].setText(s);
										s.viewno = i;
										break;
									}
									
									
								}
							}
							
						}
						void Reset() {
							this.setBackground(Color.white);
							this.setVisible(false);
							for(PlSchedule s : Schedule) {
								s.Reset();
							}
						}
						
						void NotCurrentMonth() {
							this.setBackground(notcurrentcolor);
							for(int i = 0; i < 3; i++) {
								Schedule[i].NotCurrentMonth();
							}
						}
					}

					/*
					 * 	PlDate
					 * 		-lblDate
					 * 		-PlScheduleList
					 * 			-PlSchedule * 3
					 * 		-PlSpace
					*/

					JLabel lblDate = new JLabel();		//��¥ ��Ÿ���� ��
					JPanel space_south = new JPanel();	//�ϴ� ����
					ArrayList<Schedule> ScheduleList;	//������ ����Ʈ
					PlScheduleList ScheduleListview = new PlScheduleList();
					private boolean notcurrentmonth = false;
					
					int year, month, week, date;
					
					PlDate(){
						this.setLayout(new BorderLayout(0, 0));
						
						this.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								MouseClicked();
							}
							
						});
						
						lblDate.setBorder(new EmptyBorder(4,6,0,0));
						lblDate.setSize(new Dimension(10,15));
						this.add(lblDate, BorderLayout.NORTH);
						
						space_south.setPreferredSize(new Dimension(10,8));
						this.add(space_south, BorderLayout.SOUTH);
						
						this.add(ScheduleListview, BorderLayout.CENTER);
						
					}
					
					
					void Set(Date time) {
						notcurrentmonth = false;
						this.year = time.get(time.YEAR);
						this.month = time.get(Date.MONTH) + 1;
						this.week = time.get(Date.WEEK);
						this.date = time.get(Date.DATE);
						this.setBackground(Color.WHITE);
						space_south.setBackground(Color.WHITE);
						
						
						lblDate.setText(String.valueOf(date));

						if(week == 7) {
							lblDate.setForeground(Color.blue);
						}
						else if(week == 1) {
							lblDate.setForeground(Color.red);
						}
						
						Scheduleset();

					}
					void Reset() {
						ScheduleListview.Reset();
						lblDate.setForeground(Color.BLACK);
					}
					private void Scheduleset() {
						ScheduleListview.Reset();
						
						this.ScheduleList = DB.find(year, month, date);
						
						ScheduleListview.Set(ScheduleList);
					}
					
					void NotCurrentMonth() {
						notcurrentmonth = true;
						this.setBackground(notcurrentcolor);
						ScheduleListview.NotCurrentMonth();
						space_south.setBackground(notcurrentcolor);
					}
					
					void Refresh() {
						Scheduleset();
						if(notcurrentmonth) {
							this.NotCurrentMonth();
						}
						
					}
					
					void isholiday() {
						lblDate.setForeground(Color.red);
					}
					
					public void MouseClicked() {
						Date c = new Date();
						c.set(year, month - 1, date);
						
						StickyCalendar.getthis().CallScheduleListScreen(c, this.ScheduleList);
					}
					
					
				}
				
				/*
				 * 	PlMonth
				 * 		-PlDate * (6 * 7)
				 */
				
				PlDate[][] Datearr = null;

				int year;
				int month;
				
				PlMonth() {
					super();
					Datearr = new PlDate[6][7];
					
					//this.setBorder(new LineBorder(new Color(0, 0, 0), 0));
					this.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent arg0) {
						}
					});
					this.setBackground(Color.LIGHT_GRAY);
					this.setLayout(new GridLayout(0, 7, 1, 1));
					
					for(int i = 0;i < 6; i++) {
						for(int j = 0; j < 7; j++) {
							Datearr[i][j] = new PlDate();
							this.add(Datearr[i][j]);
						}
					}
				}

				void Reset() {
					for(int i =0; i< 6; i++) {
						for(int j = 0;j < 7; j++) {
							Datearr[i][j].Reset();
						}
					}
				}
				void Set(Date time) {
					
					year = time.get(time.YEAR);
					month = time.get(Date.MONTH) + 1;
					Date C = time.clone();
					C.set(Date.DATE, 1);
					
					do {
						C.add(Date.DATE, -1);
					} while(C.get(Date.WEEK) != 1);
						
				
					for(int i = 0; i < 6; i++) {
						for(int j = 0; j < 7; j++) {
							
							Datearr[i][j].Set(C);

							if(C.get(Date.MONTH) != month - 1) {
								Datearr[i][j].NotCurrentMonth();
							}
							
							C.add(Date.DATE, 1);
							
						}
					}
					
				}

				void Refresh() {
					for(int i = 0; i< 6; i++) {
						for(int j = 0; j < 7; j++) {
							Datearr[i][j].Refresh();
						}
					}
					this.revalidate();
					this.repaint();
				}
				
			}
			
			/*
			 * 	PlMainScreen
			 * 		-PlWeek
			 * 			-lbl * 7
			 * 		-PlMonth
			 * 
			 * 
			 */

			private JPanel PlWeek = new JPanel();
			private PlMonth CurrentMonth = new PlMonth();	//�̹���
			private Date TargetTime;  		//���õ� ��
			
			PlMainScreen(){
				this.setLayout(new BorderLayout());
				TargetTime = new Date();
				TargetTime.set(Date.DATE, 5);
				
				MakePlWeek();
				SetMonth(TargetTime);
				
				this.add(PlWeek, BorderLayout.NORTH);
				this.add(CurrentMonth, BorderLayout.CENTER);
			}
			
			private void MakePlWeek() {
				//���� �� �����
				PlWeek.setLayout(new GridLayout(0, 7, 0, 0));
				
				JLabel lblSun = new JLabel("Sun");
				lblSun.setForeground(Color.red);
				lblSun.setHorizontalTextPosition(SwingConstants.CENTER);
				lblSun.setHorizontalAlignment(SwingConstants.CENTER);
				lblSun.setAlignmentX(Component.CENTER_ALIGNMENT);
				PlWeek.add(lblSun);
				
				JLabel lblMon = new JLabel("Mon");
				lblMon.setHorizontalTextPosition(SwingConstants.CENTER);
				lblMon.setHorizontalAlignment(SwingConstants.CENTER);
				lblMon.setAlignmentX(Component.CENTER_ALIGNMENT);
				PlWeek.add(lblMon);
				
				JLabel lblTue = new JLabel("Tue");
				lblTue.setHorizontalTextPosition(SwingConstants.CENTER);
				lblTue.setHorizontalAlignment(SwingConstants.CENTER);
				lblTue.setAlignmentX(Component.CENTER_ALIGNMENT);
				PlWeek.add(lblTue);
				
				JLabel lblWed = new JLabel("Wed");
				lblWed.setHorizontalTextPosition(SwingConstants.CENTER);
				lblWed.setHorizontalAlignment(SwingConstants.CENTER);
				lblWed.setAlignmentX(Component.CENTER_ALIGNMENT);
				PlWeek.add(lblWed);
				
				JLabel lblThu = new JLabel("Thu");
				lblThu.setHorizontalTextPosition(SwingConstants.CENTER);
				lblThu.setHorizontalAlignment(SwingConstants.CENTER);
				lblThu.setAlignmentX(Component.CENTER_ALIGNMENT);
				PlWeek.add(lblThu);
				
				JLabel lblFri = new JLabel("Fri");
				lblFri.setHorizontalTextPosition(SwingConstants.CENTER);
				lblFri.setHorizontalAlignment(SwingConstants.CENTER);
				lblFri.setAlignmentX(Component.CENTER_ALIGNMENT);
				PlWeek.add(lblFri);
				
				JLabel lblSat = new JLabel("Sat");
				lblSat.setForeground(Color.blue);
				lblSat.setHorizontalTextPosition(SwingConstants.CENTER);
				lblSat.setHorizontalAlignment(SwingConstants.CENTER);
				lblSat.setAlignmentX(Component.CENTER_ALIGNMENT);
				PlWeek.add(lblSat);
				
				this.add(PlWeek, BorderLayout.NORTH);
			}

			private void NextMonth() {
				TargetTime.add(Date.MONTH, 1);
				CurrentMonth.Reset();
				CurrentMonth.Set(TargetTime);
				TopBar.setTime(TargetTime);
				
				PaintCurrentMonth();
			}
			private void PrevMonth() {
				TargetTime.add(Date.MONTH, -1);
				CurrentMonth.Reset();
				CurrentMonth.Set(TargetTime);
				TopBar.setTime(TargetTime);
				
				PaintCurrentMonth();
			}
			
			void SetMonth(Date time) {	//ǥ���� �� ����

				TargetTime.set(time.get(time.YEAR), time.get(Date.MONTH), time.get(Date.DATE));

				CurrentMonth.Reset();
				CurrentMonth.Set(TargetTime);
				TopBar.setTime(TargetTime);
				
				PaintCurrentMonth();
			}
			

			void Refresh() {
				CurrentMonth.Refresh();
			}
			
			private void PaintCurrentMonth() {	//�гο� ���õ� ���� Ķ���� ǥ��
				//this.removeAll();
				//this.add(PlWeek, BorderLayout.NORTH);
				//this.add(CurrentMonth, BorderLayout.CENTER);
				this.revalidate();
				this.repaint();
			}
			

		}

		/*
			-PlCalendarScreen
				-TopBar
				-MainScreen
		*/

		private PlTopBar TopBar = new PlTopBar();
		private PlMainScreen MainScreen = new PlMainScreen();

		
		PlCalendarScreen(){
			super();
			
			this.setLayout(new BorderLayout());
			
			this.add(TopBar, BorderLayout.NORTH);
			this.add(MainScreen, BorderLayout.CENTER);
			
		}

		
		void Refresh() {
			MainScreen.Refresh();
		}

	}

	class PlScheduleListScreen extends JPanel{

		class PlTopBar extends PlCanMoveFrame {
			
			JButton BtCancel = new ImageButton(DB.getImage().Backicon);
			JButton BtRemoveSchedule = new ImageButton(DB.getImage().Trashicon);
			JButton BtAddSchedule = new ImageButton(DB.getImage().Plusicon);
			
			PlTopBar(){
				MakeButton();
				
				this.addleft(BtCancel);
				this.addright(BtRemoveSchedule);
				this.addright(BtAddSchedule);
			}
			
			private void MakeButton() {
				BtCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ReturntoCalendar();
					}
				});
				
				BtRemoveSchedule.addActionListener(new ActionListener() {
					boolean b = false;
					public void actionPerformed(ActionEvent arg0) {
						RemoveScheduleMode();
					}
				});
				
				BtAddSchedule.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						AddnewSchedule();
					}
				});
			}
			
			void RemoveMode(boolean b) {
				if(b) {
					BtCancel.setEnabled(false);
					BtAddSchedule.setEnabled(false);
				}
				else {
					BtCancel.setEnabled(true);
					BtAddSchedule.setEnabled(true);
					
				}
			}
		}
		
		class PlMainScreen extends JPanel{
			
			class PlTitleBar extends JPanel{
				
				/*
				 *  PlTitleBar
				 *   PlNorth
				 *   	-lblyear
				 *   	-lblmonth
				 *   	-lbldate
				 *   PlSouth
				 *   	-lblL
				 *   	-lblLyear
				 *   	-lblLmonth
				 *   	-lblLdate
				 */
				
				JPanel PlNorth = new JPanel();
				JPanel PlSouth = new JPanel();
				
				JLabel lblyear = new JLabel();
				JLabel lblmonth = new JLabel();
				JLabel lbldate = new JLabel();
				
				JLabel lblL = new JLabel("����");
				JLabel lblLyear = new JLabel();
				JLabel lblLmonth = new JLabel();
				JLabel lblLdate = new JLabel();
				
				PlTitleBar(){
					super();
					this.setBackground(Color.white);
					this.setPreferredSize(new Dimension(10,100));
					this.setLayout(new GridLayout(0, 1, 0, 0));
					
					MakeNorth();
					MakeSouth();
					MakeLabel();
					Makethis();
					
				}
				
				private void Makethis() {
					FlowLayout FL = new FlowLayout(FlowLayout.CENTER,10,15);
					PlNorth.setLayout(FL);
					//PlSouth.setLayout(FL);
					
					PlNorth.setBackground(Color.WHITE);
					
					this.add(PlNorth);
					
					PlSouth.setBackground(Color.WHITE);
					this.add(PlSouth);
					
				}
				
				private void MakeNorth() {
					
					PlNorth.add(lblyear);
					PlNorth.add(lblmonth);
					PlNorth.add(lbldate);
				}
				private void MakeSouth() {
					
					PlSouth.add(lblL);
					PlSouth.add(lblLyear);
					PlSouth.add(lblLmonth);
					PlSouth.add(lblLdate);
				}
				private void MakeLabel() {
					
					lblyear.setSize(new Dimension(60,35));
					lbldate.setSize(new Dimension(30,35));
					lblmonth.setSize(new Dimension(30,35));
					
					Font font = new Font("Gulim", Font.BOLD, 16);
					lblyear.setFont(font);
					lblmonth.setFont(font);
					lbldate.setFont(font);
					
					Font Lfont = new Font("Gulim", Font.PLAIN, 14);
					lblL.setFont(Lfont);
					lblLyear.setFont(Lfont);
					lblLmonth.setFont(Lfont);
					lblLdate.setFont(Lfont);
					
				}
				
				
				void Import(Date c) {
					lblyear.setText(String.valueOf(c.getS(c.YEAR)) + "년");
					lblmonth.setText(String.valueOf(c.getS(Date.MONTH) + 1) + "월");
					lbldate.setText(c.getS(Date.DATE) + "일");
					
					lblLyear.setText(c.getL(c.YEAR) + "��");
					lblLmonth.setText((c.getL(Date.MONTH) + 1) + "��");
					lblLdate.setText(c.getL(Date.DATE) + "��");
				}
			
				
			}
			
			class PlScheduleList extends JPanel{

				class PlSchedule extends JPanel{
					/*
					 * 	PlSchedule 
					 * 		-ViewColor
					 * 		-PlContainer
					 * 			-ViewTime
					 * 			-ViewExplanation
					 * 			-ViewRemove
					 */
					
					class PlContainer extends JPanel{
						
						
						class ViewTime extends JPanel{
							
							JLabel lblStartdate = new JLabel();
							JLabel lblStart = new JLabel();
							JLabel lblCenter = new JLabel();
							JLabel lblEnd = new JLabel();
							JLabel lblEnddate = new JLabel();
							
							ViewTime(){
								super();
								
								this.setBackground(Color.white);
								
								lblStart.setHorizontalAlignment(SwingConstants.CENTER);
								lblCenter.setHorizontalAlignment(SwingConstants.CENTER);
								lblEnd.setHorizontalAlignment(SwingConstants.CENTER);
								
								this.setPreferredSize(new Dimension(70,10));
								this.setLayout(new GridLayout(0,1,0,0));
								
								
							}
							
							String getmin(Date c) {
								String str = "";
								
								int i = c.get(Date.MINUTE);
								if(i < 10) str = "0" + i;
								else str += i;
								
								return str;
							}
							
							void Set(Schedule s) {
								Date start = s.getStartDate();
								Date end = s.getEndDate();
								if(Date.DateEqual(start,time) && Date.DateEqual(end,time)) {
									
									lblStart.setText(start.get(Date.HOUR) + ":" + getmin(start));
									lblCenter.setText("~");
									lblEnd.setText(end.get(Date.HOUR) + ":" + getmin(end));
									
								}
								else if(Date.DateEqual(start, time)) {
									lblStart.setText(start.get(Date.HOUR) + ":" + getmin(start));
									lblCenter.setText("~");
									lblEnd.setText("");

								}
								else if(Date.DateEqual(end, time)) {
									lblStart.setText("");
									lblCenter.setText("~");
									lblEnd.setText(end.get(Date.HOUR) + ":" + getmin(end));
									
								}
								else {
									lblStart.setText("");
									lblCenter.setText("�Ϸ� ����");
									lblEnd.setText("");
								}
								
								this.add(lblStartdate);
								this.add(lblStart);
								this.add(lblCenter);
								this.add(lblEnd);
								this.add(lblEnddate);
							}

							void Reset() {
								this.removeAll();
							}
							
							
						}
						class ViewExplanation extends JPanel{

							JLabel Explanation = new JLabel();
							JLabel Place = new JLabel();
							
							ViewExplanation(){
								super();
								
								this.setBackground(Color.WHITE);
								this.setLayout(new GridLayout(0,1));
								this.add(Explanation);
								this.add(Place);
								this.setBorder(new EmptyBorder(0,10,0,0));
							}
							
							void Set(String E, String P) {
								Explanation.setText(E);
								Place.setText(P);
							}
							void Reset() {
								Explanation.setText("");
								Place.setText("");
							}
						}
						class ViewRemove extends JPanel{
							JButton BtRemove = new ImageButton(DB.getImage().Trashicon);
							boolean Mode = false; 
							
							ViewRemove(){
								this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
								this.setPreferredSize(new Dimension(50,10));
								this.setBackground(Color.WHITE);
								
								MakeButton();
								
								this.add(BtRemove);
								Set(Mode);
							}
							
							private void MakeButton() {

								BtRemove.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										Removethis();
									}
								});
							}
							
							void Set(boolean b) {
								Mode = b;
								BtRemove.setVisible(b);
							}
							
						}
						ViewTime Time = new ViewTime();
						ViewExplanation Explanation = new ViewExplanation();
						ViewRemove Remove = new ViewRemove();
						
						PlContainer(){
							super();

							this.setLayout(new BorderLayout());
							Reset();
							
							this.add(Time, BorderLayout.WEST);
							this.add(Explanation, BorderLayout.CENTER);
							this.add(Remove, BorderLayout.EAST);
						}
						
						void Set(Schedule s) {
							Time.Set(s);
							Explanation.Set(s.getExplanation(), s.getPlace());
						}
						void Reset() {
							Time.Reset();
							Explanation.Reset();
						}
						
						void RemoveMode(boolean b) {
							Remove.Set(b);
						}
					}
					
					Schedule Schedule = null;
					JPanel ViewColor = new JPanel();
					PlContainer PlContainer = new PlContainer();
					
					PlSchedule(){
						super();
						
						this.setBackground(Color.LIGHT_GRAY);
						ViewColor.setBackground(Color.WHITE);
						
						this.setLayout(new BorderLayout(1,0));
						
						
						this.setBackground(Color.WHITE);
						this.add(ViewColor, BorderLayout.WEST);
						this.add(PlContainer, BorderLayout.CENTER);
						
						
						this.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								if(!Basis) {
									ModifySchedule(Schedule);
								}
							}
						});
					}
					boolean Basis = false;
					
					void Reset() {
						ViewColor.setBackground(Color.WHITE);
						Schedule = null;
						
						PlContainer.Reset();
					}
					void Set(Schedule s) {
						Schedule = s;
						ViewColor.setBackground(DB.getColor().get(s.getColor()).color);
						PlContainer.Set(s);
						
						if(s instanceof BasisSchedule) {
							Basis = true;
						}
						else {
							Basis = false;
						}
					}
					
					void RemoveMode(boolean b) {
						if(Schedule != null && !Basis) {
							PlContainer.RemoveMode(b);
						}
					}
					
					private void Removethis() {
						RemoveSchedule(Schedule);
					}

					
				}
				
				/*
				 *  PlScheduleList
				 *   PlSchedule * 4
				 */
				
				PlSchedule[] Schedulearr = new PlSchedule[ListNum];
				
				PlScheduleList() {
					this.setLayout(new GridLayout(0,1,2,2));
					
				}
				
				
				void Import(Schedule[] arr) {
					for(int i = 0 ; i< ListNum; i++) {
						Schedulearr[i] = new PlSchedule();
						if(arr[i] != null) {
							Schedulearr[i].Set(arr[i]);
						}
						this.add(Schedulearr[i]);
					}
				}
				
				void RemoveMode(boolean b) {
					for(PlSchedule s : Schedulearr) {
						s.RemoveMode(b);
					}
				}
				
			}
			
			class PlPageBar extends JPanel{	//�����ٸ���Ʈ�� ��������ȯ �г�
				
				/*
				 *  PlPageBar
				 *   Prev
				 *   Viewpage
				 *   next
				 */
				
				JButton prev = new ImageButton(DB.getImage().Underlefticon);
				JButton next = new ImageButton(DB.getImage().Underrighticon);
				JLabel Viewpage = new JLabel();
				
				int Currentpage = 1;
				int Maxpage = 1;
				
				PlPageBar(){
					this.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
					this.setBackground(Color.WHITE);
					this.setPreferredSize(new Dimension(10,50));
					
					MakeButton();
					this.add(prev);
					this.add(Viewpage);
					this.add(next);
				}
				
				private void MakeButton() {
					prev.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							prev();
						}
					});
					
					next.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							next();
						}
					});
				}
				
				void SetPage(int i) {
					Maxpage = i;
					Currentpage = 1;
					
					prev.setEnabled(false);
					if(Maxpage == 1) {
						next.setEnabled(false);
					}
					else {
						next.setEnabled(true);
					}
					TextChange();
				}
				
				private void next() {
					Currentpage++;
					
					prev.setEnabled(true);
					if(Currentpage == Maxpage) {
						next.setEnabled(false);
					}
					ChangeList(Currentpage);
					TextChange();
				}
				private void prev() {
					Currentpage--;
					
					next.setEnabled(true);
					if(Currentpage == 1) {
						prev.setEnabled(false);
					}
					ChangeList(Currentpage);
					TextChange();
				}
				private void TextChange() {
					Viewpage.setText(String.valueOf(Currentpage) + " / " + String.valueOf(Maxpage));
				}
			}

			/*
			 * 	PlMainScreen
			 * 		-TitleBar
			 * 		-PlList ������ŭ
			 * 			-PlSchedule - PlList�� 4����
			 * 		-PlPageBar - PlList ��ȯ
			*/
			
			private static final int ListNum = 4;	//ȭ�� �ϳ��� ǥ�� ���� ��
			PlTitleBar TitleBar = new PlTitleBar();
			PlScheduleList[] ScheduleListarr;
			PlPageBar PageBar = new PlPageBar();
			Date time;
			
			PlMainScreen(){
				super();
				
				this.setLayout(new BorderLayout());
			}
			
			
			void Import(Date c, ArrayList<Schedule> list) {	//���� �Է�
				/*
				 * titlebar���� ��¥ ����
				 * �����ټ��� �������� ���
				 * ����������ŭ list�����
				 * list�鿡�� schedule�迭 ����
				 * pagebar���� �������� ����
				 * 1�������� ��ȯ
				 */
				time = c.clone();
				TitleBar.Import(c);
				
				int no;
				PriorityQueue<Schedule> queue = new PriorityQueue<Schedule>();
				
				if(list == null) {
					no = 1;
				}
				else {
					for(int i = 0; i< list.size(); i++) {
						queue.add(list.get(i));
					}
					
					
					no = list.size() / ListNum;
					if(list.isEmpty() || list.size() % ListNum != 0) {
						no++;
					}
				}
				
				ScheduleListarr = new PlScheduleList[no];
				
				
				
				
				for(int i = 0 ;i < no; i++) {
					ScheduleListarr[i] = new PlScheduleList();
					Schedule[] Schedulearr = new Schedule[ListNum];
					for(int j = 0 ; j < ListNum; j++) {
						try {
							Schedulearr[j] = queue.poll();
						}
						catch(Exception e) {
							Schedulearr[j] = null;
						}
					}
					ScheduleListarr[i].Import(Schedulearr);
				}
				
				PageBar.SetPage(no);
				ChangeList(1);
			}
			
			private void ChangeList(int i) {	//������ ��ȯ
				try {
					this.removeAll();
					this.add(TitleBar, BorderLayout.NORTH);
					this.add(PageBar, BorderLayout.SOUTH);
					this.add(ScheduleListarr[i - 1], BorderLayout.CENTER);
					this.revalidate();
					this.repaint();
				}
				catch(Exception e) {
					return;
				}
			}
			
			void RemoveMode(boolean b) {
				TopBar.RemoveMode(b);
				for(PlScheduleList s : ScheduleListarr) {
					s.RemoveMode(b);
				}
			}
			
		}
		
		/*	
		 * 	PlScheduleListScreen ����
		 * 		-TopBar
		 * 		-space_west
		 * 		-space_east
		 * 		-MainScreen
		 * 
		 * 	�۵�
		 * 		��¥ �г� Ŭ���� ��¥�� ������ ������ ����
		 * 		MainScreen�� ����
		 * 			��¥�� TitleBar��
		 * 			������ ������ PlSchedule�� ����� ������ ���� PlList, PlPageBar ����
		 */
		
		PlSpace space_west = new PlSpace();
		PlSpace space_east = new PlSpace();
		PlTopBar TopBar = new PlTopBar();
		PlMainScreen MainScreen = new PlMainScreen();
		Date time;
		boolean RemoveScheduleMode = false;
		
		PlScheduleListScreen(){
			super();
			this.setLayout(new BorderLayout());

			
			//�翷���� ���� ����
			this.add(space_west, BorderLayout.WEST);
			this.add(space_east, BorderLayout.EAST);

			//â�̵� ������ �г� ����
			this.add(TopBar, BorderLayout.NORTH);
			this.add(MainScreen, BorderLayout.CENTER);
			
		}
		
		void Import(Date c, ArrayList<Schedule> ScheduleList) {	//���� �Է�
			this.time = c.clone();
			MainScreen.Import(time, ScheduleList);
		}
		
		
		private void ReturntoCalendar() {
			getthis().ReturnCalendarScreen();
		}
		
		private void RemoveScheduleMode() {		//���� ���� ��� �¿���
			RemoveScheduleMode = !RemoveScheduleMode;
			MainScreen.RemoveMode(RemoveScheduleMode);
		}
		
		private void RemoveSchedule(Schedule s) {	//���� ����
			DB.RemoveSchedule(s);
			this.Refresh();
			MainScreen.RemoveMode(true);
		}
		
		private void AddnewSchedule() {		//�� ���� �߰�
			if(!RemoveScheduleMode)
				getthis().CallAddScheduleScreen(time);
		}
		
		private void ModifySchedule(Schedule s) {	//���� ����
			if(!RemoveScheduleMode && s != null)
				getthis().CallAddScheduleScreen(time, s);
		}
		
		
		void Refresh() {
			ArrayList<Schedule> ScheduleList = DB.find(time.get(time.YEAR), time.get(Date.MONTH) + 1, time.get(Date.DATE));
			MainScreen.Import(time, ScheduleList);
			
		}
	}
	
	class PlAddScheduleScreen extends JPanel {	//������ �߰� ȭ�� Ŭ����
		
		class PlTopBar extends PlCanMoveFrame{
			
			/*
			 *  PlTopBar
			 *   -left - BtCancel
			 *   -right - BtConfirm
			 *   -center
			 */
			
			ImageButton BtCancel = new ImageButton(DB.getImage().Backicon);
			ImageButton BtConfirm = new ImageButton(DB.getImage().Modifyicon);
			
			PlTopBar(){
				
				MakeButton();
				
				this.addleft(BtCancel);
				this.addright(BtConfirm);
			}
			
			private void MakeButton() {
				
				BtCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Cancel();
					}
				});
				
				
				BtConfirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(ModifyMode) {
							Save();
						}
						else {
							CallModifyMode();
						}
					}
				});
			}
		
			void ModifyMode(boolean b){
				if(b) {
					BtCancel.Set(DB.getImage().Xicon2);
					BtConfirm.Set(DB.getImage().Checkicon);
				}
				else {
					BtCancel.Set(DB.getImage().Backicon);
					BtConfirm.Set(DB.getImage().Modifyicon);
				}
				this.revalidate();
				this.repaint();
			}
		}
		
		class PlMainScreen extends JPanel{
			
			class PlTitleBar extends JPanel{
				
				/*
				 *  PlTitleBar
				 *   -North
				 *    	-lblTitle
				 *   -South
				 *    	-left
				 *     		-CBLunar
				 *    	-right
				 *     		-CBColor
				 */
				
				JLabel lblTitle = new JLabel();
				private JComboBox CBColor = new JComboBox();
				private JCheckBox CBLunar = new JCheckBox("����");
				
				JPanel North = new JPanel();
				JPanel South = new JPanel();
				
				PlTitleBar(){
					this.setLayout(new GridLayout(0,1,0,0));
					this.setPreferredSize(new Dimension(10,100));
					lblTitle.setFont(new Font("Gulim", Font.BOLD, 16));
					
					MakeComboBox();
					MakeButton();
					MakeNorth();
					MakeSouth();
					
					this.add(North);
					this.add(South);
					
				}
				private void MakeComboBox() {
					String[] str = new String[DB.getColor().no];

					for(int i =0; i < str.length; i++) {
						str[i] = String.valueOf(DB.getColor().get(i).name);
					}
					CBColor.setModel(new DefaultComboBoxModel(str));
					
					CBColor.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ChangeColor(CBColor.getSelectedIndex());
						}
					});
				}
				
				private void MakeButton() {
					CBLunar.setBackground(Color.WHITE);
					CBLunar.setFont(new Font("Gulim", Font.BOLD, 17));
					CBLunar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							CalendarChange(CBLunar.isSelected());
						}
					});
					
				}
				
				void Set(Schedule s) {
					if(s.getStartDate().getch()) {
						CBLunar.setSelected(true);
					}
					CBColor.setSelectedIndex(s.getColor());
					ChangeColor(s.getColor());
				}
				void Reset() {
					CBLunar.setSelected(false);
					CBColor.setSelectedIndex(0);
				}
				
				
				private void MakeNorth(){
					North.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
					North.setBackground(Color.white);
					North.add(lblTitle);
				}

				private void MakeSouth() {
					JPanel left = new JPanel();
					JPanel right = new JPanel();
					
					left.setBackground(Color.WHITE);
					right.setBackground(Color.WHITE);
					
					left.setLayout(new FlowLayout(FlowLayout.RIGHT,40,10));
					right.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 10));

					left.add(CBLunar);
					right.add(CBColor);
					South.setLayout(new GridLayout(0,2));
					
					South.add(left);
					South.add(right);
				}
				void Titleset(String str) {
					lblTitle.setText(str);
				}
				void Modifymode(boolean b) {
					CBLunar.setEnabled(b);
					CBColor.setEnabled(b);
				}
				
			}
			
			class PlEditSchedule extends JPanel{

				abstract class Base extends JPanel{
					
					JPanel North = new JPanel();
					JPanel South = new JPanel();
					Base(){
						North.setBackground(Color.WHITE);
						South.setBackground(Color.WHITE);
						
						FlowLayout FL = new FlowLayout(FlowLayout.CENTER, 10, 10);
						North.setLayout(FL);
						South.setLayout(FL);
						
						
						this.setLayout(new GridLayout(0,1,0,0));
						this.add(North);
						this.add(South);
					}
					
				}
				
				class PlString extends Base{	//����, ���
					
					JTextField txtExplanation = new JTextField();
					JTextField txtPlace = new JTextField();
					Dimension size = new Dimension(200,25);
					int col = 10;
					
					PlString(){
						MakeNorth();
						MakeSouth();
					}
					protected void MakeNorth() {
						JLabel lblExplanation = new JLabel("����");
						North.add(lblExplanation);
						
						txtExplanation.setPreferredSize(size);
						txtExplanation.setSize(size);
						North.add(txtExplanation);
					}
					protected void MakeSouth() {
						JLabel lblPlace = new JLabel("���");
						South.add(lblPlace);
						
						txtPlace.setPreferredSize(size);
						txtPlace.setSize(size);
						South.add(txtPlace);
					}
					
					void Set(String Explanation, String Place) {
						txtExplanation.setText(Explanation);
						txtPlace.setText(Place);
					}
					
					void Reset() {
						txtExplanation.setText("");
						txtPlace.setText("");
					}
					
					String[] Export() {
						String[] data = new String[2];
						data[0] = txtExplanation.getText();
						data[1] = txtPlace.getText();
						
						return data;
					}
				
					void ModifyMode(boolean b) {
						
						txtExplanation.setEditable(b);
						txtPlace.setEditable(b);
					}
				}
				
				class PlTime extends Base{		//�ð�
					
					JComboBox[] CBTime = new JComboBox[5];
					//�� �� �� �� ��
					
					Date time = new Date();
					final boolean start;
					
					PlTime(boolean b){
						super();
						start = b;
						if(b) {
							North.add(new JLabel("����  "));
						}
						else {
							North.add(new JLabel("����  "));
						}
						
						MakeComboBox();
						SetComboBox();
						
						MakeNorth();
						MakeSouth();

						
					}
					protected void MakeNorth() {
						North.add(CBTime[0]);
						North.add(new JLabel("�� "));
						North.add(CBTime[1]);
						North.add(new JLabel("�� "));
						North.add(CBTime[2]);
						North.add(new JLabel("��"));
					}
					protected void MakeSouth() {
						South.add(CBTime[3]);
						South.add(new JLabel("��  "));
						South.add(CBTime[4]);
						South.add(new JLabel("��  "));
					}
					
					private void MakeComboBox() {
						
						final String[] yeararr = new String[DataBase.EndLimit - DataBase.StartLimit + 1];
						
						CBTime[0] = new JComboBox();
						for(int i = 0; i< yeararr.length; i++) {
							yeararr[i] = String.valueOf(DataBase.StartLimit + i);
						}
						CBTime[0].setModel(new DefaultComboBoxModel(yeararr));
						
						
						for(int i = 1; i< 5; i++) {
							String[] strarr = new String[time.getActualMaximum(converter(i))];
							int k = 0;
							if(converter(i) == Date.MONTH || converter(i) == Date.DATE)
								k = 1;
							for(int j = 0; j < strarr.length; j++) {
								strarr[j] = String.valueOf(j + k);
							}
							CBTime[i] = new JComboBox();
							CBTime[i].setModel(new DefaultComboBoxModel(strarr));
						}

					}
					
					private void SetComboBox() {
						
						Dimension size = new Dimension(40,25);

						for(int i = 0; i < 5; i++) {
							CBTime[i].setSize(size);
							final int no = i;
							CBTime[i].addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									Changed(no, (String)CBTime[no].getSelectedItem());
								}
							});
						}
						CBTime[0].setSize(new Dimension(60,25));

					}
					
					private void Changed(int no, String str) {
						int field = converter(no);
						int value = Integer.parseInt(str);
						if (field == Date.MONTH) value--;
						time.set(field, value);
						
						if(field == time.YEAR || field == Date.MONTH) {
							DateBoxChange();
						}
					}
					private void DateBoxChange() {
						int index = CBTime[2].getSelectedIndex();
						
						String[] datearr = new String[time.getActualMaximum(Date.DATE)];
						for(int i = 0 ; i< datearr.length; i++) {
							datearr[i] = String.valueOf(i + 1);
						}
						
						CBTime[2].setModel(new DefaultComboBoxModel(datearr));
						while(index > datearr.length - 1) {
							--index;
						}
						CBTime[2].setSelectedIndex(index);
					}
					
					private int converter(int no) {
						switch(no) {
						case 0:
							return time.YEAR;
						case 1:
							return Date.MONTH;
						case 2:
							return Date.DATE;
						case 3:
							return Date.HOUR;
						case 4:
							return Date.MINUTE;
							
							default:
								return 0;
						}
					}

					void CalendarChange(boolean b) {
						if(b) {
							time.ConverttoChineseCalendar();
							
						}
						else {
							time.ConverttoCalendar();
						}
						this.Set(time);
					}
					void setFullTime(boolean b) {
						CBTime[3].setEnabled(!b);
						CBTime[4].setEnabled(!b);
					}
					void ModifyMode(boolean b) {
						for(int i = 0; i < 5; i++) {
							CBTime[i].setEnabled(b);
						}
					}
					
					void Set(Date c) {
						time = c.clone();
						
						
						CBTime[0].setSelectedIndex(c.get(time.YEAR) - DataBase.StartLimit);
						CBTime[1].setSelectedIndex(c.get(Date.MONTH));
						CBTime[2].setSelectedIndex(c.get(Date.DATE) - 1);
						CBTime[3].setSelectedIndex(c.get(Date.HOUR));
						CBTime[4].setSelectedIndex(c.get(Date.MINUTE));
						
					}
					
					Date Export() {
						return time.clone();
					}
				}
				
				class PlSelectBar extends Base{
					
					private final ButtonGroup buttonGroup = new ButtonGroup();
					private JRadioButton[] RBRepeat = new JRadioButton[5];

					
					PlSelectBar(){
						
						this.setLayout(new GridLayout(0,1));
						
						MakeButton();
						MakeNorth();
						MakeSouth();
						
						this.add(North);
						this.add(South);
					}
					
					private void MakeButton() {
						String[] str = {"�ѹ�", "����", "����", "�ſ�", "�ų�"};
						
						for(int i = 0; i< 5; i++) {
							JRadioButton RB = new JRadioButton(str[i]);
							RB.setBackground(Color.WHITE);
							RBRepeat[i] = RB;
							buttonGroup.add(RB);
							North.add(RB);
						}
						RBRepeat[0].setSelected(true);
					}
					
					
					
					protected void MakeNorth() {
//						North.setLayout(new GridLayout(0,2));
//						JPanel left = new JPanel();
//						JPanel right = new JPanel();
//						left.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
//						right.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
//						left.setBackground(Color.white);
//						right.setBackground(Color.white);
//						North.add(left);
//						North.add(right);
//						
//						left.add(CBLunar);
						
					}
					protected void MakeSouth() {
						South.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
					}
					
					void Set(Schedule s) {
						RBRepeat[s.getRepeat()].setSelected(true);
					}
					
					void Reset() {
						RBRepeat[0].setSelected(true);
					}
					
					int[] Export() {
						int[] data = new int[1];
						data[0] = getSelected();
						
						return data;
					}

					int getSelected() {
						int no = 0;
						for(int i = 0; i < 5; i++) {
							if(RBRepeat[i].isSelected()) {
								no = i;
								break;
							}
						}
						return no;
					}
				
					void ModifyMode(boolean b) {
						for(int i = 0; i< 5; i++) {
							RBRepeat[i].setEnabled(b);
							
						}
						
					}
				
				}
				
				/*
				 *  PlEditSchedule
				 *   -Explanation
				 *   -StartTime
				 *   -EndTime
				 *   -SelecteBar
				 */
				
				PlString Explanation = new PlString();
				PlTime StartTime = new PlTime(true);
				PlTime EndTime = new PlTime(false);
				PlSelectBar SelectBar = new PlSelectBar(); 
				
 				PlEditSchedule(){
					this.setLayout(new GridLayout(0,1,0,2));
					
					this.add(Explanation);
					this.add(StartTime);
					this.add(EndTime);
					this.add(SelectBar);
				}
				
 				void CalendarChange(boolean b) {	//��, ��� ��ȯ
 					StartTime.CalendarChange(b);
 					EndTime.CalendarChange(b);
				}
 				
 				//������ �ޱ�
				void Import(Date c) {
					Explanation.Reset();
					c.set(Date.HOUR, 9);
					c.set(Date.MINUTE, 0);
					StartTime.Set(c);
					c.set(Date.HOUR, 10);
					EndTime.Set(c);
					
					SelectBar.Reset();
				}
				void Import(Schedule s) {
					
					Date start = s.getStartDate().clone();
					Date end = s.getEndDate().clone();

					Explanation.Set(s.getExplanation(), s.getPlace());
					StartTime.Set(start);
					EndTime.Set(end);
					SelectBar.Set(s);
				}
				
				//�� �г��� �����͸� ������ ���� �����
				Schedule MakeSchedule() {
					
					if(s != null) {
						DB.RemoveSchedule(s);
					}
					if(SelectBar.Export()[0] == 0)
						s = new OnceSchedule();
					else {
						s = new RepeatSchedule();
					}
					String[] strdata = Explanation.Export();
					s.setExplanation(strdata[0]);
					s.setPlace(strdata[1]);
					
					Date start = StartTime.Export();
					Date end = EndTime.Export();
					
					s.setStartDate(start);
					s.setEndDate(end);
					
					s.setRepeat(SelectBar.getSelected());
					
					return s;
				}
			
				//���� ���� ���
				void ModifyMode(boolean b) {
					Explanation.ModifyMode(b);
					StartTime.ModifyMode(b);
					EndTime.ModifyMode(b);
					SelectBar.ModifyMode(b);
				}

			}
			
			/*
			 *  PlMainScreen
			 *   -PlTitleBar
			 *   -PlEditSchedule
			 */
			
			PlTitleBar TitleBar = new PlTitleBar();
			PlEditSchedule EditSchedule = new PlEditSchedule();
			Schedule s;
			
			
			PlMainScreen(){
				this.setLayout(new BorderLayout());
				this.add(TitleBar, BorderLayout.NORTH);
				this.add(EditSchedule, BorderLayout.CENTER);
				
			}
			
			//������ �ޱ�
			void Import(Date c) {
				s = null;
				
				ChangeColor(0);
				TitleBar.Titleset("���� �߰�");
				TitleBar.Reset();
				EditSchedule.Import(c);
			}
			void Import(Schedule s) {
				this.s = s;
				TitleBar.Titleset("���� Ȯ��");
				TitleBar.Set(s);
				EditSchedule.Import(s);
			}
			
			//�� ��� ��ȯ
			private void CalendarChange(boolean b) {
				EditSchedule.CalendarChange(b);
			}
			
			//���� ����
			Schedule SaveSchedule() {
				TitleBar.Titleset("���� Ȯ��");
				return EditSchedule.MakeSchedule();
			}
			
			void CancelModify() {	// ��� ��ư
				TitleBar.Titleset("���� Ȯ��");
				if(s != null) {
					Import(s);
				}
				else {
					getthis().ReturnScheduleListScreen();
				}
			}
			
			void StartModify() {	//���� ���� ����
				TitleBar.Titleset("���� ����");
			}
			
			void ModifyMode(boolean b) {	//���� ���� ��� ��ȯ
				TitleBar.Modifymode(b);
				EditSchedule.ModifyMode(b);
			}
		}

		/*
		 *  PlAddScheduleScreen
		 *   -PlTopBar
		 *   -PlMainScreen
		 */
		
		PlTopBar TopBar = new PlTopBar();
		PlMainScreen MainScreen = new PlMainScreen();
		PlSpace space_west = new PlSpace();
		PlSpace space_east = new PlSpace();
		boolean ModifyMode = false;
		int colorno;
		
		PlAddScheduleScreen(){
			super();
			
			this.setLayout(new BorderLayout());

			
			this.add(TopBar, BorderLayout.NORTH);
			this.add(MainScreen, BorderLayout.CENTER);
			this.add(space_east, BorderLayout.EAST);
			this.add(space_west, BorderLayout.WEST);
		}
		
		//������ �Է�
		void Import(Date c) {
			ModifyModeChange(true);
			MainScreen.Import(c);
		}
		void Import(Date c, Schedule s) {
			ModifyModeChange(false);
			MainScreen.Import(s);
		}
		
		private void ModifyModeChange(boolean b) {	//���� ��� ��ȯ
			ModifyMode = b;
			MainScreen.ModifyMode(b);
			TopBar.ModifyMode(b);
		}
		
		private void ChangeColor(int colorno) {
			this.colorno = colorno;
			MainScreen.setBorder(new LineBorder(DB.getColor().get(colorno).color, 2));
		}
		
		private void Cancel() {		//��ҹ�ư
			if(ModifyMode) {
				ModifyModeChange(false);
				MainScreen.CancelModify();
			}
			else {
				getthis().ReturnScheduleListScreen();
			}
		}
		
		private void CallModifyMode() {		//������� ȣ��
			ModifyModeChange(true);
			MainScreen.StartModify();
			
		}
		
		private void Save() {
			ModifyModeChange(false);
			
			Schedule s = MainScreen.SaveSchedule();
			s.setColor(colorno);
			
			DB.AddSchedule(s);
			ScheduleListScreen.Refresh();
			DB.SaveSchedule();
			TopBar.ModifyMode(ModifyMode);
		}
		
	}

	class PlSouthBar extends JPanel { 	//�ϴ� ����, Resize ���� �г�

		class Motion{
			int x, y;
			
			Dimension d = new Dimension();
			
			void SetPoint(int x, int y) {
				this.x = x;
				this.y = y;
				d = getthis().getSize();
			}
			void MovePoint(int x, int y) {
				getthis().setSize(d.width + x - this.x, d.height + y - this.y);
				DB.getUserSetting().Set(DB.getUserSetting().SIZE, d.width + x - this.x, d.height + y - this.y);
			}
		}
		
		/*
		 *  Motion m�� �̿��ؼ� â ��������
		 */
		
		PlSouthBar(){
			super();
			
			this.setBackground(Color.white);
			this.setLayout(new BorderLayout());
			
			JPanel PlResize = new JPanel();
			PlResize.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			PlResize.setBackground(Color.PINK);
			PlResize.setPreferredSize(new Dimension(8,8));
			

			Motion m = new Motion();
			
			PlResize.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					m.SetPoint(e.getXOnScreen(), e.getYOnScreen());
				}
			});
			
			PlResize.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					m.MovePoint(e.getXOnScreen(), e.getYOnScreen());
					
				}
			});
			
			this.add(PlResize, BorderLayout.EAST);
		}
		
		
	}
	
	/*
	 * 	StickyCalendar
	 * 	-Screen
	 * 		(
	 * 		-PlCalendarScreen
	 * 			or
	 * 		-PlScheduleListScreen
	 * 			or
	 * 		-PlAddScheduleScreen
	 * 		)
	 * 		-PlSouthBar 
	 */
	
	
	private static StickyCalendar StickyCalendarinstance;
	private static final int CALENDAR = 1;
	private static final int SCHEDULELIST = 2;
	private static final int ADDSCHEDULE = 3;
	
	
	private static final Color notcurrentcolor = SystemColor.control;	//�̹� �� �ƴҶ��� �÷�
	private final DataBase DB;

	private final JPanel Screen;		//�⺻ ȭ�� �г�
	
	private final PlCalendarScreen CalendarScreen;		//���� ȭ�� �г�
	private final PlScheduleListScreen ScheduleListScreen;	//���� ����Ʈ �г�
	private final PlAddScheduleScreen AddScheduleScreen;	//���� �߰� �г�

	private final PlSouthBar SouthBar;		//�� �� �г�
	
	
	public static void main(String[] args) {
		StickyCalendarinstance = new StickyCalendar();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StickyCalendar frame = getthis();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}

	static StickyCalendar getthis() {		//�ڽ��� �ּ� ����
		return StickyCalendarinstance;
	}
	
	private StickyCalendar() {
		
		Screen = new JPanel();		//��ũ�� �г�
		DB = DataBase.getInstance();		//�����ͺ��̽� ����, ����� ������ �б�
		CalendarScreen = new PlCalendarScreen();	//Ķ���� ȭ�� ����
		ScheduleListScreen = new PlScheduleListScreen();	//������ ����Ʈ ȭ�� ����
		AddScheduleScreen = new PlAddScheduleScreen();	//������ �߰� ȭ�� ����
		SouthBar = new PlSouthBar();//�ϴ� �� ����
		
		MakeBase();					//�⺻ ���� ����
		ChangeScreen(CALENDAR);	//Ķ���� ȭ������ ����
	}
	
	private void MakeBase() {		//�⺻ ȭ�� ����

		setTitle("Sticky Calendar");					//���� ����		
		setUndecorated(true);							//�⺻â �Ⱥ��̰� ����
		
		this.addWindowListener(new WindowAdapter() {	//â ���� ����
			public void windowClosing(WindowEvent e) {
				getthis().ProgramClose();
			}
		});
		
		
		setMinimumSize(new Dimension(400, 500));		//�ּ� ������ ����
		
		//������, ��ġ ����
		setSize(DB.getUserSetting().get(DB.getUserSetting().Width), DB.getUserSetting().get(DB.getUserSetting().Height));
		setLocation(DB.getUserSetting().get(DB.getUserSetting().X), DB.getUserSetting().get(DB.getUserSetting().Y));
		
		//Screen ����� ContentPane���� ����
		Screen.setBorder(new EmptyBorder(0, 0, 0, 0));
		Screen.setLayout(new BorderLayout(100, 0));
		setContentPane(Screen);							
	}
	

	private void ProgramClose() {	//���α׷� ����ɶ� ����Ǵ� �͵� ����
		DB.getUserSetting().save();
		System.exit(0);
	}
	
	private void ChangeScreen(int i) {	//â ��ȯ
		/*
		 	1 = CalendarScreen
			2 = ScheduleListScreen
			3 = AddScheduleScreen
		*/
		JPanel p;
		switch(i) {
		case CALENDAR:
			p = CalendarScreen;
			break;
		case SCHEDULELIST:
			p = ScheduleListScreen;
			break;
		case ADDSCHEDULE:
			p = AddScheduleScreen;
			break;
			
			default:
				return;
		}
		
		Screen.removeAll();
		Screen.add(p, BorderLayout.CENTER);
		Screen.add(SouthBar, BorderLayout.SOUTH);
		revalidate();
		repaint();
		
	}
	
	private void CallScheduleListScreen(Date d, ArrayList<Schedule> list) {	//��������Ʈȭ�� ȣ��
		ScheduleListScreen.Import(d, list);
		getthis().ChangeScreen(SCHEDULELIST);
	}
	private void ReturnCalendarScreen() {	//Ķ���� ȭ������ ����
		CalendarScreen.Refresh();
		ChangeScreen(CALENDAR);
	}
	
	private void CallAddScheduleScreen(Date d) {	//�������� ȭ�� ȣ��(���� �߰�)
		AddScheduleScreen.Import(d);
		getthis().ChangeScreen(ADDSCHEDULE);
	}
	private void CallAddScheduleScreen(Date d, Schedule s) {	//�������� ȭ�� ȣ��(���� ����)
		AddScheduleScreen.Import(d, s);
		getthis().ChangeScreen(ADDSCHEDULE);
	}
	private void ReturnScheduleListScreen() {		//���� ����Ʈ ȭ������ ����
		ScheduleListScreen.Refresh();
		getthis().ChangeScreen(SCHEDULELIST);
	}
	
	
}
