package CRGNonProfit;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.Automation.Base.BaseTest;
import com.Automation.Util.Constants;
import com.Automation.Util.DataUtil;
import com.aventstack.extentreports.Status;

public class TC002_NPO_SF_LeadVerification extends BaseTest {

	@Test(dataProvider = "getData")
	public void testB(Hashtable<String, String> data) throws Exception {
		test.log(Status.INFO, "Starting: " + testName);

		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals(Constants.RUNMODE_NO)) {
			test.log(Status.SKIP, "Runmode is set to NO");
			throw new SkipException("Runmode is set to No");
		}
		System.out.println("--------------------------------------");
		System.out.println("Statring Test... " + this.getClass().getSimpleName()+" |");
		System.out.println("--------------------------------------");
		ds.executeKeywords(testName, xls, data);

	}
}
