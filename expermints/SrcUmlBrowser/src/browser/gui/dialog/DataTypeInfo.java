package browser.gui.dialog;

import java.lang.reflect.Type;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class DataTypeInfo {

	public static String discoverGenericTypes(Type type)
	{
		StringBuilder buidler = new StringBuilder();
		
		if ( type instanceof WildcardType) {
			
			WildcardType item = WildcardType.class.cast(type);

			buidler.append("<");
			for ( Type args : item.getUpperBounds()) {
				buidler.append(DataTypeInfo.discoverGenericTypes(args));
			}
			
			for ( Type args : item.getLowerBounds()) {
				buidler.append(DataTypeInfo.discoverGenericTypes(args));
			}
			buidler.append(">");
			
		} else if ( type instanceof ParameterizedType) {
			
			ParameterizedType item = ParameterizedType.class.cast(type);
			
			buidler.append(DataTypeInfo.discoverGenericTypes(item.getRawType()));
			buidler.append("<");
			for (Type args : item.getActualTypeArguments()) {
				buidler.append(DataTypeInfo.discoverGenericTypes(args));
			}
			buidler.append(">");
		
		} else if ( type instanceof TypeVariable<?>) {
			
			TypeVariable<?> item = TypeVariable.class.cast(type);
			for (Type args : item.getBounds()) {
				buidler.append(DataTypeInfo.discoverGenericTypes(args));
			}
							
		} else if ( type instanceof GenericArrayType) {
			
			GenericArrayType item = GenericArrayType.class.cast(type);
			
			buidler.append(DataTypeInfo.discoverGenericTypes(item.getGenericComponentType()));
			
		} else {
			buidler.append( ((Class<?>)type).getSimpleName() );
		}
		return buidler.toString();
	}
}
