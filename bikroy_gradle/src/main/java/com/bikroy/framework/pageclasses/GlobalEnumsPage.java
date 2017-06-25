package com.bikroy.framework.pageclasses;

public class GlobalEnumsPage {

	public enum LoginField {
		ALL, USERNAME, PASSWORD, EMPTY;
	}

	public enum SignUpField {
		ALL, USERNAME, EMAILID, PASSWORD, CONFIRMPSWD, INVALIDCONFIRMPSWD, EMPTY;
	}

	public enum ProvideItemDetails {
		LOGGEDINUSER, NOLOGGEDINUSER;
	}

	public static enum PageSuccessMessages {
		OrderPeriodCreationSuccess("Order Period created successfully"),

		PaymentMethodCreationSuccess("Payment Method Type XXX created successfully");

		public static PageSuccessMessages GetKey(final String Value) {
			for (PageSuccessMessages tc : PageSuccessMessages.values()) {
				if (tc.GetValue().equals(Value)) {
					return tc;
				}
			}
			return null;
		}

		private final String indexLabel;

		private PageSuccessMessages(final String labelText) {
			this.indexLabel = labelText;
		}

		public String GetValue() {
			return this.indexLabel;
		}
	}
}
