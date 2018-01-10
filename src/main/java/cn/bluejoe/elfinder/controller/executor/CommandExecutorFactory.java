package cn.bluejoe.elfinder.controller.executor;

import cn.bluejoe.elfinder.service.FsServiceFactory;

public interface CommandExecutorFactory
{
	CommandExecutor get(String commandName,FsServiceFactory service);
}