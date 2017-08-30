import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class author {
	private static final String URL = "jdbc:mysql://localhost:3306/Dai";     
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";     
	private static final String USER_NAME = "root";     
	private static final String PASSWORD = "zjj940815";
	public static void main(String[] args) {
		Connection conn=null;
		try {
			Class.forName(JDBC_DRIVER);
			conn=DriverManager.getConnection(URL,USER_NAME,PASSWORD);			
			//年份
			String[] year=new String[11];
			for(int i=2006;i<2017;i++)
			{
				year[i-2006]=String.valueOf(i);
			}
			auth_Analysis(conn,year); 
			//auth(conn,year);
			
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public static void Inst_Theme(Connection conn,String[] year)
	{
		try {
			String selectString="Select C1,ID,DE From articles2 where PY=?";
			PreparedStatement selectPS = conn.prepareStatement(selectString);		
			for(int i=9;i<11;i++)
			{		
				List<String> inst_so=new ArrayList<String>();
				selectPS.setString(1, year[i]);
				ResultSet rs = selectPS.executeQuery();
				while(rs.next()){	
					if(!rs.getString(1).equals(""))
					{
						List<String> Keywords=new ArrayList<String>();
						List<String> Inst=new ArrayList<String>();
						Inst.addAll(C1_Inst(rs.getString(1)));
						Keywords.addAll(splitwords(rs.getString(2)));
						Keywords.addAll(splitwords(rs.getString(3)));
						for(int j =0;j<Inst.size();j++)
						{
							for(int k=0;k<Keywords.size();k++)
							{
								inst_so.add(Inst.get(j)+"\t"+Keywords.get(k));
							}							
						}
					}
				}
				String file=System.getProperty("user.dir") + "\\data\\Inst_ID"+year[i]+".txt";
				count(inst_so,file);
				rs.close();					
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void theme_SO(Connection conn,String[] year)
	{
		try {
			String selectString="Select DE,SO From articles2 where PY=?";
			PreparedStatement selectPS = conn.prepareStatement(selectString);		
			for(int i=0;i<11;i++)
			{		
				List<String> inst_so=new ArrayList<String>();
				selectPS.setString(1, year[i]);
				ResultSet rs = selectPS.executeQuery();
				while(rs.next()){	
					if(!rs.getString(1).equals(""))
					{
						List<String> Keywords=new ArrayList<String>();
						Keywords.addAll(splitwords(rs.getString(1)));
						
						String SO=rs.getString(2).trim();
						SO=SO.replace("::", " ");
						for(int j =0;j<Keywords.size();j++)
						{
							//System.out.println(Inst.get(j)+"\t"+SO);
							inst_so.add(Keywords.get(j)+"\t"+SO);
						}
						//System.out.println(rs.getString(3));
					}
				}
				String file=System.getProperty("user.dir") + "\\data\\DE_SO"+year[i]+".txt";
				count(inst_so,file);
				rs.close();					
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static List<String> splitwords(String keywords)
	{
		List<String> words= new ArrayList<String>();
		keywords=keywords.replaceAll("::", " ");
		if(keywords.indexOf(";")>-1)
		{
			while(keywords.indexOf(";")>-1)
			{
				words.add(keywords.substring(0, keywords.indexOf(";")).trim().toUpperCase());
				keywords=keywords.substring(keywords.indexOf(";")+1);
			}
		}
		words.add(keywords.trim().toUpperCase());
		return words;
	}
	public static void Inst_SO(Connection conn,String[] year)
	{	
		try {
			String selectString="Select C1,SO From articles2 where PY=?";
			PreparedStatement selectPS = conn.prepareStatement(selectString);		
			for(int i=0;i<11;i++)
			{		
				List<String> inst_so=new ArrayList<String>();
				selectPS.setString(1, year[i]);
				ResultSet rs = selectPS.executeQuery();
				while(rs.next()){	
					if(!rs.getString(1).equals(""))
					{
						List<String> Inst=new ArrayList<String>();
						Inst.addAll(C1_Inst(rs.getString(1)));
						String SO=rs.getString(2).trim();
						SO=SO.replace("::", " ");
						for(int j =0;j<Inst.size();j++)
						{
							//System.out.println(Inst.get(j)+"\t"+SO);
							inst_so.add(Inst.get(j)+"\t"+SO);
						}
						//System.out.println(rs.getString(3));
					}
				}
				String file=System.getProperty("user.dir") + "\\data\\Inst_SO"+year[i]+".txt";
				count(inst_so,file);
				rs.close();			
				
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Set<String> C1_Inst(String str)
	{
		Set<String> Inst =new HashSet<String>();
		while(str.indexOf("::")>=0)
		{
			String temp=str.substring(0,str.indexOf("::"));
			if(temp.startsWith("["))
			{
				temp=temp.substring(temp.indexOf("]")+1).trim();
			}
			String inst=temp.substring(0, temp.indexOf(",")).trim();
			Inst.add(inst);
			str=str.substring(str.indexOf("::")+2);
		}
		if(str.startsWith("["))
		{
			str=str.substring(str.indexOf("]")+1).trim();
		}
		String inst=str.substring(0, str.indexOf(",")).trim();
		Inst.add(inst);		
		return Inst;
	}
	public static void auth(Connection conn,String[] year)
	{
		try {
			String selectString="Select ID,AF From author1 where PY=?";
			PreparedStatement selectPS = conn.prepareStatement(selectString);
			String updateString="update author1 set AF=? where ID=?";
			PreparedStatement update=conn.prepareStatement(updateString);
			for(int i=0;i<11;i++)
			{
				selectPS.setString(1, year[i]);
				ResultSet rs = selectPS.executeQuery();
				while(rs.next()){
					String ID=rs.getString(1);
					String name=rs.getString(2).trim();
					if(name.indexOf("-")>=0)
					{
						name=name.replace("-", "");
					}
					if(name.lastIndexOf(" ")>0&&!name.endsWith(".")&&name.lastIndexOf(" ")!=name.indexOf(" "))
					{
						name=name.substring(0, name.lastIndexOf(" "))+name.substring(name.lastIndexOf(" ")+1);
					}
					update.setString(1, name);
					update.setString(2, ID);
					System.out.println(ID+"\t"+name);
					update.execute();				
					}
				rs.close();
				
				//break;
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void auth_Analysis(Connection conn,String[] year)
	{
		
		try {
			String selectString="Select AF,inst From author where PY=?";
			PreparedStatement selectPS = conn.prepareStatement(selectString);
			
				
			//List<String> TC=new ArrayList<String>();
			List<String> result=new ArrayList<String>();
				for(int i =0;i<11;i++)
				{
					
					selectPS.setString(1, year[i]);
					ResultSet rs = selectPS.executeQuery();
					while(rs.next()){
						String temp=rs.getString(1)+"\t"+rs.getString(2);
						result.add(temp);
						//TC.add(rs.getString(3));
					}
					rs.close();
					if(i==4||i==7||i==10)
					{
						String file=System.getProperty("user.dir") + "\\data\\author"+year[i]+".txt";
						count(result,file);
						//TC.clear();
						result.clear();
					}
				}
				
				
				
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void count(List<String> content,String file)
	{		
		Set<String> auth_no =new HashSet<String>();
		List<String> result= new ArrayList<String>();
		auth_no.addAll(content);
		result.addAll(auth_no);
		int[] num=new int[auth_no.size()];
		for(int i=0;i<auth_no.size();i++)
		{
			num[i]=0;
		}
		for(int i=0;i<content.size();i++)
		{
			if(result.contains(content.get(i)))
			{
				num[result.indexOf(content.get(i))]++;
			}
		}
		String temp="";
		for(int i=0;i<result.size();i++)
		{
			//System.out.println(result.get(i)+"\t"+num[i]);
			temp=temp+result.get(i)+"\t"+num[i]+"\n";			
		}
		writetofile(file,temp);
		System.out.println(file);
	}
	public static void Count(List<String> content,List<String> TC,String file)
	{		
		Set<String> auth_no =new HashSet<String>();
		List<String> result= new ArrayList<String>();		
		auth_no.addAll(content);
		result.addAll(auth_no);
		Integer[] Total=new Integer[result.size()];
		for(int i =0;i<result.size();i++)
		{		
			Total[i]=0;
		}
		for(int i =0;i<content.size();i++)
		{		
			int location=result.indexOf(content.get(i));
			Total[location]=Total[location]+Integer.valueOf(TC.get(i));
		}
		String temp="";
		for(int i=0;i<result.size();i++)
		{
			//System.out.println(result.get(i)+"\t"+num[i]);
			temp=temp+result.get(i)+"\t"+Total[i]+"\n";			
		}
		writetofile(file,temp);
		System.out.println(file);
		/*
		int[] num=new int[auth_no.size()];
		for(int i=0;i<auth_no.size();i++)
		{
			num[i]=0;
		}
		for(int i=0;i<content.size();i++)
		{
			if(result.contains(content.get(i)))
			{
				num[result.indexOf(content.get(i))]++;
			}
		}
		String temp="";
		for(int i=0;i<result.size();i++)
		{
			//System.out.println(result.get(i)+"\t"+num[i]);
			temp=temp+result.get(i)+"\t"+num[i]+"\n";			
		}
		writetofile(file,temp);
		System.out.println(file);
		*/
	}
	public static void writetofile(String filename,String content)
	{
		FileWriter writer = null;  
        try {     
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(filename, true);     
            writer.write(content+"\n");       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
	}
}