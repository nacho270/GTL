package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.recibo.to.ChequeFullTO;
import ar.com.textillevel.web.spring.exception.GTLException;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value = "/cheque", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class ChequeRestController {

	@RequestMapping
	public @ResponseBody ChequeFullTO buscarCheque(@RequestParam(value = "idCheque") Integer idCheque) {
		return new ChequeFullTO(BeanHelper.geChequeFacade().getChequebyId(idCheque));
	}

	@RequestMapping(value = "/byNro")
	public @ResponseBody ChequeFullTO buscarChequePorNumero(@RequestParam(value = "letraCheque") String letraCheque, @RequestParam(value = "nroCheque") Integer nroCheque) {
		Cheque cheque = BeanHelper.geChequeFacade().getChequeByNumeracion(letraCheque, nroCheque);
		if (cheque == null) {
			throw new GTLException(777, "No se encontró el cheque");
		}
		return new ChequeFullTO(cheque);
	}
}
