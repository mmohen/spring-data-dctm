package com.emc.documentum.springdata.entitymanager.attributes;

public class AttributeFactory {

	public static Attribute<?> getAttribute(Class<?> type, String attributeName) {
		if(type == java.lang.String.class) {
			return new StringAttribute(attributeName);
		}
		else if (type == java.lang.Integer.class || type == int.class){
			return new IntAttribute(attributeName);
		}
		else if (type == java.lang.Double.class || type == double.class){
			return new DoubleAttribute(attributeName);
		}
		else if (type == java.lang.Long.class || type == long.class){
			return new LongAttribute(attributeName);
		}
		else if (type == java.lang.Short.class || type == short.class){
			return new ShortAttribute(attributeName);
		}
		else if (type == java.lang.Float.class || type == float.class){
			return new FloatAttribute(attributeName);
		}
		else if (type == java.lang.Byte.class || type == byte.class){
			return new ByteAttribute(attributeName);
		}
		else if (type == java.lang.Boolean.class || type == boolean.class){
			return new BooleanAttribute( attributeName);
		}
		else if (type == java.lang.Character.class || type == char.class){
			return new CharacterAttribute(attributeName);
		}
		return null;
	}

}
