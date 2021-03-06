package com.workflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.customers.DAOFactory;
import com.customers.DAOLookup;

public class FinanceWorkflow extends Thread{
	int suspend_resume;
	int customerid;
	Persisting persist;
	public boolean flag;
	public FinanceWorkflow(int suspend_resume,int customerid){
		persist=new Persisting();
		this.suspend_resume=suspend_resume;
		this.customerid=customerid;
		flag=true;
		this.start();	
	}
	
	@Override
	public void run()
	{
		int requestid=persist.persistRequest(this.currentThread().getId(),0, "finance", "prov", "in prov");
		String api = "http://localhost:8086/TestRestServ/rest/om/billingAccountPull/ves";//update string
		int responseCode = 0;	
		JSONObject response=null;
//		URL url;
//		try {
//			url = new URL(api);
//			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
//			httpConnection.connect();
//			responseCode = httpConnection.getResponseCode();
//		if (responseCode == 200) {
//			BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
//			String str = "";
//			StringBuilder responseJson = new StringBuilder();
//			while ((str = br.readLine()) != null) {
//				responseJson.append(str);
//			}
//			response=new JSONObject(new String(responseJson));
//		}
//		else{
//			flag=false;
//		}
		//if(response.getString("provisioningstatus").equals("suspended"))
		System.out.println("In finance"+suspend_resume+" "+customerid);
		if(suspend_resume==0){
			DAOLookup.setcInfo("request");
			DAOFactory df=DAOLookup.getDAOObject();
			df.update("status","complete",requestid);
			DAOLookup.setcInfo("customer");
			DAOFactory df2=DAOLookup.getDAOObject();
			df2.update("customer_status","suspended",customerid);
		}else
			//if(response.getString("provisioningstatus").equals("resumed")){
			if(suspend_resume==1){	
			DAOLookup.setcInfo("request");
				DAOFactory df=DAOLookup.getDAOObject();
				df.update("status","complete",requestid);
				DAOLookup.setcInfo("customer");
				DAOFactory df2=DAOLookup.getDAOObject();
				df2.update("customer_status","active",customerid);
			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			flag=false;
//		} catch (JSONException e) {
//			e.printStackTrace();
//			flag=false;
//		}
	}

}
