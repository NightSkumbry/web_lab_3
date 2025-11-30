package ru.ns.lab.controller.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ru.ns.lab.service.params.ParamsException;
import ru.ns.lab.service.params.ParamsParser;

public class YValidator implements Validator<String> {
    ParamsParser parser = new ParamsParser();
    
    @Override
    public void validate(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Введите число",
                "Значение Y должно быть указано"
            ));
        }

        double y;

        try {
            y = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Неверный формат числа",
                "Значение Y должно быть числом"
            ));
        }

        if (Double.isNaN(y)) {
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Недопустимое значение",
                "Значение Y не может быть NaN"
            ));
        }

        if (Double.isInfinite(y)) {
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Недопустимое значение",
                "Значение Y не может быть бесконечностью"
            ));
        }

        try {
            y = parser.parseY(value);
        } catch (ParamsException e) {
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Недопустимое значение",
                e.getMessage()
            ));
        }

        if (y <= -5 || y >= 5) {
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Недопустимое значение",
                "Значение Y должно быть в диапазоне от (-5; 5)"
            ));
        }
    }
    
}
