package cn.bluejoe.elfinder.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.puppyrush.buzzcloud.entity.ControllerException;
import com.puppyrush.buzzcloud.entity.band.BandController;
import com.puppyrush.buzzcloud.entity.member.MemberController;

import cn.bluejoe.elfinder.controller.executor.CommandExecutionContext;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.impl.DefaultFsMapping;
import cn.bluejoe.elfinder.impl.DefaultFsMapping.BandMember;
import cn.bluejoe.elfinder.service.FsServiceFactory;

@Controller("elfinderController")
@RequestMapping("/elfinder-servlet/connector")
public class ConnectorController
{
	

	@Autowired(required=false)
	private CommandExecutorFactory commandExecutorFactory;
	
	@Autowired(required=false)
	private DefaultFsMapping fsMapping;	
	
	@Autowired(required=false)
	private MemberController memberCtl;	
	

	
	
	@RequestMapping("/init")
	public void init(@RequestParam("bandId") int bandId, HttpServletRequest request){
		
		int memberId=-1;
		try {
			memberId = memberCtl.getMember(request.getRequestedSessionId()).getId();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BandMember bm = new BandMember(bandId,memberId);
		
		if(fsMapping.contains(bm)==false){
			Logger.getLogger(getClass()).info("init fsService of (" +bandId + "," + memberId+")");
			fsMapping.addFsServiceFactory(bm);
		}
		
	}
		
	@RequestMapping
	public void connector(@RequestParam("bandId") int bandId, HttpServletRequest request,	final HttpServletResponse response) throws IOException
	{
		
		int memberId=-1;
		try {
			memberId = memberCtl.getMember(request.getRequestedSessionId()).getId();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BandMember bm = new BandMember(bandId,memberId);
		
		try
		{
			if(fsMapping.contains(bm)==false){
				throw new IllegalArgumentException("fsMapping error");
			}
			
			
			request = parseMultipartContent(request);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}

		final FsServiceFactory service = fsMapping.getFsServiceFactory(bm);
		String cmd = request.getParameter("cmd");
		CommandExecutor ce = commandExecutorFactory.get(cmd,service);

		if (ce == null)
		{
			// This shouldn't happen as we should have a fallback command set.
			throw new FsException(String.format("unknown command: %s", cmd));
		}

		try
		{			
			
			
			final HttpServletRequest finalRequest = request;
			ce.execute(new CommandExecutionContext()
			{

				@Override
				public FsServiceFactory getFsServiceFactory()
				{
					return service;
				}

				@Override
				public HttpServletRequest getRequest()
				{
					return finalRequest;
				}

				@Override
				public HttpServletResponse getResponse()
				{
					return response;
				}

				@Override
				public ServletContext getServletContext()
				{
					return finalRequest.getSession().getServletContext();
				}
			});
		}
		catch (Exception e)
		{
			throw new FsException("unknown error", e);
		}
	}
	
	
	public CommandExecutorFactory getCommandExecutorFactory()
	{
		return commandExecutorFactory;
	}


	private HttpServletRequest parseMultipartContent(
			final HttpServletRequest request) throws Exception
	{
		if (!ServletFileUpload.isMultipartContent(request))
			return request;

		// non-file parameters
		final Map<String, String> requestParams = new HashMap<String, String>();

		// Parse the request
		ServletFileUpload sfu = new ServletFileUpload();
		String characterEncoding = request.getCharacterEncoding();
		if (characterEncoding == null)
		{
			characterEncoding = "UTF-8";
		}

		sfu.setHeaderEncoding(characterEncoding);
		FileItemIterator iter = sfu.getItemIterator(request);
		MultipleUploadItems uploads = new MultipleUploadItems();

		while (iter.hasNext())
		{
			FileItemStream item = iter.next();

			// not a file
			if (item.isFormField())
			{
				InputStream stream = item.openStream();
				requestParams.put(item.getFieldName(),
						Streams.asString(stream, characterEncoding));
				stream.close();
			}
			else
			{
				// it is a file!
				String fileName = item.getName();
				if (fileName != null && !"".equals(fileName.trim()))
				{
					uploads.addItemProxy(item);
				}
			}
		}

		uploads.writeInto(request);

		// 'getParameter()' method can not be called on original request object
		// after parsing
		// so we stored the request values and provide a delegate request object
		return (HttpServletRequest) Proxy.newProxyInstance(this.getClass()
				.getClassLoader(), new Class[] { HttpServletRequest.class },
				new InvocationHandler()
				{
					@Override
					public Object invoke(Object arg0, Method arg1, Object[] arg2)
							throws Throwable
					{
						// we replace getParameter() and getParameterValues()
						// methods
						if ("getParameter".equals(arg1.getName()))
						{
							String paramName = (String) arg2[0];
							return requestParams.get(paramName);
						}

						if ("getParameterValues".equals(arg1.getName()))
						{
							String paramName = (String) arg2[0];

							// normalize name 'key[]' to 'key'
							if (paramName.endsWith("[]"))
								paramName = paramName.substring(0,
										paramName.length() - 2);

							if (requestParams.containsKey(paramName))
								return new String[] { requestParams
										.get(paramName) };

							// if contains key[1], key[2]...
							int i = 0;
							List<String> paramValues = new ArrayList<String>();
							while (true)
							{
								String name2 = String.format("%s[%d]",
										paramName, i++);
								if (requestParams.containsKey(name2))
								{
									paramValues.add(requestParams.get(name2));
								}
								else
								{
									break;
								}
							}

							return paramValues.isEmpty() ? new String[0]
									: paramValues.toArray(new String[0]);
						}

						return arg1.invoke(request, arg2);
					}
				});
	}


}