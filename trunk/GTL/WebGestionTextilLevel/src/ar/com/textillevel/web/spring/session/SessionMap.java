package ar.com.textillevel.web.spring.session;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.web.spring.exception.NoSessionException;

public class SessionMap {

	private static final Logger log = Logger.getLogger(SessionMap.class);

	public static String SESSION_TOKEN_KEY = "session_token";
	
	private final Map<String, Long> innerMap;
	private static volatile SessionMap instance;
	private final Timer sessionPurgerScheduler;
	private static final long MAX_SESSION = System.getProperty("textillevel.session.max_time") == null ? DateUtil.ONE_MINUTE * 10 : Long.parseLong(System.getProperty("textillevel.session.max_time"));

	private SessionMap() {
		innerMap = new ConcurrentHashMap<String, Long>();
		sessionPurgerScheduler = new Timer();
		sessionPurgerScheduler.schedule(new TimerTask() {

			@Override
			public void run() {
				synchronized (innerMap) {
					log.debug("PURGING SESSIONS");
					for (String token : innerMap.keySet()) {
						if ((System.currentTimeMillis() - innerMap.get(token)) > MAX_SESSION) {
							clearSession(token);
						}
					}
				}
			}
		},0, DateUtil.ONE_MINUTE);
	}

	public void updateSession(String token) {
		if(token!=null){
			innerMap.put(token, new Long(System.currentTimeMillis()));
		}
	}

	public void validateSession(String token) throws Exception {
		if (StringUtil.isNullOrEmpty(token)) {
			throw new NoSessionException();
		}
		Long sessionStartTime = innerMap.get(token);
		if (sessionStartTime == null) {
			throw new NoSessionException();
		}
		if ((System.currentTimeMillis() - sessionStartTime.longValue()) > MAX_SESSION) {
			clearSession(token);
			throw new NoSessionException();
		}
	}

	public static SessionMap getInstance() {
		if (instance == null) {
			synchronized (SessionMap.class) {
				if (instance == null) {
					instance = new SessionMap();
				}
			}
		}
		return instance;
	}

	public void newSession(String token) {
		innerMap.put(token, new Long(System.currentTimeMillis()));
	}

	public void clearSession(String token) {
		if(token!=null){
			innerMap.remove(token);
		}
	}
}
