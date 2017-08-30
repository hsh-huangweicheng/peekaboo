import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DaiHX {
	private static final String URL = "jdbc:mysql://localhost:3306/Dai";     
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";     
	private static final String USER_NAME = "root";     
	private static final String PASSWORD = "zjj940815";
	public static void main(String[] args) {
		Connection conn=null;
		try {
			Class.forName(JDBC_DRIVER);
			conn=DriverManager.getConnection(URL,USER_NAME,PASSWORD);
			//AU(conn);
			//年份
			String[] year=new String[11];
			for(int i=2006;i<2017;i++)
			{
				year[i-2006]=String.valueOf(i);
			}
			Inst_TC(year,conn);
			
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	public static void Country(String[] year,Connection conn)
	{		
		try {
			String selectString="Select C1 From articles2 where C1 like '[%' ";
			PreparedStatement selectPS = conn.prepareStatement(selectString);
			List<String> country=new ArrayList<String>();			
				
				ResultSet rs = selectPS.executeQuery();
				while(rs.next()){
					String temp=rs.getString(1);
					Set<String> countries=countries(temp);
					if(countries.size()>1)
					{
						List<String> cooperation=new ArrayList<String>();
						cooperation.addAll(countries);
						for(int i =0;i<cooperation.size();i++)
						{
							for(int j=0;j<cooperation.size();j++)
							{
								country.add(cooperation.get(i)+"\t"+cooperation.get(j));
							}
						}
					}
					else
					{
						continue;
					}				
				}
				
				rs.close();
				
				String file=System.getProperty("user.dir") + "\\data\\农业大学cooperation.txt";
				Count(country,file);
				
					
				
				//break;
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Set<String> countries(String C1)
	{
		Set<String> countries=new HashSet<String>();
		while(C1.indexOf("::")>=0)
		{
			String temp=C1.substring(0,C1.indexOf("::"));
			String country=temp.substring(temp.lastIndexOf(", ")+2);
			if(country.indexOf("USA")>=0)
			{
				country="USA";
			}
			countries.add(country);
			C1=C1.substring(C1.indexOf("::")+1);
		}
		String country=C1.substring(C1.lastIndexOf(", ")+2);
		if(country.indexOf("USA")>=0)
		{
			country="USA";
		}
		countries.add(country);
		return countries;
	}
	public static void Inst_TC(String[] year,Connection conn)
	{		
		try {
			String selectString="Select C1,TC From articles2 where PY=? and C1 like '[%' ";
			PreparedStatement selectPS = conn.prepareStatement(selectString);
			List<String> Inst=new ArrayList<String>();
			List<String> TC=new ArrayList<String>();
			for(int i=0;i<11;i++)
			{
				selectPS.setString(1, year[i]);
				ResultSet rs = selectPS.executeQuery();
				while(rs.next()){
					String temp=rs.getString(1);			
					Inst.addAll(Inst(temp));
					for(int j=0;j<Inst(temp).size();j++)
					{TC.add(rs.getString(2));}
				}
				rs.close();				
				if(i==4||i==7||i==10)
				{
					String file=System.getProperty("user.dir") + "\\data\\Inst_TC"+year[i]+".txt";
					Count(Inst,TC,file);
					Inst.clear();
					TC.clear();
					
				}
				//break;
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void SO(String[] year,Connection conn)
	{		
		try {
			String selectString="Select C1,TC From articles1 where PY=?";
			PreparedStatement selectPS = conn.prepareStatement(selectString);
			List<String> journal=new ArrayList<String>();
			List<String> TC=new ArrayList<String>();
			for(int i=0;i<11;i++)
			{
				selectPS.setString(1, year[i]);
				ResultSet rs = selectPS.executeQuery();
				while(rs.next()){
					String temp=rs.getString(1);
					temp=temp.replace("::", " ");
					journal.add(temp);
					TC.add(rs.getString(2));
				}
				rs.close();
				if(i==4||i==7||i==10)
				{
					String file=System.getProperty("user.dir") + "\\data\\SO"+year[i]+"被引.txt";
					Count(journal,TC,file);
					journal.clear();
					TC.clear();
					
				}
				//break;
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	public static void ID(String[] year,Connection conn)
	{	
		
		try {
			String selectString="Select ID,DE From articles1 where DE is not null and ID is not null";
			PreparedStatement selectPS = conn.prepareStatement(selectString);
			
				List<String> Keywords=new ArrayList<String>();
				List<String> matrix= new ArrayList<String>();
				
				ResultSet rs = selectPS.executeQuery();
				while(rs.next()){
					if(!rs.getString(1).equals(""))
					{
						Keywords.addAll(splitwords(rs.getString(1)));						
					}
					
					if(!rs.getString(2).equals(""))
					{
						Keywords.addAll(splitwords(rs.getString(2)));
					}
					
					if(!rs.getString(1).equals("")&&!rs.getString(2).equals(""))//
					{
						matrix.addAll(occurrence(rs.getString(1)+";"+rs.getString(2)));//)
					}
				}
				rs.close();
				//统计词频
				String file=System.getProperty("user.dir") + "\\data\\ID+DE.txt";
				List<String> matrix_1=Count(Keywords,file);
				//统计矩阵
				file=System.getProperty("user.dir") + "\\data\\ID+DE._matrix.txt";
				count(matrix,file);
				
				//构建矩阵
				if(matrix_1.size()<20000)
				{
					Matrix(matrix_1,matrix,file+".matrix.txt");
				}		
				
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void AU(Connection conn)
	{
		List<String[]> author =new ArrayList<String[]>();
		try {
			String insertString = "insert into author1 values(?,?,?,?,?)";
			PreparedStatement insertPS = conn.prepareStatement(insertString);
			String selectString="Select C1,PY,TC From articles2 where C1 like '[%'";
			PreparedStatement selectPS = conn.prepareStatement(selectString);
			ResultSet rs = selectPS.executeQuery();
			while(rs.next()){
				String year=rs.getString(2);
				String temp=rs.getString(1);
				String TC=rs.getString(3);
				//String WOS=rs.getString(3);
				//System.out.println(WOS);
				author.addAll(C1(temp,year,TC));				
			}
			for(int i =0;i<author.size();i++)
			{
				//System.out.println(String.valueOf(i+1)+"\t"+author.get(i)[0]+"\t"+author.get(i)[1]+"\t"+author.get(i)[2]);
				insertPS.setString(1, String.valueOf(i+1));
				insertPS.setString(2, author.get(i)[0]);
				insertPS.setString(3, author.get(i)[1]);
				insertPS.setString(4, author.get(i)[2]);
				insertPS.setString(5, author.get(i)[3]);
				insertPS.executeUpdate();
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static List<String> Inst(String str)
	{
		List<String> author =new ArrayList<String>();
		while(str.indexOf("::[")>=0)
		{
			String temp=str.substring(0,str.indexOf("::["));
			String inst=temp.substring(temp.indexOf("]")+1).trim();
			inst=inst.substring(0, inst.indexOf(","));
			author.add(inst);
			str=str.substring(str.indexOf("::[")+3);
		}		
		String inst=str.substring(str.indexOf("]")+1).trim();
		inst=inst.substring(0, inst.indexOf(","));
		author.add(inst);	
		return author;
	}
	public static List<String[]> C1(String str,String year,String TC)
	{
		List<String[]> author =new ArrayList<String[]>();
		while(str.indexOf("::[")>=0)
		{
			String temp=str.substring(0,str.indexOf("::["));
			String authors=temp.substring(1, temp.indexOf("]")).trim();
			String inst=temp.substring(temp.indexOf("]")+1).trim();
			while(authors.indexOf(";")>=0)
			{
				String[] auth=new String[4];
				auth[0]=authors.substring(0, authors.indexOf(";")).toUpperCase();
				auth[1]=inst.substring(0, inst.indexOf(","));
				auth[2]=year;
				auth[3]=TC;
				author.add(auth);
				authors=authors.substring(authors.indexOf(";")+1);
			}
			String[] auth=new String[4];
			auth[0]=authors.toUpperCase();
			auth[1]=inst.substring(0, inst.indexOf(","));
			auth[2]=year;
			auth[3]=TC;
			author.add(auth);
			str=str.substring(str.indexOf("::[")+3);
		}	
		String authors=str.substring(1, str.indexOf("]")).trim();
		String inst=str.substring(str.indexOf("]")+1).trim();
		while(authors.indexOf(";")>=0)
		{
			String[] auth=new String[4];
			auth[0]=authors.substring(0, authors.indexOf(";")).toUpperCase();
			auth[1]=inst.substring(0, inst.indexOf(","));
			auth[2]=year;
			auth[3]=TC;
			author.add(auth);
			authors=authors.substring(authors.indexOf(";")+1);
		}
		String[] auth=new String[4];
		auth[0]=authors.toUpperCase();
		auth[1]=inst.substring(0, inst.indexOf(","));
		auth[2]=year;
		auth[3]=TC;
		author.add(auth);
		
		return author;
	}
	
	public static void count(List<String> content,String file)
	{
		
		Set<String> journal_NO =new HashSet<String>();
		List<String> journal= new ArrayList<String>();
		journal_NO.addAll(content);
		journal.addAll(journal_NO);
		int[] num=new int[journal_NO.size()];
		for(int i=0;i<journal_NO.size();i++)
		{
			num[i]=0;
		}
		for(int i=0;i<content.size();i++)
		{
			if(journal.contains(content.get(i)))
			{
				num[journal.indexOf(content.get(i))]++;
			}
		}
		String temp="";
		for(int i=0;i<journal.size();i++)
		{
			//System.out.println(journal.get(i)+"\t"+num[i]);
			temp=temp+journal.get(i)+"\t"+num[i]+"\n";			
		}
		writetofile(file,temp);
		System.out.println(file+"\t"+"done!");
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
	public static List<String> Count(List<String> content,String file)
	{
		List<String> matrix=new ArrayList<String>();
		Set<String> journal_NO =new HashSet<String>();
		List<String> journal= new ArrayList<String>();
		journal_NO.addAll(content);
		journal.addAll(journal_NO);
		int[] num=new int[journal_NO.size()];
		for(int i=0;i<journal_NO.size();i++)
		{
			num[i]=0;
		}
		for(int i=0;i<content.size();i++)
		{
			if(journal.contains(content.get(i)))
			{
				num[journal.indexOf(content.get(i))]++;
			}
		}
		String temp="";
		for(int i=0;i<journal.size();i++)
		{
			//System.out.println(journal.get(i)+"\t"+num[i]);
			if(num[i]>1)
			{
				matrix.add(journal.get(i));
			}
			temp=temp+journal.get(i)+"\t"+num[i]+"\n";			
		}
		writetofile(file,temp);
		System.out.println(file+"\t"+"done!");
		return matrix;
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
	//根据每一条keywords形成一对一的词对
	public static List<String> occurrence(String keywords)
	{
		List<String> words= new ArrayList<String>();
		List<String> matrix= new ArrayList<String>();
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
		for(int i =1;i<words.size();i++)
		{
			for(int j =i;j<words.size();j++)
			{
				matrix.add(words.get(i)+"\t"+words.get(j));
			}
		}						
		return matrix;
	}
	public static void Matrix(List<String> Keywords,List<String> Matrix,String file)
	{
		int[][] count=new int[Keywords.size()][Keywords.size()];
		for(int i =0;i<Keywords.size();i++)
		{		
			for(int j=0;j<Keywords.size();j++)
			{
				count[i][j]=0;
			}			
		}
		for(int i =0;i<Matrix.size();i++)
		{
			String line=Matrix.get(i).substring(0, Matrix.get(i).indexOf("\t"));
			String row=Matrix.get(i).substring(Matrix.get(i).indexOf("\t")+1);
			//System.out.println(Keywords.indexOf(line)+"\t"+Keywords.indexOf(row));
			if(Keywords.indexOf(line)>=0&&Keywords.indexOf(row)>=0)
			{
				count[Keywords.indexOf(line)][Keywords.indexOf(row)]++;
				count[Keywords.indexOf(row)][Keywords.indexOf(line)]++;
			}
		}

		for(int i =0;i<Keywords.size();i++)
		{
			String print="";
			for(int j=0;j<Keywords.size();j++)
			{
				print=print+"\t"+count[i][j];
			}
			writetofile(file,print);
		}
		System.out.println(file+"\t"+"done!");
	}
}
