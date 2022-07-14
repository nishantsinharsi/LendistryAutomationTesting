package DBTransaction;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//import org.json.simple.JSONObject;
import org.postgresql.util.PGobject;

public class DBTransaction {
	String urlPostGre;

	public DBTransaction(Properties envProp) {
		String url = "jdbc:postgresql://$host/PanaCIMMA?user=$MA_Database_userid&password=$MA_Database_password";
		String url1 = url.replace("$host", envProp.getProperty("host"));
		String url2 = url1.toString().replace("$MA_Database_userid", envProp.getProperty("MA_Database_userid"));
		String url3 = url2.toString().replace("$MA_Database_password", envProp.getProperty("MA_Database_password"));
		urlPostGre = url3;
	}

	public Connection getPostGreConnection() {
		// String urlPostGre =
		// "jdbc:postgresql://localhost/PanaCIMMA?user=postgres&password=Panasonic1";
		Connection pgConnection = null;
		try {
			pgConnection = DriverManager.getConnection(urlPostGre);
			System.out.println("PostGre SQL Connection Suceeded");
			return pgConnection;
		} catch (SQLException e) {
			System.out.println("PostGre SQL Connection did not suceed, the following exception was raised" + e);
		}
		return null;

	}

	public void DeleteWOFromSubWO(String workordername) throws SQLException {
		Connection pgConnection = getPostGreConnection();
		Statement pgStatement = null;

		pgStatement = pgConnection.createStatement();

		System.out.println("'DB transaction Initiated' + '"
				+ "for deleting workorder form table workorder with workorderName " + workordername + "'  ");
		// DELETE FROM ma.workorder w Where w.workorder_name='WOA_1'
		String Command = "delete from ma.subworkorder sw where sw.workorder_product_id in (select workorder_product_id FROM ma.workorder w inner join ma.workorder_product wp on w.workorder_id = wp.workorder_id where w.workorder_name = '"
				+ workordername + "')";
		try {
			pgStatement.executeUpdate(Command);
			System.out.println("Deleted WO from table WO Product successfully");
		} catch (Exception e) {
			pgConnection.rollback();
			System.out.println("Did not delete WO successfully");
		} finally {
			pgConnection.close();
		}
	}

	public void DeleteWOFromWorkOrder_Product(String workordername) throws SQLException {
		Connection pgConnection = getPostGreConnection();
		Statement pgStatement = null;

		pgStatement = pgConnection.createStatement();

		System.out.println("'DB transaction Initiated' + '"
				+ "for deleting workorder form table workorder with workorderName " + workordername + "'  ");
		// DELETE FROM ma.workorder w Where w.workorder_name='WOA_1'
		String Command = "delete from ma.workorder_product wp where wp.workorder_id in (select w.workorder_id FROM ma.workorder w where w.workorder_name = '"
				+ workordername + "')";
		try {
			pgStatement.executeUpdate(Command);

			System.out.println("Deleted WO from table WO Product successfully");
		} catch (SQLException e) {
			pgConnection.rollback();
			System.out.println("Did not delete WO successfully");
		} finally {
			pgConnection.close();
		}
	}

	public void DeleteWOFromWorkOrder(String workordername) throws SQLException {
		Connection pgConnection = getPostGreConnection();
		Statement pgStatement = null;

		pgStatement = pgConnection.createStatement();

		System.out.println("'DB transaction Initiated' + '"
				+ "for deleting workorder form table workorder with workorderName " + workordername + "'  ");
		// DELETE FROM ma.workorder w Where w.workorder_name='WOA_1'
		String Command = "DELETE FROM ma.workorder w where w.workorder_name = '" + workordername + "'";
		try {
			pgStatement.executeUpdate(Command);
			System.out.println("Deleted WO successfully");
		} catch (SQLException e) {
			pgConnection.rollback();
			System.out.println("Did not delete WO successfully");
		} finally {
			pgConnection.close();
		}
	}

	public void DeleteWO(String workOrderName) throws SQLException {
		DeleteWOFromSubWO(workOrderName);
		DeleteWOFromWorkOrder_Product(workOrderName);
		DeleteWOFromWorkOrder(workOrderName);
	}

	public void RunPgSqlTransaction(String woJson) throws SQLException {
		String userId = "";
		Connection pgConnection = getPostGreConnection();
		// Statement pgStatement=null;

		// pgStatement = pgConnection.createStatement();
		CallableStatement ma_usp_insert_workorder_Proc = pgConnection
				.prepareCall("{call ma.usp_insert_workorder( ?,? ) }");

		// var workOrderJson =
		// "{\"WorkOrderName\":\"WorkOrderTest\",\"WorkOrderId\":0,\"ProductName\":\"PRODUCT-E\",\"ProductInfoId\":\"5\",\"PatternPerPanel\":1,\"Side\":\"A\",\"PatternQuantity\":101,\"WorkOrderProductId\":0,\"SubWorkOrders\":[{\"SubWorkOrderId\":0,\"WoProductRelationId\":0,\"ProductSetupId\":7,\"Lane\":\"F\",\"TargetQuantity\":1,\"CompletedQuantity\":0,\"ScheduleStartTime\":\"2018-04-29T09:04:00Z\",\"ScheduleEndTime\":\"2018-04-29T12:04:00Z\",\"ActualStartTime\":\"2018-04-28T09:04:20.961Z\",\"ActualEndTime\":\"2018-04-28T09:04:20.961Z\",\"ReworkedQuantity\":0,\"re_subworkorder_id\":0,\"WorkOrderStatusId\":1,\"WorkOrderStatus\":\"Created\",\"CreatedDate\":\"0001-01-01T00:00:00\",\"CreatedBy\":0,\"UpdatedDate\":\"0001-01-01T00:00:00\",\"UpdatedBy\":0,\"Mjsid\":\"PRDE_RECIPE1\",\"LineName\":null,\"LineInfoId\":1,\"LotName\":\"PRODUCT-E_1_PRDE_RECIPE1\",\"Side\":\"Top\"}]}";
		// String woName = "winame";
		// String productName = "pname";
		// String prductInFold = "6";
		// String patternPerPanem = "10";
		// String side = "Top";
		// String productSetupId = "2";
		// String lane = "R";
		// String scheduledStart = "sfhgsjh";
		// String scheduledEnd = "shdgcfjsd";
		// String mjsid = "mjsid001";
		// String workorderstatus = "status";
		// String statusId = "4";
		// String lineInFold = "3";
		// String lotName = "topLot";
		// String subWOSide = "T";

		// String a = "{\"WorkOrderName\":\"" + woName +
		// "\",\"WorkOrderId\":0,\"ProductName\":\"" + productName +
		// "\",\"ProductInfoId\":\"" + prductInFold + "\",\"PatternPerPanel\":" +
		// patternPerPanem + ",\"Side\":\"" + side +
		// "\",\"PatternQuantity\":101,\"WorkOrderProductId\":0,\"SubWorkOrders\":[{\"SubWorkOrderId\":0,\"WoProductRelationId\":0,\"ProductSetupId\":"
		// + productSetupId + ",\"Lane\":\"" + lane +
		// "\",\"TargetQuantity\":1,\"CompletedQuantity\":0,\"ScheduleStartTime\":\"" +
		// scheduledStart + "\",\"ScheduleEndTime\":\"" + scheduledEnd +
		// "\",\"ActualStartTime\":\"2018-04-28T09:04:20.961Z\",\"ActualEndTime\":\"2018-04-28T09:04:20.961Z\",\"ReworkedQuantity\":0,\"re_subworkorder_id\":0,\"WorkOrderStatusId\":"
		// + statusId + ",\"WorkOrderStatus\":\"" + workorderstatus +
		// "\",\"CreatedDate\":\"0001-01-01T00:00:00\",\"CreatedBy\":0,\"UpdatedDate\":\"0001-01-01T00:00:00\",\"UpdatedBy\":0,\"Mjsid\":\""
		// + mjsid + "\",\"LineName\":null,\"LineInfoId\":" + lineInFold +
		// ",\"LotName\":\"" + lotName + "\",\"Side\":\"" + subWOSide + "\"}]}";

		try {
			// String Command = "SELECT * FROM ma.usp_insert_workorder('" + woJson + "', '"
			// + userId + "')";
			// JSONObject obj = new JSONObject();

			PGobject jsonObject = new PGobject();
			jsonObject.setType("json");
			jsonObject.setValue(woJson);

			ma_usp_insert_workorder_Proc.setObject(1, jsonObject);
			ma_usp_insert_workorder_Proc.setString(2, userId);
			// ma_usp_insert_workorder_Proc.registerOutParameter(1,Types.INTEGER);
			ma_usp_insert_workorder_Proc.executeUpdate();
			// Result = ma_usp_insert_workorder_Proc.getInt(1);

			ma_usp_insert_workorder_Proc.close();
			System.out.println("Created WO successfully");

		} catch (Exception e) {
			pgConnection.rollback();
			System.out.println("Did not create WO successfully");
		} finally {
			pgConnection.close();
		}
	}

	public void deleteWOImportTemplate(String templateName) throws SQLException {

		Connection pgConnection = getPostGreConnection();
		Statement pgStatement = null;

		pgStatement = pgConnection.createStatement();

		String delStatement = "delete  from ma.workorder_import_templates where workorder_import_template_name = '"
				+ templateName + "'";

		System.out.println(delStatement);

		try {
			pgStatement.executeUpdate(delStatement);
			System.out.println("Deleted Template successfully");

		} catch (Exception e) {
			pgConnection.rollback();
			System.out.println("Did not delete Template");
			e.printStackTrace();
		} finally {
			pgConnection.close();
		}
	}

	public void createWOImportTemplate(String templateName) throws SQLException {

		Connection pgConnection = getPostGreConnection();
		Statement pgStatement = null;

		pgStatement = pgConnection.createStatement();

		String insStatement = "INSERT INTO ma.workorder_import_templates(\n"
				+ "	workorder_import_template_name, product_header, line_header, project_header, lot_header, lane_header, \n"
				+ "	created_date,created_by,is_deleted, workorder_name_header)\n" + "	VALUES ( '" + templateName
				+ "', 'product1', 'line', 'project_header', 'lot', 'lane','01/01/2019',1, 'false', 'WorkOrderName')";

		System.out.println(insStatement);

		try {
			pgStatement.executeUpdate(insStatement);
			System.out.println("Template Created Successfully");

		} catch (Exception e) {
			pgConnection.rollback();
			System.out.println("Did not Create Template");
			e.printStackTrace();
		} finally {
			pgConnection.close();
		}
	}

	public void RunPgSqlEmailTransaction(String emailAddress) throws SQLException {
		// String userId = "" ;
		Connection pgConnection = getPostGreConnection();
		Statement pgStatement = null;

		pgStatement = pgConnection.createStatement();

		// pgStatement = pgConnection.createStatement();
		// CallableStatement ma_usp_insert_emailAddress_Proc =
		// pgConnection.prepareCall("{call ma.usp_email_configuration( ?) }");
		// emailJson = "{\"email_address:abc@tcs.com", \"is_deleted\":true,
		// \"notification_configuration_id\":1}";
		// emailJson =
		// "[{\"EmailNotificationId\":-1,\"EmailAddress\":\"abc@tcs12345.com\",
		// \"IsDeleted\":\"false\",
		// \"NotificationConfigId\":\"1\",\"EnableFlag\":\"true\"}]";

		String emailStatement = "INSERT INTO ma.email_notification(email_address, is_deleted, notification_configuration_id) VALUES (\'"
				+ emailAddress + "\',false,1);";
		System.out.println(emailStatement);

		try {
			// PGobject jsonObject = new PGobject();
			// jsonObject.setType("json");
			// jsonObject.setValue(emailJson);
			// ma_usp_insert_workorder_Proc.setObject(1, jsonObject);
			// ma_usp_insert_workorder_Proc.setString(2, userId);
			// ma_usp_insert_workorder_Proc.registerOutParameter(1,Types.INTEGER);
			pgStatement.executeUpdate(emailStatement);
			// Result = ma_usp_insert_workorder_Proc.getInt(1);
			// ma_usp_insert_emailAddress_Proc.close();
			System.out.println("Created Email Notification successfully");

		} catch (Exception e) {
			pgConnection.rollback();
			System.out.println("Did not Created Email Notification");
		} finally {
			pgConnection.close();
		}
	}

	public void RunPgSqlEmailMultipleTransaction(String emailAddress) throws SQLException {
		// String userId = "" ;
		Connection pgConnection = getPostGreConnection();
		Statement pgStatement = null;

		pgStatement = pgConnection.createStatement();
		// CallableStatement ma_usp_insert_emailAddress_Proc =
		// pgConnection.prepareCall("{call ma.usp_email_configuration( ?) }");
		String emailStatement = "INSERT INTO ma.email_notification(email_address, is_deleted, notification_configuration_id) VALUES (\'"
				+ emailAddress + "\',false,1);";
		String emailStatement1 = "INSERT INTO ma.email_notification(email_address, is_deleted, notification_configuration_id) VALUES (\'"
				+ emailAddress + "\',false,2);";
		String emailStatement2 = "INSERT INTO ma.email_notification(email_address, is_deleted, notification_configuration_id) VALUES (\'"
				+ emailAddress + "\',false,5);";
		// System.out.println(emailStatement+ ","+emailStatement1+ ","+emailStatement2);

		try {
			pgStatement.executeUpdate(emailStatement);
			pgStatement.executeUpdate(emailStatement1);
			pgStatement.executeUpdate(emailStatement2);
			// ma_usp_insert_emailAddress_Proc.close();
			System.out.println("Created Email Notification successfully");
		} catch (Exception e) {
			pgConnection.rollback();
			System.out.println("Did not Created Email Notification");
		} finally {
			pgConnection.close();
		}
	}

	public void deleteEmailforNotification(String emailAddress) throws SQLException {

		Connection pgConnection = getPostGreConnection();
		Statement pgStatement = null;

		pgStatement = pgConnection.createStatement();

		String emailStatement = "delete from ma.email_notification where email_address = \'" + emailAddress + "\' ;";
		// System.out.println(emailStatement);

		try {
			pgStatement.executeUpdate(emailStatement);
			System.out.println("Deleted Email Notification successfully");

		} catch (Exception e) {
			pgConnection.rollback();
			System.out.println("Did not delete Email Notification");
			e.printStackTrace();
		} finally {
			pgConnection.close();
		}
	}

}
