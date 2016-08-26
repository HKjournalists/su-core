package com.gettec.fsnip.fsn.util;

import java.math.BigDecimal;

public class FsnD {
    public static final double round(double v,int lDecs,boolean bTrunc){
        if(lDecs < 0)
            return v;
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");

        return b.divide(one,lDecs ,bTrunc ? BigDecimal.ROUND_DOWN : BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static final double round(double v,int lDecs){
       return round(v,lDecs,false);
    }
    public static final double sub(double v1, double v2) {
        return add(v1, -v2, 0, 0, 0, 0, 2);
     }
     public static final double sub(double v1, double v2, double v3) {
        return add(v1, -v2, -v3, 0, 0, 0, 3);
     }
     public static final double sub(double v1, double v2, double v3, double v4) {
        return add(v1, -v2, -v3, -v4, 0, 0, 4);
     }
     public static final double sub(double v1, double v2, double v3, double v4,
                                    double v5) {
        return add(v1, -v2, -v3, -v4, -v5, 0, 5);
     }
     public static final double sub(double v1, double v2, double v3, double v4,
                                    double v5, double v6) {
        return add(v1, -v2, -v3, -v4, -v5, -v6, 6);
     }
     public static final double add(double v1, double v2) {
         return add(v1, v2, 0, 0, 0, 0, 2);
      }
      public static final double add(double v1, double v2, double v3) {
         return add(v1, v2, v3, 0, 0, 0, 3);
      }
      public static final double add(double v1, double v2, double v3, double v4) {
         return add(v1, v2, v3, v4, 0, 0, 4);
      }
      public static final double add(double v1, double v2, double v3, double v4,
                                     double v5) {
         return add(v1, v2, v3, v4, v5, 0, 5);
      }
      public static final double add(double v1, double v2, double v3, double v4,
                                     double v5, double v6) {
         return add(v1, v2, v3, v4, v5, v6, 6);
      }

      private static final double add(double v1, double v2, double v3, double v4,
                                      double v5, double v6, int n) {
         BigDecimal b1 = new BigDecimal(Double.toString(v1));
         BigDecimal b2 = new BigDecimal(Double.toString(v2));
         BigDecimal b3 = null;
         BigDecimal b4 = null;
         BigDecimal b5 = null;
         BigDecimal b6 = null;

         switch (n) {
            case 2:
               return b1.add(b2).doubleValue();
            case 6:
               b6 = new BigDecimal(Double.toString(v6));
            case 5:
               b5 = new BigDecimal(Double.toString(v5));
            case 4:
               b4 = new BigDecimal(Double.toString(v4));
            case 3:
               b3 = new BigDecimal(Double.toString(v3));
         }

         switch (n) {
            case 3:
               return b1.add(b2).add(b3).doubleValue();
            case 4:
               return b1.add(b2).add(b3).add(b4).doubleValue();
            case 5:
               return b1.add(b2).add(b3).add(b4).add(b5).doubleValue();
            case 6:
               return b1.add(b2).add(b3).add(b4).add(b5).add(b6).doubleValue();
            default:
               return 0;
         }
      }
}
