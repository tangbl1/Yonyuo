package nc.vo.uapbd.pricemanage;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;


public class Enumerate1 extends MDEnum{
    public Enumerate1(IEnumValue enumValue){
        super(enumValue);
    }

    public static final Enumerate1 Enumerate1_固定价格  = MDEnum.valueOf(Enumerate1.class, java.lang.String.valueOf("3"));
    public static final Enumerate1 Enumerate1_增价  = MDEnum.valueOf(Enumerate1.class, java.lang.String.valueOf("1"));
    public static final Enumerate1 Enumerate1_扣价  = MDEnum.valueOf(Enumerate1.class, java.lang.String.valueOf("2"));

}
