package com.remmylife.client;

import java.util.ArrayList;
import java.util.Date;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.HttpTransportSE;

import com.remmylife.diary.*;

	public class ManagerAccess {

		static String WEB_SERVICE_URL = "http://101.5.154.170:8080/RemMyLifeServer/services/RemMyLifeServer?wsdl";
		static String TARGET_NAME_SPACE = "http://service.remmylife.com";
		
		
		void init(){
		}
		public ManagerAccess() {
			init();
			
		}
		
		public ManagerAccess(String web_service_url, String target_name_space) {
			init();
			this.WEB_SERVICE_URL=web_service_url;
			this.TARGET_NAME_SPACE= target_name_space;
		}
		
		// user �Ĳ���
		public boolean loginByName(String userName, String password){
			return callMethodInOutPrim("loginByName", "userName", userName, false, "password", password, false);
			
		}
		
		public User getUser(User user){
			return (User)callMethodInOut("getUser", "user",user,true);
			
		}
		
		public boolean saveUser(User user){
			return callMethodInOutPrim("saveUser", "user",user, true);
		}
		
		public boolean deleteUser(User user){
			return callMethodInOutPrim("deleteUser", "user",user, true);
			
		
		}
		
		// diary �Ĳ���
		
		public boolean deleteDiary(Diary diary){
			return callMethodInOutPrim("deleteDiary", "diary",diary, true);
	
		}
		
		public boolean saveDiary(Diary diary){

			return callMethodInOutPrim("saveDiary", "diary",diary, true);
	
		}
		
		public void shareDiary(Diary diary, User self){
			callMethodIn("shareDiary", "self",self, true);

		}
		
		public void unshareDiary(Diary diary, User self){
			callMethodIn("unshareDiary", "self",self, true);
		}
		
		
		@SuppressWarnings("unchecked")
		public ArrayList<Diary> getDiaryList(User self, boolean own) {
			return (ArrayList<Diary>)callMethodInOut("getDiaryList", 
					"self",self, true,
					"own",own, false);

		}
		
		public Diary getDiary(Diary diary){
			return (Diary)callMethodInOut("getDiary", "diary",diary,true);

		}
		
		// Comment
		public void saveComment(Diary diary,User user,String note){
			
			callMethodIn("saveComment", "diary",diary, true,"user",user, true,"note",note, false);
			
			

		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Comment> getComment(Diary diary){

			return (ArrayList<Comment>)callMethodInOut("getComment", 
					"diary",diary, true);
			

		}
		
		
		
		
		// search
		@SuppressWarnings("unchecked")
		public ArrayList<Diary> searchByTitle(String title, User self, boolean own){
			return (ArrayList<Diary>)callMethodInOut("searchByTitle", 
					"title", title, false,
					"self", self, true,
					"own", own, false);

		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Diary> searchByDate(Date date, User self, boolean own){ 
			return (ArrayList<Diary>)callMethodInOut("searchByDate", 
					"date",date,true,
					"self",self,true,
					"own",own,false);
		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Diary> searchByType(DiaryType type, User self, boolean own){
			return (ArrayList<Diary>)callMethodInOut("searchByType", 
					"type",type,true,
					"self",self,true,
					"own", own, false);
		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Diary> sortByDate(User self, boolean own){
			return (ArrayList<Diary>)callMethodInOut("sortByDate", 
					"self",self, true,
					"own",own, false);
		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Diary> searchByContent(String content, User self, boolean own){
			return (ArrayList<Diary>) callMethodInOut("searchByContent",
					"content",content,false,
					"self",self,true,
					"own",own,false);
			
		}
		
		 // Weibo
        	public boolean deleteFromWeibo(Diary diary, String accessToken){
            		return  callMethodInOutPrim("deleteFromWeibo",
                		"diary", diary, true,
               			"accessToken", accessToken, false);
        	}
    
        	public boolean shareToWeibo(Diary diary, String accessToken){
            		return callMethodInOutPrim("shareToWeibo",
                    		"diary", diary, true,
                    		"accessToken", accessToken, false);
        	}
		
		
		private void callMethodIn(String method, Object... args ) {
			SoapObject bodyOutObject = new SoapObject(TARGET_NAME_SPACE, method);
			for (int i = 0; i < args.length; i+= 3) {
				String argName = (String) args[i];
				Object arg = args[i+1];
				boolean encode = (Boolean) args[i+2];
				if (encode) {
					bodyOutObject.addProperty(argName, Base64.encode(Utils.convertToByteArray(arg)));
				} else {
					bodyOutObject.addProperty(argName, arg);
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = bodyOutObject;
			HttpTransportSE ht = new HttpTransportSE(WEB_SERVICE_URL);
			try {
				ht.call(null, envelope);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public boolean callMethodInOutPrim(String method, Object... args) {
			SoapObject bodyOutObject = new SoapObject(TARGET_NAME_SPACE, method);
			for (int i = 0; i < args.length; i += 3) {
				String argName = (String) args[i];
				Object arg = args[i+1];
				boolean encode = (Boolean) args[i+2];
				if (encode) {
					bodyOutObject.addProperty(argName, Base64.encode(Utils.convertToByteArray(arg)));
				} else {
					bodyOutObject.addProperty(argName, arg);
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut =  bodyOutObject;
			HttpTransportSE ht = new HttpTransportSE(WEB_SERVICE_URL);
			try {
				ht.call(null, envelope);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			return Boolean.parseBoolean(soapObject.getProperty(0).toString());
		}
		
		private Object callMethodInOut(String method, Object... args ) {
			SoapObject bodyOutObject = new SoapObject(TARGET_NAME_SPACE, method);
			for (int i = 0; i < args.length; i+= 3) {
				String argName = (String) args[i];
				Object arg = args[i+1];
				boolean encode = (Boolean) args[i+2];
				if (encode) {
					bodyOutObject.addProperty(argName, Base64.encode(Utils.convertToByteArray(arg)));
				} else {
					bodyOutObject.addProperty(argName, arg);
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = bodyOutObject;
			HttpTransportSE ht = new HttpTransportSE(WEB_SERVICE_URL);
			try {
				ht.call(null, envelope);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SoapObject bodyInObject =(SoapObject) envelope.bodyIn;
			byte[] bytes = Base64.decode(bodyInObject.getProperty(0).toString());
			return Utils.convertToObject(bytes);
		}
		
	}
		


