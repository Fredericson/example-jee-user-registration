package org.example.jee.validation;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.jee.model.Member;

public class OneFieldMustContainValueValidator implements
		ConstraintValidator<OneFieldMustContainValue, Member> {

	private String[] fieldNames;

	@Override
	public void initialize(OneFieldMustContainValue annotation) {
		this.fieldNames = annotation.fieldNames();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(Member member, ConstraintValidatorContext ctx) {

		if (member == null) {
			return true;
		}

		try {
			for (String fieldName : fieldNames) {
				final String fieldValue = BeanUtils.getProperty(member,
						fieldName);
				if (StringUtils.isNotBlank(fieldValue)) {
					return true;
				}
			}
			ctx.disableDefaultConstraintViolation();
			ctx.buildConstraintViolationWithTemplate(
					ctx.getDefaultConstraintMessageTemplate())
					.addNode(fieldNames[0]).addConstraintViolation();
			return false;

		} catch (final NoSuchMethodException ex) {
			throw new RuntimeException(ex);

		} catch (final InvocationTargetException ex) {
			throw new RuntimeException(ex);

		} catch (final IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

}
