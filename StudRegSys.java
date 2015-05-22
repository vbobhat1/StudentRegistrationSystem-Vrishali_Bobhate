import java.io.BufferedReader;
import java.io.*;
import java.sql.*; 
import java.util.*;
import java.lang.*;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.*;
import java.math.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;

public class StudRegSys {
	 static String username;
         static String pwd;
	 static int my_flag=0;
	 static  CallableStatement cs2;

/* @author : Vrishali Bobhate */

	 /* -------------------------------------------------------------------------------
	  * This procedure is to search for prerequisite courses 
	  * -------------------------------------------------------------------------------
	 */ 


public static void search_prerequisites() throws SQLException{

		String deptcode = new String();
		int course_no;
		try
		{
			System.out.println("Please enter the Department Code");
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                        deptcode= br.readLine();
			System.out.println("Please enter the course#");
			course_no = Integer.parseInt(br.readLine());

//System.out.println(deptcode);
//System.out.println(course_no);

//Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
Connection conn = ds.getConnection("vbobhat1", "BingData");
CallableStatement cs = conn.prepareCall ("begin StudRegSys.course_info(:1,:2,:3,:4); end;");
cs.setString(1,deptcode);
			cs.setInt(2,course_no);
       			cs.registerOutParameter(3,OracleTypes.CURSOR);
       			cs.registerOutParameter(4,OracleTypes.CURSOR);
       			// execute and retrieve the result set


       			cs.execute();  
      
                       ResultSet rs = (ResultSet)cs.getObject(4);
                                
                        while (rs.next())
                        {

                              System.out.println(rs.getString(1) + "\t" +rs.getString(2));
		
			}



		}

		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
     }

	 /* -------------------------------------------------------------------------------
	  * This procedure is to drop a student from class
	  * -------------------------------------------------------------------------------
	 */ 


public static void drop() throws SQLException{
		 
		
		 String sid = new String();
		 String classid = new String();

		 try{
         		System.out.println("Please enter the sid(starting with 'B')\n");  
		 	BufferedReader br = new BufferedReader(new InputStreamReader(System.in ));  
		 

			sid = br.readLine();

		 	System.out.println("Please enter the classid(starting with 'c')\n");  
		
			classid=br.readLine();
//Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
Connection conn = ds.getConnection("vbobhat1", "BingData");
CallableStatement cs = conn.prepareCall ("begin StudRegSys.delete_enrollments(:1,:2,:3); end;");
	      	    
	    		cs.setString(1,sid);
	    		cs.setString(2,classid);
			cs.registerOutParameter(3,Types.VARCHAR);
	    		cs.executeQuery();
			String output = new String(cs.getString(3));
			System.out.println("MESSAGE:" + output);
			System.out.println("\n\n");
		 
		 }
catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
     }
	 /* -------------------------------------------------------------------------------
	  * This procedure is to delete the entire  student records  from the registration 
	  * -------------------------------------------------------------------------------
	 */ 
	 
	 public static void delete() throws SQLException{
	 
	
		  String sid = new String();
		 try{
         		System.out.println("Please enter the sid(Starting with 'B')\n");  
		 	BufferedReader br = new BufferedReader(new InputStreamReader(System.in ));  
			sid = br.readLine();
 //Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
Connection conn = ds.getConnection("vbobhat1", "BingData");
CallableStatement cs = conn.prepareCall ("begin StudRegSys.delete_student(:1); end;");
	      	    
	    		cs.setString(1,sid);
	    		cs.executeQuery();
	    		System.out.println("Student Successfully deleted from student table!");
			System.out.println("\n\n");
		 
		 }
	catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
     }
	 	 /* -------------------------------------------------------------------------------
	  * This procedure is to enroll the students into the class 
	  * -------------------------------------------------------------------------------
	 */ 
	 
	 
	 public static void enroll() throws SQLException{
		 try{
			//Declarations  
			String sid = new String();
			String classid =  new String();			 
					 
			System.out.println("Please enter the students sid (starting with 'B')\n");  
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in ) );  
			sid = br.readLine();
						 			 
			System.out.println("Please enter the classid (starting with 'c')\n");  
			classid = br.readLine();

//Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
	Connection conn = ds.getConnection("vbobhat1", "BingData");
	CallableStatement cs = conn.prepareCall ("begin StudRegSys.enroll_student(:1,:2); end;");
		        
		    	cs.setString(1,sid);
		    	cs.setString(2,classid);		    
	        	cs.executeQuery();
			
	        	System.out.println("Student Successfully Enrolled!");
	        
	        	// close the result set, statement, and the connection
		    	cs.close();
		    	conn.close(); 
		 }
catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
	 }
	 
	 /* -------------------------------------------------------------------------------
	  * This procedure helps to see the students information
	  * -------------------------------------------------------------------------------*/ 
	  public static void students_info() throws SQLException{
		try
		{ 

			String sid = new String();
		
		 	System.out.println("Please enter the students sid (starting with 'B') \n");  
		 	BufferedReader br = new BufferedReader(new InputStreamReader(System.in ));  
	
			sid = br.readLine();
			
//Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
	Connection conn = ds.getConnection("vbobhat1", "BingData");
	 
CallableStatement cs = conn.prepareCall ("begin StudRegSys.student_info(:1,:2,:3); end;");
			cs.setString(1,sid);
	    		cs.registerOutParameter(2,OracleTypes.CURSOR); 
                 	cs.registerOutParameter(3, Types.VARCHAR);      
	    		
//execute and retrieve the result set
        		cs.execute();
        		ResultSet rs;
	
        		rs = (ResultSet)cs.getObject(2);
        		boolean a1=false;       
			System.out.println("SID" + "\t" + "FIRSTNAME" + "\t" + "LASTNAME"+ "\t" + "DEPT-COURSE#" + "\t" + "TITLE"); 
        		System.out.println("---" + "\t" + "---------" + "--------" + "\t" + "-----------" + "\t" + "-----");
			while (rs.next()) {       	  
        	            	  
        			System.out.println(sid + "\t" + rs.getString(3) + "\t" + rs.getString(1) + "\t" + rs.getString(2));
                	
           			a1=true;
        		}
			
        		if(a1==false)
	    			System.out.println("The student is not enrolled for any course");
		
			 System.out.println("\n\n");

        		cs.close();
	    		conn.close();		
		}
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}		
	}


	 /* -------------------------------------------------------------------------------
	  * This procedure is used to add students to the student table 
	  * -------------------------------------------------------------------------------
	 */ 
	 
	 public static void Add_students() throws SQLException 
	 {
		try
		{
			//Declarations  
			Float gpa=0.0f;
			String fname="",lname="",status="";
			String email="";
			String sid="";
			System.out.println("Please enter the students sid (starting with 'B')\n");  
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in ) );  
			sid = br.readLine(); 
					 			
			System.out.println("Please enter the students First  name \n");
			fname=br.readLine();

			 System.out.println("Please enter the students Last  name \n");                                  
                        lname=br.readLine();
			
				System.out.println("Please enter the students status (('freshman', 'sophomore', 'junior', 'senior', 'graduate'))\n");  
				status = br.readLine();
		
			do{
				System.out.println("Please enter the students GPA ((gpa >= 0 and gpa <= 4.0))\n");  
				gpa = Float.parseFloat(br.readLine());
			}while(gpa<0.0 && gpa>4.0);

						  
			System.out.println("Please enter the students email address\n");  
			email = br.readLine();
      //Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
        Connection conn = ds.getConnection("vbobhat1", "BingData");
	CallableStatement cs = conn.prepareCall ("begin StudRegSys.add_student(:1,:2,:3,:4,:5,:6); end;");
			cs.setString(1,sid);		       	    
		    	cs.setString(2,fname);
			cs.setString(3,lname);
		    	cs.setString(4,status);
		    	cs.setFloat(5,gpa);
		    	cs.setString(6,email);
			cs.executeQuery();
	       
	        	System.out.println("Student Succesfully Added!");
	        
	        	// close the result set, statement, and the connection
		    	cs.close();
		    	conn.close();
		 }
	catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
	 }
/* -----------------------------------------------------------------------
	 * This function DISPLAYs the contents of the table selected by the user. 
	 * ----------------------------------------------------------------------- */
	 public static void Display (int Table_num) throws SQLException
	 {
		 try{		
			//Declarations
			int flag =0; //to check if data is found or not
			String query = new String();
			
      
			if (Table_num ==1) 
	        		query =  "begin StudRegSys.show_students(:1); end;" ;
	        	else if (Table_num == 2)
	        		query =  "begin StudRegSys.show_classes(:1); end;" ;
	        	else if (Table_num == 3)
	        		query =  "begin StudRegSys.show_prerequisites(:1); end;" ;
			else if (Table_num == 4)
                                query =  "begin StudRegSys.show_enrollments(:1); end;" ;
                        else if (Table_num == 5)
                                query =  "begin StudRegSys.show_courses(:1); end;" ;
	        	else if (Table_num == 6)
	        		query =  "begin StudRegSys.show_logs(:1); end;" ;
			else if	(Table_num == 7)
				{
					search_prerequisites();
					return;
				}
			else if	(Table_num == 8)
				{
				 	students_info();
					return;
				}
			/*else if (Table_num == 8)
				query = "begin StudRegSys.student_info(:1,:2,:3);end;" ;*/

//Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
        Connection conn = ds.getConnection("vbobhat1", "BingData");
CallableStatement cs = conn.prepareCall (query);
	        
	        	cs.registerOutParameter(1,OracleTypes.CURSOR);
	        
	        	//execute and retrieve the result set
	        	cs.execute();
	        	ResultSet rs;
		
	        	rs = (ResultSet)cs.getObject(1);
	                
	        	// print the results for Show_students
	        	if(Table_num == 1){   
				System.out.println("SID" + "\t" + "FIRSTNAME" + "\t" + "LASTNAME" + "\t" + "STATUS" + "\t" + "\t" + "GPA" + "\t" + "\t" + "EMAIL");
	        		System.out.println("---" + "\t" + "---------" + "\t" + "--------" + "\t" + "------" + "\t" + "\t" + "---" + "\t" + "\t" + "-----");
	        		while (rs.next()){
	        		flag=1; //indicates data found
				System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + "\t" + rs.getString(3) + "\t" + "\t" + rs.getString(4) + "\t" + "\t" 
+ rs.getFloat(5) + "\t" + "\t" + rs.getString(6));
	        		}
	        	}
// print the results for Show_classes       
	        	else if(Table_num == 2){   
				System.out.println("CLASSID" + "\t" + "DEPT_CODE" + "\t" + "COURSE#" + "\t" + "SECT#" + "\t" + "YEAR" + "\t" +
	        		"SEMESTER" + "\t" + "LIMIT" + "\t" + "CLASS_SIZE" );
	        		System.out.println("---" + "\t" + "---------" + "\t" + "-------" + "\t" + "-----" + "\t" + "----" + "\t" + 
				"--------" + "\t" + "-----" + "\t" + "----------" );
	        			
	        		while (rs.next())
	        		{
	        			flag=1; //indicates data found
	        			System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + "\t" + rs.getInt(3) + "\t" + rs.getInt(4) + "\t"+ rs.getInt(5) + "\t" + rs.getString(6) + "\t" +"\t" + rs.getInt(7) + "\t" + "\t" + rs.getInt(8) ); 
	        		}
	        	}
			// print the results for Show_Prerequities
                        else if(Table_num == 3){
                                System.out.println("DEPT_CODE" + "\t" + "COURSE#" + "\t" + "PRE_DEPT_CODE" + "\t" + "PRE_COURSE#");
                                System.out.println("---------" + "\t" + "-------" + "\t" + "-------------" + "\t" + "-----------");
                                while (rs.next())
                                {
                                        flag=1; //indicates data found
                                        System.out.println(rs.getString(1) + "\t" + "\t" + rs.getInt(2) + "\t" +  rs.getString(3) + "\t" + "\t" + rs.getInt(4));
                                }
                        }
	        	// print the results for Show_enrollment
	        	else if(Table_num == 4){   
				System.out.println("SID" + "\t" + "CLASSID" + "\t" + "LGRADE");
	        		System.out.println("---" + "\t" + "-------" + "\t" + "------");
	        		while (rs.next())
	        		{
	        			flag=1; //indicates data found
	        			System.out.println(rs.getString(1) + "\t" + rs.getString(2)+ "\t" +  rs.getString(3));
	        		}
	        	}
			// print the results for Show_courses
                        else if(Table_num == 5){
                                System.out.println("DEPT_CODE" + "\t" + "COURSE#" + "\t" + "\t" + "TITLE" );
                                System.out.println("---------" + "\t" + "-------" + "\t" + "\t" + "-----" );
                                while (rs.next())
                                {
                                        flag=1; //indicates data found
                                        System.out.println(rs.getString(1) + "\t" + "\t" + rs.getInt(2)+ "\t" + "\t" + rs.getString(3));
                                }
                        }
	        	// print the results for Show_logs
	        	else if(Table_num == 6){ 	
				System.out.println("LOGID" + "\t" + "WHO" + "\t" + "\t" + "TIME" + "\t" + "\t" + "TABLE_NAME" +"\t" + "OPERATION" + "\t" + "KEY_VALUE" );
	        		System.out.println("-----" + "\t" + "---" + "\t" + "\t" + "----" + "\t" + "\t" + "----------" + "\t" + "---------" + "\t" + "---------");
	        		while (rs.next()){
	        			flag=1; //indicates data found
  System.out.println(rs.getInt(1) + "\t" + rs.getString(2) +"\t" + rs.getTime(3)+ "\t" + rs.getString(4) + "\t"+ rs.getString(5)+ "\t" + "\t" + rs.getString(6));
	  
	        		}
			}    
		

	        
			//Indicates that no data was found
			if (flag== 0) 
				System.out.println ("No data found");
		
	        	//close the result set, statement, and the connection
	        	rs.close();
	        	cs.close();
	        	conn.close();
	        
	        	 System.out.println("\n\n");

	        }
catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
  }

///////**********MAIN FUNCTION**********************///////////

public static void main(String[] args)throws SQLException {
		try
	        {
			
				//Declarations
				char user_ip, ctrl_ip, yn_ip;
				
				//Initalizations
				yn_ip= ' ';
				user_ip = 1;
				ctrl_ip=1;
				System.out.println("+---------------------------------------------------------------------------------------------+");
                                System.out.println("|                               STUDENT REGISTRATION SYSTEM                                   |");
                                System.out.println("|                                                                                             |");
                                System.out.println("+---------------------------------------------------------------------------------------------+");
				System.out.println(" ");
				
                                String password = null;
				try{
  //Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
        Connection conn = ds.getConnection("vbobhat1", "BingData");

if(conn!=null)
					{
						System.out.println("Welcome to the Student Registration System application.");
						System.out.println(" ");
						conn.close();
					}
				}
				catch (SQLException ex) { 
					System.out.println ("SQL Exception caught!\n");
					System.out.println ("Wrong Username and/or  Password....Quiting\n");
					System.exit(1);
				}
				do{
                                        System.out.println("MAIN MENU                                  ");
					System.out.println("\t\t\t\t\t\t\t\t-------------------------------------------");
					System.out.println("\t\t\t\t\t\t\t\t0 - EXIT");
					System.out.println("\t\t\t\t\t\t\t\t1 - VIEW DATA OPTION");
                                        System.out.println("\t\t\t\t\t\t\t\t2 - EDIT DATA OPTION");
					System.out.println("\t\t\t\t\t\t\t\t-------------------------------------------");
					System.out.println(" ");
					System.out.println("\t\tEnter your option -- ");
                                        System.out.print(" ");
					
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in ) );  
					user_ip = (char) br.read();
					
					switch (user_ip)
					{
						// EXIT
						case '0': 
							System.out.println("Thanks for using Student Registration System. Goodbye!!!");					
							break;
						
	// VIEW DATA CASE
						case '1':
							do{
								
								System.out.println("MAIN------>VIEW DATA OPTION                  ");
                                        			System.out.println("\t\t\t\t\t\t\t\t-------------------------------------------");
								System.out.println("\t\t\t\t\t\t\t\t0  - RETURN TO MAIN MENU");
								System.out.println("\t\t\t\t\t\t\t\t1  - DISPLAY STUDENTS DATA");
								System.out.println("\t\t\t\t\t\t\t\t2  - DISPLAY CLASSES DATA");
								System.out.println("\t\t\t\t\t\t\t\t3  - DISPLAY PREREQUISITES DATA");
								System.out.println("\t\t\t\t\t\t\t\t4  - DISPLAY ENROLLMENTS DATA");
								System.out.println("\t\t\t\t\t\t\t\t5  - DISPLAY COURSES DATA");
								System.out.println("\t\t\t\t\t\t\t\t6  - DISPLAY LOGS DATA");
								System.out.println("\t\t\t\t\t\t\t\t7  - DISPLAY PREREQUISITES OF A COURSE");
                                                                System.out.println("\t\t\t\t\t\t\t\t8  - DISPLAY COURSES TAKEN BY A STUDENT");
                                                                
								System.out.println("\t\t\t\t\t\t\t\t-------------------------------------------");
								System.out.println(" ");
                                        			System.out.println("\t\tEnter your option -- ");
                                        			System.out.print(" ");
								BufferedReader br_ctrl_ip = new BufferedReader(new InputStreamReader(System.in ) );  
								ctrl_ip = (char) br_ctrl_ip.read();
								
								
								switch (ctrl_ip)
								{
									//RETURN TO MAIN MENU
									case '0':
										break;
									
									//DISPLAY STUDENTS DATA
									case '1':
										Display(1);
										break;
									
									//DISPLAY CLASSES DATA
									case '2':
										Display(2);
										break;
								
									//DISPLAY PREREQUISITES DATA
                                                                	case '3':
                                                                	        Display(3);
                                                                	        break;

							    		//DISPLAY ENROLLMENTS DATA
									case '4':
										Display(4);
										break;

									//DISPLAY COURSES DATA
                                                                        case '5':
                                                                                Display(5);
                                                                                break;

									//DISPLAY LOG DATA
									case '6':
										Display(6);
										break;

									//DISPLAY PREREQUISITE OF A COURSE
									case '7':
                                                                                //Function call for this;
										Display(7);
                                                                                break;
									
									//DISPLAY COURSES TAKEN BY A STUDENT
									case '8':
										Display(8);
                                                                                //Function call for this;
                                                                                break;															
									default:
										System.out.println("Invalid option...., Please enter a valid option.....");
									
								}
								
							}while(ctrl_ip!='0');
							
							break;
						
			 			/* EDIT DATA OPTION */
						case '2':
							do{
								System.out.println("MAIN------>EDIT DATA OPTION                  ");
                                                        	System.out.println("\t\t\t\t\t\t\t\t-------------------------------------------");
								System.out.println("\t\t\t\t\t\t\t\t0 - RETURN TO MAIN MENU");
                                                        	System.out.println("\t\t\t\t\t\t\t\t1 - ADD STUDENT OPTION");
                                                        	System.out.println("\t\t\t\t\t\t\t\t2 - ENROLL STUDENT OPTION");
                                                        	System.out.println("\t\t\t\t\t\t\t\t3 - DE-ENROLL STUDENT OPTION");
                                                       	 	System.out.println("\t\t\t\t\t\t\t\t4 - DELETE STUDENT OPTION");
                                                        	System.out.println("\t\t\t\t\t\t\t\t-------------------------------------------");
                                                        	System.out.println(" ");
                                                        	System.out.println("\t\tEnter your option -- ");
                                                        	System.out.print(" ");

                                                        	BufferedReader br_ctrl_ip = new BufferedReader(new InputStreamReader(System.in ) );
                                                        	ctrl_ip = (char) br_ctrl_ip.read();
	
								switch (ctrl_ip)
                                                                {
									//RETURN TO MAIN MENU
                                                                        case '0':
                                                                                break;

//ADD STUDENT OPTION
                                                                        case '1':
										Add_students();
                                                                                //calling the function to add students
                                                                                break;

                                                                        //ENROLL STUDENT OPTION
                                                                        case '2':
										enroll();
                                                                                ///calling the function to enroll students
                                                                                break;

                                                                        //DE-ENROLL STUDENT OPTION
                                                                        case '3':
										drop();
                                                                                //calling the function to de-enroll students 
                                                                                break;

                                                                        //DELETE STUDENT OPTION
                                                                        case '4':
										delete();
                                                                                //calling the function to delete students
                                                                                break;

                                                                        default:
                                                                                System.out.println("Invalid option, Please enter a valid option");

                                                                }

                                                        }while(ctrl_ip!='0');

                                                        break;	
								
						default:
							System.out.println("Invalid option, please enter a valid option");
					}
					
				}while (user_ip!='0');
				
	        }
	        catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
  }
} 
