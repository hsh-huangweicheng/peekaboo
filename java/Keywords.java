package DE_ID;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Keywords {
	
	private static final String URL = "jdbc:mysql://localhost:3306/temp";     
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";     
	private static final String USER_NAME = "root";     
	private static final String PASSWORD = "zjj940815";
	private static Connection conn = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub				
		/*
		String filePath = System.getProperty("user.dir") + "\\data\\ut-ESI";
		List<String> filelist = new ArrayList<String>();		
		getFiles(filePath,filelist);		
		for(String filename: filelist)
		{
			List <String> IDList = new ArrayList<String>();
			readfile(filename,IDList);		
			String file=System.getProperty("user.dir") + "\\data\\"
					+filename.substring(filename.lastIndexOf("\\")+1)+".txt";
			DE(IDList,file);
		}
		*/
		String file=System.getProperty("user.dir") + "\\data\\DE_ALL.txt";
		DE_ALL(file);
		
	}
	public static void DE_ALL(String file)
	{
		try {
			Class.forName(JDBC_DRIVER);
			conn=DriverManager.getConnection(URL,USER_NAME,PASSWORD);
			List<String> AU_Keywords= new ArrayList<String>();
			List<String> matrix= new ArrayList<String>();
			String SelectString ="select DE,ID from articles";
			PreparedStatement SelectPS = conn.prepareStatement(SelectString);		
			ResultSet rs=SelectPS.executeQuery();
			while(rs.next()){
				if(!rs.getString(1).equals(""))
				{
					AU_Keywords.addAll(splitwords(rs.getString(1)));
					matrix.addAll(occurrence(rs.getString(1)));
				}
				
				if(!rs.getString(2).equals(""))
				{
					AU_Keywords.addAll(splitwords(rs.getString(2)));
					matrix.addAll(occurrence(rs.getString(2)));
				}
				
			}
			rs.close();
			Set<String> Word =new HashSet<String>();
			Word.addAll(AU_Keywords);
			AU_Keywords.clear();
			AU_Keywords.addAll(Word);
			for(String a:Word)
			{
				writetofile(file+".words.txt",a);
			}
			
			count(matrix,file);
			Matrix(AU_Keywords,matrix,file+".Matrix.txt");
		}catch(Exception e){
			System.out.println(e.toString());
		}	
	}
	public static void DE(List <String> IDList,String file)
	{
		try {
			Class.forName(JDBC_DRIVER);
			conn=DriverManager.getConnection(URL,USER_NAME,PASSWORD);
			List<String> AU_Keywords= new ArrayList<String>();
			List<String> matrix= new ArrayList<String>();
			String SelectString ="select ID,DE from articles where UT=?";
			PreparedStatement SelectPS = conn.prepareStatement(SelectString);
			for(int i=0;i<IDList.size();i++)
			{
				SelectPS.setString(1, IDList.get(i));
				ResultSet rs=SelectPS.executeQuery();
				while(rs.next()){
					if(!rs.getString(1).equals(""))
					{
						AU_Keywords.addAll(splitwords(rs.getString(1)));
						matrix.addAll(occurrence(rs.getString(1)));
					}
					
					if(!rs.getString(2).equals(""))
					{
						AU_Keywords.addAll(splitwords(rs.getString(2)));
						matrix.addAll(occurrence(rs.getString(2)));
					}
					
				}
				rs.close();
				
			}
			Set<String> Word =new HashSet<String>();
			Word.addAll(AU_Keywords);
			AU_Keywords.clear();
			AU_Keywords.addAll(Word);
			for(String a:Word)
			{
				writetofile(file+".words.txt",a);
			}
			
			count(matrix,file);
			Matrix(AU_Keywords,matrix,file+".Matrix.txt");
		}catch(Exception e){
			System.out.println(e.toString());
		}	
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
			System.out.println(Keywords.indexOf(line)+"\t"+Keywords.indexOf(row));
			count[Keywords.indexOf(line)][Keywords.indexOf(row)]++;
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
	}
	public static void count(List<String> AU_Keywords,String file)
	{
		Set<String> Word =new HashSet<String>();
		List<String> Keywords= new ArrayList<String>();
		Word.addAll(AU_Keywords);
		Keywords.addAll(Word);
		int[] num=new int[Word.size()];
		for(int i=0;i<Word.size();i++)
		{
			num[i]=0;
		}
		for(int i=0;i<AU_Keywords.size();i++)
		{
			if(Keywords.contains(AU_Keywords.get(i)))
			{
				num[Keywords.indexOf(AU_Keywords.get(i))]++;
			}
		}
		for(int i=0;i<Word.size();i++)
		{
			//System.out.println(Keywords.get(i)+"\t"+num[i]);
			writetofile(file,Keywords.get(i)+"\t"+num[i]);
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
			for(int j =1;j<words.size();j++)
			{
				matrix.add(words.get(i)+"\t"+words.get(j));
			}
		}						
		return matrix;
	}
	public static void getFiles(String filePath,List<String> filelist)
	{
		File root = new File(filePath);
		File[] files = root.listFiles();
		for(File file:files)
		{     			
			if(file.isDirectory())
			{
				getFiles(file.getAbsolutePath(),filelist);
				//filelist.add(file.getAbsolutePath());
				//System.out.println("显示"+filePath+"下所有子目录及其文件"+file.getAbsolutePath());
		    }
			else
			{
				filelist.add(file.getAbsolutePath());
				//System.out.println("显示"+filePath+"下所有子目录"+file.getAbsolutePath());
		    }  		     
		}
	}
	public static void readfile(String filePath,List<String> IDList)
	{
		try {
			IDList.addAll(FileUtils.readLines(new File(filePath), "utf8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
