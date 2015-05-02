package ar.com.textillevel.gui.util.passwordanalyzer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordAnalyzer {
	
	private static PasswordAnalyzer instance = new PasswordAnalyzer();
	
	private static final Integer MIN_LEN_PASS = 5;
	private static final Integer MED_LEN_PASS = 8;
	
	private static final String REG_EXP_DIGITOS = ".*\\d+.*";
	private static final String REG_MAY = ".*[A-Z]+.*";
	private static final String REG_MIN = ".*[a-z]+.*";
	private static final String CARACTERES_RAROS = ".*[!,@,#,$,%,^,&,*,?,_,~]+.*";
	
	private static final Pattern patternDigitos = Pattern.compile(REG_EXP_DIGITOS);
	private static final Pattern patternMay = Pattern.compile(REG_MAY);
	private static final Pattern patternMin = Pattern.compile(REG_MIN);
	private static final Pattern patternCaracRaros = Pattern.compile(CARACTERES_RAROS);
	
	public static PasswordAnalyzer getInstance(){
		return instance;
	}
	
	public EPasswordStrength analyze(String pass){
		if(esFuerte(pass)){
			return EPasswordStrength.FUERTE;
		}else if(esMedio(pass)){
			return EPasswordStrength.MEDIO;
		}else{
			return EPasswordStrength.DEBIL;
		}
	}
	
	private boolean esFuerte(String pass){
		return pass.length()>MED_LEN_PASS && tieneMayusculas(pass) && tieneMinusculas(pass)  && tieneDigitos(pass) && tieneCarcteresRaros(pass);
	}
	
	private boolean esMedio(String pass){
		return pass.length()>MIN_LEN_PASS&&pass.length()<=MED_LEN_PASS && tieneMayusculas(pass) && tieneMinusculas(pass) && tieneDigitos(pass);
	}

	private boolean tieneMinusculas(String pass) {
		Matcher matcher = patternMin.matcher(pass.trim());
		return matcher.matches();
	}

	private boolean tieneMayusculas(String pass) {
		Matcher matcher = patternMay.matcher(pass.trim());
		return matcher.matches();
	}

	private boolean tieneCarcteresRaros(String pass) {
		Matcher matcher = patternCaracRaros.matcher(pass.trim());
		return matcher.matches();
	}

	private boolean tieneDigitos(String pass) {
		Matcher matcher = patternDigitos.matcher(pass.trim());
		return matcher.matches();
	}
}
