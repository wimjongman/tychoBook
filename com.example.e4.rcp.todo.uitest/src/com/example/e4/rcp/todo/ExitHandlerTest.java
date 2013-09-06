package com.example.e4.rcp.todo;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class ExitHandlerTest {

	private static SWTBot bot;

	@BeforeClass
	public static void beforeClass() {
		bot = new SWTBot();
	}

	@Test
	public void executeExit() {
		SWTBotMenu fileMenu = bot.menu("File");
		Assert.assertNotNull("No 'File' menu found", fileMenu);

		SWTBotMenu exitMenu = fileMenu.menu("Exit");
		Assert.assertNotNull("No 'Exit' menu entry found", exitMenu);
		
		exitMenu.click();
	}
}
