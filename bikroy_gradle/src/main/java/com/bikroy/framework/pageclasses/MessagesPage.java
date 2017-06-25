package com.bikroy.framework.pageclasses;

import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.globals.GlobalEnumerations.TextComparators;
import com.bikroy.framework.globals.Logger;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.LocateBy;
import com.bikroy.framework.pageclasses.GlobalEnumsPage.PageSuccessMessages;
import com.bikroy.framework.utilities.textutilities.TextUtilities;

public class MessagesPage {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	@LocateBy(xpath = "//div[contains(@class,'icon')]/following::h1")
	private static ElementField OPERATION_MESSAGE;

	@LocateBy(xpath = "")
	private static ElementField ERROR_MESSAGE;

	public static String isOperationSuccessfulOnMessage(PageSuccessMessages messageToVerify, String additionalMessage,
			TextComparators comparator) throws Exception {
		return MessagesPage.isOperationSuccessfulOnMessage(messageToVerify.GetValue(), additionalMessage, comparator);
	}

	/**
	 * Compares in messages element for both messages to verify in one go with
	 * AND operation.
	 * 
	 * 
	 * @param messageToVerify
	 *            First message to verify
	 * @param additionalMessage
	 *            Second message to verify
	 * @param comparator
	 * @return NULL if result is true <br>
	 *         Message String if result is false
	 * @throws Exception
	 */
	public static String isOperationSuccessfulOnMessage(String messageToVerify, String additionalMessage, TextComparators comparator)
			throws Exception {
		String msg = MessagesPage.isOperationSuccessfulOnMessage(messageToVerify, comparator);
		String msg2 = MessagesPage.isOperationSuccessfulOnMessage(additionalMessage, comparator);

		if (msg == null) {
			if (msg2 == null) {
				return null;
			}
			return msg2;
		}

		return msg;
	}

	public static String isOperationSuccessfulOnMessage(PageSuccessMessages messageToVerify, TextComparators comparator) throws Exception {
		return MessagesPage.isOperationSuccessfulOnMessage(messageToVerify.GetValue(), comparator);
	}

	public static String isOperationSuccessfulOnMessage(String messageToVerify, TextComparators comparator) throws Exception {
		String msg = GlobalController.brw.getText(MessagesPage.OPERATION_MESSAGE);
		MessagesPage.logger.info(msg);

		msg = TextUtilities.nullToBlank(msg, true);
		boolean result = TextUtilities.compareValue(messageToVerify, msg, true, comparator);

		return (result ? null : msg);
	}

	public static boolean isErrorMessageAppeared() throws Exception {
		return GlobalController.brw.isElementPresent(MessagesPage.ERROR_MESSAGE);
	}

	public MessagesPage verifyDisplayedMessageText(String messageToVerify, String additionalMessage, TextComparators comparator)
			throws Exception {

		String rsltMsg = MessagesPage.isOperationSuccessfulOnMessage(messageToVerify, additionalMessage, TextComparators.contains);
		if (rsltMsg != null) {
			throw new Exception("Test Case failed: " + rsltMsg);
		}
		return GlobalController.brw.initElements(MessagesPage.class);

	}

}
