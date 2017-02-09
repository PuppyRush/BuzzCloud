package cn.bluejoe.elfinder.controller.executor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("commandExecutorFactory")
public class DefaultCommandExecutorFactory implements CommandExecutorFactory
{
	final String _classNamePattern = "cn.bluejoe.elfinder.controller.executors.%sCommandExecutor";

	private Map<String, CommandExecutor> _map = new HashMap<String, CommandExecutor>();

	private CommandExecutor _fallbackCommand;

	@Override
	public CommandExecutor get(String commandName)
	{
		if (_map.containsKey(commandName))
			return _map.get(commandName);

		try
		{
			String className = String.format(_classNamePattern, commandName
					.substring(0, 1).toUpperCase() + commandName.substring(1));
			return (CommandExecutor) Class.forName(className).newInstance();
		}
		catch (Exception e)
		{
			// not found
			return _fallbackCommand;
		}
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
