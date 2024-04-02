package br.com.don.erp.converter;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("dinheiroConverter")
public class DinheiroConverter implements Converter<BigDecimal> {

    @Override
    public BigDecimal getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty() || value.equals("R$ 0,00")) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(value.replace("R$ ", "").replace(",", "."));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BigDecimal value) {
        if (value == null) {
            return "";
        }

        return "R$ " + value.toString();
    }
}
