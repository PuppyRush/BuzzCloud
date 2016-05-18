package property;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class controller
 */
@WebServlet(
		urlPatterns = { 
				"/",
				"*.do"
		}, 
		initParams = { 
				@WebInitParam(name = "propertyConfig", value = "commandMapping.properties")
		})
public class controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
		private Map<String, Object> commandMap = new HashMap<String, Object>();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
	public controller() {
	    super();
	    // TODO Auto-generated constructor stub
		}

    //명령어와 처리클래스가 매핑되어 있는 properties 파일을 읽어서 
    //HashMap객체인 commandMap에 저장

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
		//initParams에서 propertyConfig의 값을 읽어옴
		String props = config.getInitParameter("propertyConfig");
		String realFolder = "/property"; //properties파일이 저장된 폴더
		//웹어플리케이션 루트 경로
		ServletContext context = config.getServletContext();
		//realFolder를 웹어플리케이션 시스템상의 절대경로로 변경
		String realPath = context.getRealPath(realFolder) +"/"+props;
							    
		//명령어와 처리클래스의 매핑정보를 저장할 Properties객체 생성
		Properties pr = new Properties();
		FileInputStream f = null;
		try{
			//command.properties파일의 내용을 읽어옴
			f = new FileInputStream(realPath); 
			//command.properties의 내용을 Properties객체 에 저장
			pr.load(f);
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (f != null) try { f.close(); } catch(IOException ex) {}
		}
		//Set객체의 iterator()메소드를 사용해 Iterator객체를 얻어냄
		Iterator<?> keyIter = pr.keySet().iterator();
		//Iterator객체에 저장된 명령어와 처리클래스를 commandMap에 저장
		while( keyIter.hasNext() ) {
			String command = (String)keyIter.next();
			String className = pr.getProperty(command);
			try{
				Class<?> commandClass = Class.forName(className);
				Object commandInstance = commandClass.newInstance();
				commandMap.put(command, commandInstance);
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}catch (InstantiationException e) {
				e.printStackTrace();
			}catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		requestPro(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		requestPro(request, response);
	}
	
	private void requestPro(
			HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

			HashMap<String, String>preSplit = null;
			String view = null;
			commandAction com=null;
			try {
				String command = request.getRequestURI();
		        if(command.indexOf(request.getContextPath()) == 0) 
		           command = command.substring(request.getContextPath().length());
		        
		        com = (commandAction)commandMap.get(command);  
		        preSplit = com.requestPro(request, response);
		        	view = preSplit.get("view");
		        	if(preSplit.size() > 1){
		        		preSplit.remove("view");
		        	
	        			Set<Entry<String, String>> set = preSplit.entrySet();
        				Iterator<Entry<String, String>> it = set.iterator();
	        			while(it.hasNext()){
	        				Map.Entry<String, String> e = (Map.Entry<String, String>)it.next();
        					request.setAttribute(e.getKey(), e.getValue());
	        				}
		        		}
			}catch(Throwable e) {
				e.printStackTrace();
			}

		    RequestDispatcher dispatcher = 
		       request.getRequestDispatcher(view);
		    dispatcher.forward(request, response);
		}
	
}
