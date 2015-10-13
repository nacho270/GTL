package ar.com.fwcommon.mail;

public interface MailObserver {

	public void update(MailSender.MailInfo mailInfo);

}