package demo.util;

import java.util.*;

import demo.event.*;

public class OrderUtil {
	public static final String RABBIT_HOST = "141.144.29.245";
	public static final String RABBIT_USER = "guest";
	public static final String RABBIT_PASSWORD = "guest";
	
	
	public static final String EVENT_ORDER_SUBMIT = "Order Submitted";
	public static final String EVENT_DECIDE_IF_DOCS_NEEDED = "Decide If Docs Needed";
	public static final String EVENT_REQUEST_PAYMENT_AUTHORIZE = "Request Payment Authorization";
	public static final String EVENT_LIST_DOCS = "List Docs Needed";
	public static final String EVENT_REQUEST_REPORT = "Send Report Request";
	public static final String EVENT_NO_DOCS_NEEDED = "No Docs Needed";
	public static final String EVENT_RECEIVE_REPORT = "Report Delivered";
	public static final String EVENT_RECEIVE_PAYMENT_AUTH = "Payment Authorized";
	public static final String EVENT_SHIP_ORDER = "Ship Order";

	public static final String ACTION_REQUEST_PAYMENT_AUTHORIZE = "ACTION_REQUEST_PAYMENT_AUTHORIZE";
	public static final String ACTION_REQUEST_REPORT = "ACTION_REQUEST_REPORT";
	public static final String ACTION_RECEIVE_REPORT = "ACTION_RECEIVE_REPORT";
	public static final String ACTION_RECEIVE_PAYMENT_AUTH = "ACTION_RECEIVE_PAYMENT_AUTH";
	public static final String ACTION_SHIP_ORDER = "Ship Order";

	
	public static final String EVENT_SOURCE_SYSTEM = "SYSTEM";
	public static final String EVENT_SOURCE_CRM = "CRM";
	public static final String EVENT_SOURCE_MOBILE = "MOBILE";

	
	public static String EXCHANGE_ORDER_EVENTS = "event_exchange";
	public static String QUEUE_ORDER_EVENTS = "event_order";
	public static String ROUTING_KEY_ORDER_EVENTS = "100";
	public static String QUEUE_PAYMENT_EVENTS = "event_payment";
	public static String ROUTING_KEY_PAYMENT_EVENTS = "200";
	public static String QUEUE_ERROR_EVENTS = "event_error";
	public static String ROUTING_KEY_ERROR_EVENTS = "900";

	public static final String PARAM_REPORT_NAME = "Report_Name";
	public static final String PARAM_REPORT_METHOD = "Report_Method";
	public static final String PARAM_REPORT_MESSAGE = "Report_Message";
	public static final String PARAM_PAYMENT_APPROVED_FLAG = "Payment_Approved_Flag";
	public static final String PARAM_PAYMENT_APPROVED_MESSAGE = "Payment_Approved_Message";

	public static boolean isPaymentAuthorized(List<BaseEvent> pastEvents) throws Exception {
		boolean paymentAuthorized = false;
		for (BaseEvent event : pastEvents) {
			if (event.getEventCode().equals(EVENT_RECEIVE_PAYMENT_AUTH)) {
				paymentAuthorized = true;
				break;
			}
		}
		return paymentAuthorized;
	}

	public static boolean checkPendingReports(List<BaseEvent> pastEvents) throws Exception {
		boolean pendingReport = false;

		for (BaseEvent event : pastEvents) {
			if(event.getEventCode().equals(OrderUtil.EVENT_NO_DOCS_NEEDED)){
				return false;
			}
		}
		// Check if there is an EVENT_REQUEST_REPORT event for a report,
		// for which no EVENT_RECEIVE_REPORT event is present
		boolean thisReportIsReceived = true;
		String reportName = "";
		for (BaseEvent event : pastEvents) {
			thisReportIsReceived = true;
			reportName = "";
			if (event.getEventCode().equals(EVENT_REQUEST_REPORT)) {
				reportName = event.getParam(PARAM_REPORT_NAME).getValue().toString();
				if (!reportRceived(reportName, pastEvents)) {
					pendingReport = true;
					break;
				}
			}
		}
		return pendingReport;
	}

	public static boolean reportRceived(String requestedReportName, List<BaseEvent> pastEvents) throws Exception {
		boolean reportRceived = false;
		String receivedReportName = null;
		for (BaseEvent event : pastEvents) {
			if (event.getEventCode().equals(EVENT_RECEIVE_REPORT)) {
				receivedReportName = event.getParam(PARAM_REPORT_NAME).getValue().toString();
				if (receivedReportName.equals(requestedReportName)) {
					reportRceived = true;
					break;
				}
			}
		}
		return reportRceived;
	}

}
