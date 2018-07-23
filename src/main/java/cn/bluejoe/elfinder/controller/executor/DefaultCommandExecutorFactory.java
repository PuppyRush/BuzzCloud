package cn.bluejoe.elfinder.controller.executor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.bluejoe.elfinder.controller.executors.GetCommandExecutor;
import cn.bluejoe.elfinder.controller.executors.MkdirCommandExecutor;
import cn.bluejoe.elfinder.controller.executors.OpenCommandExecutor;
import cn.bluejoe.elfinder.impl.FsSecurityCheckForAll;
import cn.bluejoe.elfinder.service.FsService;
import cn.bluejoe.elfinder.service.FsServiceFactory;

@Service("commandExecutorFactory")
public class DefaultCommandExecutorFactory implements CommandExecutorFactory
{
	final String _classNamePattern = "cn.bluejoe.elfinder.controller.executors.%sCommandExecutor";

	private Map<String, CommandExecutor> _map = new HashMap<String, CommandExecutor>();

	private CommandExecutor _fallbackCommand;

	@Override
	public CommandExecutor get(String commandName, FsServiceFactory service)
	{
		CommandExecutor exe = null;
		if (_map.containsKey(commandName))
			exe = _map.get(commandName);
		else{
			try
			{
				String className = String.format(_classNamePattern, commandName
						.substring(0, 1).toUpperCase() + commandName.substring(1));
				exe = (CommandExecutor) Class.forName(className).newInstance();
				_map.put(commandName, exe);
			}
			catch (Exception e)
			{
				// not found
				return _fallbackCommand;
			}
		}
		
		if(exe == null)
			return _fallbackCommand;
		else{
			
			if(isValidatedCommand(exe,service.getFileService()))
				return exe;
			else
				return _fallbackCommand;
		}
	}

	private boolean isValidatedCommand(CommandExecutor command, FsService checker ) {
		
		/*if(command instanceof MkdirCommandExecutor){
			checker.getSecurityChecker().
			if(checker.getSecurityChecker().isCreatable())
			return true;
		}*/
		
		return true;
	}
	
	public String getClassNamePattern()
	{
		return _classNamePattern;
	}

	public Map<String, CommandExecutor> getMap()
	{
		return _map;
	}

	public CommandExecutor getFallbackCommand()
	{
		return _fallbackCommand;
	}

	public void setMap(Map<String, CommandExecutor> map)
	{
		_map = map;
	}

	public void setFallbackCommand(CommandExecutor fallbackCommand)
	{
		this._fallbackCommand = fallbackCommand;
	}
}
