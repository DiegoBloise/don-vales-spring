package br.com.don.erp.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.com.don.erp.model.Entregador;

@FacesConverter("entregadorConverter")
public class EntregadorConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return this.getAttributesFrom(component).get(value);
            } catch (Exception e) {
                throw new ConverterException("Erro ao converter entregador: ", e);
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            Entregador entregador = (Entregador) value;
            this.addAttribute(component, entregador);
            return String.valueOf(entregador.getId());
        }
        return null;
    }

    private Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }

    private void addAttribute(UIComponent component, Entregador entregador) {
        this.getAttributesFrom(component).put(String.valueOf(entregador.getId()), entregador);
    }
}
